package org.codenarc.idea.gen

import com.intellij.codeHighlighting.HighlightDisplayLevel
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.apache.commons.lang3.StringUtils
import org.codenarc.CodeNarc
import org.codenarc.idea.CodeNarcInspectionTool
import org.codenarc.idea.ui.Helpers
import org.codenarc.rule.AbstractRule
import org.codenarc.rule.comments.JavadocEmptyFirstLineRule
import org.codenarc.rule.convention.ImplicitReturnStatementRule
import org.codenarc.rule.formatting.ClosureStatementOnOpeningLineOfMultipleLineClosureRule
import org.codenarc.rule.formatting.SpaceAfterMethodCallNameRule
import org.codenarc.rule.formatting.SpaceAroundMapEntryColonRule
import org.codenarc.rule.grails.GrailsDomainHasEqualsRule
import org.codenarc.rule.grails.GrailsDomainHasToStringRule
import org.codenarc.rule.groovyism.ExplicitCallToAndMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToCompareToMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToOrMethodRule
import org.codenarc.rule.imports.UnusedImportRule
import org.codenarc.rule.security.JavaIoPackageAccessRule
import org.codenarc.rule.unnecessary.UnnecessaryDotClassRule
import org.codenarc.rule.unnecessary.UnnecessaryGStringRule
import org.codenarc.rule.unnecessary.UnnecessaryReturnKeywordRule
import org.codenarc.rule.unnecessary.UnnecessarySemicolonRule
import org.codenarc.rule.unused.UnusedArrayRule
import org.codenarc.rule.unused.UnusedMethodParameterRule
import org.codenarc.rule.unused.UnusedObjectRule
import org.codenarc.rule.unused.UnusedPrivateFieldRule
import org.codenarc.rule.unused.UnusedPrivateMethodParameterRule
import org.codenarc.rule.unused.UnusedPrivateMethodRule
import org.codenarc.rule.unused.UnusedVariableRule
import org.jetbrains.annotations.Nullable

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Run this class using <code>/.gradlew run</code> to
 */
@CompileStatic
class RuleInspectionsGenerator {

    static class InspectionDescriptor {

        String implementationClass
        String shortName
        String displayName
        String groupPath
        String groupKey
        String level
        boolean enabledByDefault

        // status flags
        boolean hasQuickFix
        boolean hasSpec
        boolean cleanupTool

        // not used yet
        String groupBundle

    }

    static void main(String[] args) {
        if (args.length != 1) {
            println 'Expecting exactly one argument - path to the project root'
        }

        String projectRoot = args[0]

        println "Generating rule classes for project root: ${projectRoot}"

        List<InspectionDescriptor> classes = generateClasses(projectRoot)
        report classes
    }

    private static final Set<Class<?>> DISABLED_BY_DEFAULT_RULES = new HashSet<>([
            ClosureStatementOnOpeningLineOfMultipleLineClosureRule, // unreliable
            ExplicitCallToAndMethodRule,                            // often disabled
            ExplicitCallToOrMethodRule,                             // often disabled
            ExplicitCallToCompareToMethodRule,                      // often disabled
            GrailsDomainHasEqualsRule,                              // quite difficult to implement
            GrailsDomainHasToStringRule,                            // not always a good thing, may trigger db calls
            JavaIoPackageAccessRule,                                // often disabled
            JavadocEmptyFirstLineRule,                              // unreliable
            // ImplicitClosureParameterRule,                        // we might consider disabling this by default as well
            SpaceAroundMapEntryColonRule,                           // sometimes we want to align the colons in a column
            SpaceAfterMethodCallNameRule,                           // failing
            UnnecessaryReturnKeywordRule,                           // clashes with ImplicitReturnStatementRule
            UnnecessarySemicolonRule,                               // unreliable
            UnusedArrayRule,                                        // handled by IntelliJ
            UnusedMethodParameterRule,                              // handled by IntelliJ
            UnusedObjectRule,                                       // handled by IntelliJ
            UnusedPrivateFieldRule,                                 // handled by IntelliJ
            UnusedPrivateMethodRule,                                // handled by IntelliJ
            UnusedPrivateMethodParameterRule,                       // handled by IntelliJ
            UnusedVariableRule,                                     // handled by IntelliJ
    ])

