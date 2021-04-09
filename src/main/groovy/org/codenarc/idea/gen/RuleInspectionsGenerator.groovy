package org.codenarc.idea.gen

import com.intellij.codeHighlighting.HighlightDisplayLevel
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.apache.commons.lang3.StringUtils
import org.codenarc.CodeNarc
import org.codenarc.idea.CodeNarcInspectionTool
import org.codenarc.idea.ui.Helpers
import org.codenarc.rule.AbstractRule
import org.codenarc.rule.unnecessary.UnnecessaryReturnKeywordRule
import org.codenarc.rule.unnecessary.UnnecessarySubstringRule
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

        // not used yet
        String groupBundle

    }

    static void main(String[] args) {
        if (args.length != 1) {
            println 'Waiting exactly one argument - path to the project root'
        }

        String projectRoot = args[0]

        println "Generating rule classes for project root: ${projectRoot}"

        generateClasses(projectRoot)
    }

    private static final Set<Class<?>> DISABLED_BY_DEFAULT_RULES = new HashSet<>([
            UnnecessarySubstringRule, // deprecated
            UnnecessaryReturnKeywordRule, // clashes with ImplicitReturnStatementRule
    ])

    private static final String RULESETS_PATH = "rulesets/"

    private static final String[] rulesets = [
            "basic",
            "braces",
            "comments",
            "concurrency",
            "convention",
            "design",
            "dry",
            "exceptions",
            "formatting",
            "generic",
            "grails",
            "groovyism",
            "imports",
            "jdbc",
            "junit",
            "logging",
            "naming",
            "security",
            "serialization",
            "size",
            "unnecessary",
            "unused"
    ]

    private final static Pattern RULE_CLASS_PATTERN = Pattern.compile(".*class='(.*?)'.*")
    private final static Pattern RULE_GROUP_PATTERN = Pattern.compile(".*/([^.]*)\\.xml")

    private final String ruleClass;
    private final String group;

    RuleInspectionsGenerator(String ruleClass, String group) {
        this.ruleClass = ruleClass
        this.group = group
    }

    private static void generateClasses(String projectRoot) {
        updatePluginXml(projectRoot, generateClassFiles(projectRoot))
    }

    private static List<InspectionDescriptor> generateClassFiles(String projectRoot) {
        List<InspectionDescriptor> generatedClasses = []
        String[] rulesetFiles = getRulesetFiles()

        for (String ruleset in rulesetFiles) {
            new BufferedReader(new InputStreamReader(RuleInspectionsGenerator.getResourceAsStream("/" + ruleset))).withCloseable { reader ->
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
                    enabledByDefault: generatedClass.enabledByDefault
            )
            ideaPlugin.extensions[0].append(inspectionNode)
        }

        XmlNodePrinter printer = new XmlNodePrinter(new PrintWriter(new FileWriter(pluginDescriptor)))
        printer.preserveWhitespace = true
        printer.print(ideaPlugin)
    }

    private static String[] getRulesetFiles() {
        try {
            return getResourceListing()
        } catch (URISyntaxException | IOException ignored) {
            return rulesets // fallback
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
        if (dirURL != null && dirURL.getProtocol() == "file") {
            /* A file path: easy enough */
            return new File(dirURL.toURI()).list()
        }

        if (dirURL == null) {
            /*
             * In case of a jar file, we can't actually find a directory.
             * Have to assume the same jar as clazz.
             */
            String me = CodeNarc.class.getName().replace(".", "/") + ".class"
            dirURL = CodeNarc.class.getClassLoader().getResource(me)
        }

        if (dirURL != null) {
            String proto = dirURL.getProtocol()
            if (proto != null && proto == "jar") {
                /* A JAR path */
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"))
                //strip out only the JAR file
                JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))
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

        throw new UnsupportedOperationException("Cannot list files for URL " + dirURL)
    }

    private static String getLevelFromPriority(int priority) {
        switch (priority) {
            case 1: return HighlightDisplayLevel.ERROR.name
            case 2: return HighlightDisplayLevel.WARNING.name
            default: return HighlightDisplayLevel.WEAK_WARNING.name
        }
    }

    /**
     * Generates class Java code and returns the fully qualified name name of the class
     * @return the fully qualified name of the newly generated class or null if the rule cannot be supported
     */
    @Nullable
    @SuppressWarnings('TrailingWhitespace')
    private InspectionDescriptor generateSingleClassFile(String projectRoot) {
        StringWriter sw = new StringWriter()
        PrintWriter printWriter = new PrintWriter(sw);

        Class<?> ruleClassInstance = Helpers.getRuleClassInstance(ruleClass);
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
                'groovy',
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

        if (newSourceFile.exists()) {
            imports.addAll newSourceFile.readLines()
                    .findAll { it.startsWith('import') }
                    .collect { it.substring(7, it.length() - 1) }
        }

        for (String imported in imports.sort()) {
            printWriter.println("        import $imported;")
        }

        printWriter.println """
        @Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
        public class $newClassName extends CodeNarcInspectionTool<$ruleClassInstance.simpleName> {
        
            // this code has been generated from $ruleClass
        
            public static final String GROUP = "$group";
        
            public $newClassName() {
                super(new $ruleClassInstance.simpleName());
            }
            
            @Override
            public String getRuleset() {
                return GROUP;
            }
        """

        for (MetaProperty prop : Helpers.proxyableProps(ruleClassInstance)) {
            String getter;
            String setter;

            if (prop instanceof MetaBeanProperty) {
                MetaBeanProperty beanProp = (MetaBeanProperty) prop;
                getter = beanProp.getter.name;
                setter = beanProp.setter.name;
            } else {
                String capitalizedPropName = StringUtils.capitalize(prop.name);
                getter = 'get' + capitalizedPropName;
                setter = 'set' + capitalizedPropName;
            }

            String propTypeString = prop.type.isPrimitive() || prop.type.package.equals(String.package) ? prop.type.simpleName : prop.type.name

            printWriter.println """
            public void $setter(${propTypeString} value) {
                getRule().$setter(value);
            }
            
            public ${ propTypeString} $getter() {
                return getRule().$getter();
            }
            """
        }

        String customCode = '''
            // custom code can be written after this line and it will be preserved during the regeneration

            @Override
            protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
                return Collections.emptyList();
            }

        }
        '''

        InspectionDescriptor descriptor = new InspectionDescriptor(
                implementationClass: "${newClassPackage}.${newClassName}",
                groupPath: [CodeNarcInspectionTool.GROUP_DISPLAY_NAME, group].join(','),
                groupKey: group,
                shortName: CodeNarcInspectionTool.getShortName(ruleInstance),
                displayName: CodeNarcInspectionTool.getDisplayName(ruleInstance),
                level: getLevelFromPriority(ruleInstance.priority),
                enabledByDefault: !DISABLED_BY_DEFAULT_RULES.contains(ruleClassInstance)
        )

        if (newSourceFile.exists()) {
            String text = newSourceFile.text

            if (!text.contains('@Generated')) {
                // already exits and it highly customised
                // returning fully qualified name to keep the inclusion in plugin.xml
                return descriptor
            }

            String customCodeDelimiter = '// custom code'
            if (text.contains(customCodeDelimiter)) {
                customCode = text.substring(text.lastIndexOf(customCodeDelimiter))
            }
        }

        newSourceFile.text = sw.toString().stripIndent().trim() + '\n\n    ' + customCode.stripIndent().trim()

        return descriptor
    }

}
