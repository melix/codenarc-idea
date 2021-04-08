/*
 *
 *
 *   Copyright 2011 Cédric Champeau
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  /
 * /
 */

package org.codenarc.idea

import com.intellij.codeInspection.InspectionToolProvider
import org.codenarc.CodeNarc
import org.jetbrains.annotations.NotNull

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.regex.Matcher
import java.util.regex.Pattern
/**
 * Created by IntelliJ IDEA.
 * User: cedric
 * Date: 20/01/11
 * Time: 23:24
 *
 * This component is responsible for loading the rules from the classpath, and "converting" them into IntelliJ IDEA
 * inspections. This is done by dynamically generating subclasses of {@link CodeNarcInspectionTool} class.
 *
 * @author Cédric Champeau
 */
class CodeNarcInspectionToolProvider implements InspectionToolProvider {

    private static final String RULESETS_PATH = "rulesets/"

    private InspectionsClassLoader inspectionsClassLoader
    private Class[] ruleInspectionClasses

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

    CodeNarcInspectionToolProvider() {
        inspectionsClassLoader = new InspectionsClassLoader()
        initializeRuleInspectionClasses()
    }

    @NotNull
    @Override
    Class[] getInspectionClasses() {
        return ruleInspectionClasses
    }

    private void initializeRuleInspectionClasses() {
        List<Class> proxyClasses = new LinkedList<>()
        String[] rulesetFiles

        try {
            rulesetFiles = getResourceListing()
        } catch (URISyntaxException | IOException ignored) {
            rulesetFiles = rulesets // fallback
        }

        for (String ruleset in rulesetFiles) {
            new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + ruleset))).withCloseable { reader ->
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
                            Class clazz = inspectionsClassLoader.defineClass(new ImplementGetRuleClassGenerator(m.group(1), groupName).toByteArray())
                            // initialize rule
                            proxyClasses.add(clazz)
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

        ruleInspectionClasses = proxyClasses.toArray(new Class[0])
    }

    /**
     * List directory contents for a resource folder. Not recursive.
     * This is basically a brute-force implementation.
     * Works for regular files and also JARs.
     *
     * @author Greg Briggs
     * @return Just the name of each member item, not the full paths.
     * @throws URISyntaxException
     * @throws IOException
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
            String me = CodeNarc.class.getName().replace(".", "/")+".class"
            dirURL = CodeNarc.class.getClassLoader().getResource(me)
        }

        if (dirURL != null) {
            String proto = dirURL.getProtocol()
            if (proto != null && proto == "jar") {
                /* A JAR path */
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")) //strip out only the JAR file
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

        throw new UnsupportedOperationException("Cannot list files for URL "+dirURL)
    }

}