    private static final Set<Class<?>> CLEANUP_AVAILABLE = new HashSet<>([
            ImplicitReturnStatementRule,
            UnnecessaryDotClassRule,
            UnnecessaryGStringRule,
            UnusedImportRule,
    ])

    private static final String RULESETS_PATH = 'rulesets/'

    private static final String[] RULESETS = [
            'basic',                        // common
            'braces',                       // common
            'comments',                     // only 6 usages in public configs
            'concurrency',                  // common
            'convention',                   // common
            'design',                       // common
            'dry',                          // common, often disabled in specs
            'enhanced',                     // common
            'exceptions',                   // common
            'formatting',                   // common, more graceful line length, fixed regex for map literals
            'generic',                      // common
            'grails',                       // not that common
            'groovyism',                    // common, come explicit call checks disabled
            'imports',                      // common, wildcard imports often allowed but not here
            'jdbc',                         // common
            'jenkins',                      // common
            'junit',                        // common
            'logging',                      // common
            'naming',                       // common
            'security',                     // common
            'serialization',                // common
            'size',                         // common
            'unnecessary',                  // common
            'unused',                       // common
    ]

    private final static Pattern RULE_CLASS_PATTERN = Pattern.compile(".*class='(.*?)'.*")
    private final static Pattern RULE_GROUP_PATTERN = Pattern.compile('.*/([^.]*)\\.xml')

    private final String ruleClass
    private final String group

    RuleInspectionsGenerator(String ruleClass, String group) {
        this.ruleClass = ruleClass
        this.group = group
    }

    private static List<InspectionDescriptor> generateClasses(String projectRoot) {
        List<InspectionDescriptor> classes = generateClassFiles(projectRoot)
        updatePluginXml(projectRoot, classes)
        return classes
    }

    private static List<InspectionDescriptor> generateClassFiles(String projectRoot) {
        List<InspectionDescriptor> generatedClasses = []
        String[] rulesetFiles = getRulesetFiles()

        for (String ruleset in rulesetFiles) {
            new BufferedReader(new InputStreamReader(RuleInspectionsGenerator.getResourceAsStream('/' + ruleset))).withCloseable { reader ->
                try {
                    String line
                    while ((line = reader.readLine()) != null) {
                        Matcher m = RULE_CLASS_PATTERN.matcher(line)
                        if (m.find()) {
                            String groupName = ruleset
                            Matcher n = RULE_GROUP_PATTERN.matcher(ruleset)
                            if (n.find()) {
                                groupName = Character.toUpperCase(n.group(1).charAt(0)).toString() + n.group(1).substring(1)
                            }
                            InspectionDescriptor generated = new RuleInspectionsGenerator(m.group(1), groupName).generateSingleClassFile(projectRoot)

                            if (generated) {
                                generatedClasses.add(generated)
                            }
                        }
                    }
                } catch (IOException | ClassFormatError e) {
                    e.printStackTrace()
                }
                // silent
            }
        }

        return generatedClasses
    }

    @CompileDynamic
    private static void updatePluginXml(String projectRoot, List<InspectionDescriptor> generatedClasses) {
        List<String> pathSegmentsToPluginXml = [
                projectRoot,
                'src',
                'main',
                'resources',
                'META-INF',
        ]

        File pluginDescriptor = new File(pathSegmentsToPluginXml.join(File.separator), 'plugin.xml')

        XmlParser parser = new XmlParser()
        Node ideaPlugin = parser.parse(pluginDescriptor)

        ideaPlugin.extensions.localInspection.each { it.parent().remove(it) }

        for (InspectionDescriptor generatedClass in generatedClasses) {
            Node inspectionNode = new NodeBuilder().localInspection(
                    language: 'Groovy',
                    implementationClass: generatedClass.implementationClass,
                    shortName: generatedClass.shortName,
                    displayName: generatedClass.displayName,
                    groupPath: generatedClass.groupPath,
                    groupKey: generatedClass.groupKey,
                    level: generatedClass.level,
                    enabledByDefault: generatedClass.enabledByDefault,
                    hasStaticDescription: true,
                    cleanupTool: generatedClass.cleanupTool
            )
            ideaPlugin.extensions[0].append(inspectionNode)
        }

        XmlNodePrinter printer = new XmlNodePrinter(new PrintWriter(new FileWriter(pluginDescriptor)))
        printer.preserveWhitespace = true
        printer.print(ideaPlugin)

        while (printer = null) {

        }
    }

