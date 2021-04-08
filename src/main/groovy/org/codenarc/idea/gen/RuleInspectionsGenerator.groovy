package org.codenarc.idea.gen

import groovy.transform.CompileStatic
import org.apache.commons.lang3.StringUtils
import org.codenarc.CodeNarc
import org.codenarc.idea.ui.Helpers
import org.codenarc.rule.AbstractRule
import org.jetbrains.annotations.Nullable

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.regex.Matcher
import java.util.regex.Pattern

@CompileStatic
class RuleInspectionsGenerator {

    static void main(String[] args) {
        if (args.length != 1) {
            println 'Waiting exactly one argument - path to the project root'
        }

        String projectRoot = args[0]

        println "Generating rule classes for project root: ${projectRoot}"

        generateClasses(projectRoot)
    }

    private static final String RULESETS_PATH = "rulesets/"

    private final static String[] rulesets = [
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
                            String generated = new RuleInspectionsGenerator(m.group(1), groupName).generateClass(projectRoot)
                            if (generated) {
                                println("""<localInspection implementationClass="$generated"/>""")
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace()
                }
                catch (ClassFormatError e) {
                    e.printStackTrace()
                    throw (e)
                }
                // silent
            }
        }
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

    /**
     * Generates class Java code and returns the fully qualified name name of the class
     * @return the fully qualified name of the newly generated class or null if the rule cannot be supported
     */
    @Nullable
    @SuppressWarnings('TrailingWhitespace')
    private String generateClass(String projectRoot) {
        StringWriter sw = new StringWriter()
        PrintWriter printWriter = new PrintWriter(sw);

        Class<?> ruleClassInstance = Helpers.getRuleClassInstance(ruleClass);

        AbstractRule ruleInstance = ruleClassInstance.newInstance() as AbstractRule

        if (ruleInstance.compilerPhase > 3) {
            return null
        }

        String newClassPackage = "org.codenarc.idea.inspections.${group.toLowerCase()}"
        String newClassName = "${ruleClassInstance.simpleName}InspectionTool"

        printWriter.println """
        package $newClassPackage;
        
        import javax.annotation.Generated;

        import org.codenarc.idea.CodeNarcInspectionTool;
        import $ruleClass;
        
        @Generated("Generated Inspection - remove this line if you have made custom changes to prevent overiding this class")
        public class $newClassName extends CodeNarcInspectionTool<$ruleClassInstance.simpleName> {
        
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

            printWriter.println """
            public void $setter(${prop.type.simpleName} value) {
                getRule().$setter(value);
            }
            
            public ${prop.type.simpleName} $getter() {
                return getRule().$getter();
            }
            """
        }

        printWriter.println '''
        }
        '''



        System.out.println(sw.toString().stripIndent().trim())

        return "${newClassPackage}.${newClassName}"
    }

}