    private static String[] getRulesetFiles() {
        try {
            return getResourceListing()
        } catch (URISyntaxException | IOException ignored) {
            return RULESETS // fallback
        }
    }

    /**
     * List directory contents for a resource folder. Not recursive.
     * This is basically a brute-force implementation.
     * Works for regular files and also JARs.
     *
     * @author Greg Briggs
     * @return Just the name of each member item, not the full paths.
     * @throws URISyntaxException* @throws IOException
     */
    private static String[] getResourceListing() {
        URL dirURL = CodeNarc.class.getClassLoader().getResource(RULESETS_PATH)
        if (dirURL != null && dirURL.getProtocol() == 'file') {
            /* A file path: easy enough */
            return new File(dirURL.toURI()).list()
        }

        if (dirURL == null) {
            /*
             * In case of a jar file, we can't actually find a directory.
             * Have to assume the same jar as clazz.
             */
            String me = CodeNarc.class.getName().replace('.', '/') + '.class'
            dirURL = CodeNarc.class.getClassLoader().getResource(me)
        }

        if (dirURL != null) {
            String proto = dirURL.getProtocol()
            if (proto != null && proto == 'jar') {
                /* A JAR path */
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf('!'))
                //strip out only the JAR file
                JarFile jar = new JarFile(URLDecoder.decode(jarPath, 'UTF-8'))
                Enumeration<JarEntry> entries = jar.entries() //gives ALL entries in jar
                Set<String> result = new HashSet<>() //avoid duplicates in case it is a subdirectory
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName()
                    if (name.startsWith(RULESETS_PATH)) { //filter according to the path
                        if (name.substring(9).isEmpty()) {
                            continue
                        }

                        result.add(name)
                    }
                }
                return result.toArray(new String[0])
            }
        }

        throw new UnsupportedOperationException('Cannot list files for URL ' + dirURL)
    }

    private static String getLevelFromPriority(int priority) {
        switch (priority) {
            case 1: return HighlightDisplayLevel.ERROR.name
            case 2: return HighlightDisplayLevel.WARNING.name
            default: return HighlightDisplayLevel.WEAK_WARNING.name
        }
    }

    @SuppressWarnings(['CodeNarc.Println'])
    private static void report(List<InspectionDescriptor> classes) {
        println()
        println 'Inspection Tool Classes Generation Finished'
        println()
        println "${classes.size()} classes generated"
        println "${classes.size() - classes.count { it.hasQuickFix }}/${classes.size()} requires quick fix"
        println "${classes.size() - classes.count { it.hasSpec }}/${classes.size()} requires spec"

        println()
        println 'The following classes requires quick fix handling'
        for (InspectionDescriptor descriptor in classes.findAll { !it.hasQuickFix }) {
            println " * ${descriptor.implementationClass}"
        }

        println()
        println 'The following classes are waiting for a specification'
        for (InspectionDescriptor descriptor in classes.findAll { !it.hasSpec }) {
            println " * ${descriptor.implementationClass}"
        }
    }

    /**
     * Generates class Java code and returns the fully qualified name name of the class
     * @return the fully qualified name of the newly generated class or null if the rule cannot be supported
     */
    @Nullable
    @SuppressWarnings(['CodeNarc.TrailingWhitespace', 'CodeNarc.AbcMetric'])
    private InspectionDescriptor generateSingleClassFile(String projectRoot) {
        StringWriter sw = new StringWriter()
        PrintWriter printWriter = new PrintWriter(sw)

        Class<?> ruleClassInstance = Helpers.getRuleClassInstance(ruleClass)
        AbstractRule ruleInstance = ruleClassInstance.newInstance() as AbstractRule

        if (ruleInstance.compilerPhase > 3) {
            return null
        }

        String newClassPackage = "org.codenarc.idea.inspections.${group.toLowerCase()}"
        String newClassName = "${ruleClassInstance.simpleName[0..-5]}InspectionTool"

        List<String> paths = [
                projectRoot,
                'src',
                'main',
                'java',
        ]

        paths.addAll(newClassPackage.split('\\.'))

        File parentFile = new File(paths.join(File.separator))
        parentFile.mkdirs()

        File newSourceFile = new File(parentFile, newClassName + '.java')

        printWriter.println """
        package $newClassPackage;
        """

        Set<String> imports = new TreeSet<>([
                'javax.annotation.Generated',
                'com.intellij.codeInspection.LocalQuickFix',
                'com.intellij.psi.PsiElement',
                'org.codenarc.idea.CodeNarcInspectionTool',
                'org.codenarc.rule.Violation',
                ruleClass,
                'org.jetbrains.annotations.NotNull',
                'java.util.Collection',
                'java.util.Collections',
        ])

        if (ruleClassInstance in CLEANUP_AVAILABLE) {
            imports.add 'com.intellij.codeInspection.CleanupLocalInspectionTool'
        }

        if (newSourceFile.exists()) {
            imports.addAll newSourceFile.readLines()
                    .findAll { it.startsWith('import') }
                    .collect { it.substring(7, it.length() - 1) }
        }

        for (String imported in imports.sort()) {
            printWriter.println("import $imported;")
        }

        // codenarc-disable LineLength
        printWriter.println """
        @Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
        public class $newClassName extends CodeNarcInspectionTool<$ruleClassInstance.simpleName> ${ ruleClassInstance in CLEANUP_AVAILABLE ? 'implements CleanupLocalInspectionTool ' : '' }{
        
            // this code has been generated from $ruleClass
        
            public static final String GROUP = "$group";
        
            public $newClassName() {
                super(new $ruleClassInstance.simpleName());
                applyDefaultConfiguration(getRule());
            }
            
            @Override
            public String getRuleset() {
                return GROUP;
            }
        """.stripIndent()
        // codenarc-enable LineLength

        for (MetaProperty prop : Helpers.proxyableProps(ruleClassInstance)) {
            String getter
            String setter

            if (prop instanceof MetaBeanProperty) {
                MetaBeanProperty beanProp = (MetaBeanProperty) prop
                getter = beanProp.getter.name
                setter = beanProp.setter.name
            } else {
                String capitalizedPropName = StringUtils.capitalize(prop.name)
                getter = 'get' + capitalizedPropName
                setter = 'set' + capitalizedPropName
            }

            String propTypeString = prop.type.isPrimitive() || prop.type.package == String.package
                ? prop.type.simpleName
                : prop.type.name

            printWriter.println """
    public void $setter(${propTypeString} value) {
        getRule().$setter(value);
    }
    
    public ${propTypeString} $getter() {
        return getRule().$getter();
    }
            """
        }

        String emptyListQuickFixImplementation = '''
    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }
        '''

        String customCode = """
            // custom code can be written after this line and it will be preserved during the regeneration

            ${emptyListQuickFixImplementation.trim()}

        }
        """

        InspectionDescriptor descriptor = new InspectionDescriptor(
                implementationClass: "${newClassPackage}.${newClassName}",
                groupPath: [CodeNarcInspectionTool.GROUP_DISPLAY_NAME, group].join(','),
                groupKey: group,
                shortName: CodeNarcInspectionTool.getShortName(ruleInstance),
                displayName: CodeNarcInspectionTool.getDisplayName(ruleInstance),
                level: getLevelFromPriority(ruleInstance.priority),
                enabledByDefault: !DISABLED_BY_DEFAULT_RULES.contains(ruleClassInstance),
                cleanupTool: ruleClassInstance in CLEANUP_AVAILABLE
        )

        if (newSourceFile.exists()) {
            String existingFileText = newSourceFile.text

            descriptor.hasQuickFix = !existingFileText.replaceAll(/\s+/, ' ') //
                    .contains(emptyListQuickFixImplementation.replaceAll(/\s+/, ' ')) && !existingFileText.contains('TODO')

            List<String> pathsToTestClass = [
                    projectRoot,
                    'src',
                    'test',
                    'groovy',
            ]

            pathsToTestClass.addAll(newClassPackage.split('\\.'))

            File testFile = new File(pathsToTestClass.join(File.separator), "${newClassName}Spec.groovy")

            descriptor.hasSpec = testFile.exists()

            if (!existingFileText.contains('@Generated')) {
                // already exits and it highly customised
                // returning fully qualified name to keep the inclusion in plugin.xml
                return descriptor
            }

            String customCodeDelimiter = '// custom code'
            if (existingFileText.contains(customCodeDelimiter)) {
                customCode = existingFileText.substring(existingFileText.lastIndexOf(customCodeDelimiter))
            }
        }

        newSourceFile.text = sw.toString().stripIndent().trim() + '\n\n    ' + customCode.stripIndent().trim() + '\n'

        return descriptor
    }

}
