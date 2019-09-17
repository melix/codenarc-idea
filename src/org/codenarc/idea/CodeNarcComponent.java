/*
 *
 *
 *   Copyright 2011 C�dric Champeau
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

package org.codenarc.idea;

import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.openapi.components.ApplicationComponent;
import org.codenarc.CodeNarc;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by IntelliJ IDEA.
 * User: cedric
 * Date: 20/01/11
 * Time: 23:24
 */

/**
 * This component is responsible for loading the rules from the classpath, and "converting" them into IntelliJ IDEA
 * inspections. This is done by dynamically generating subclasses of {@link CodeNarcInspectionTool} class.
 *
 * @author Cédric Champeau
 */
public class CodeNarcComponent implements ApplicationComponent, InspectionToolProvider {

    protected static final String BASE_MESSAGES_BUNDLE = "codenarc-base-messages";

    private InspectionsClassLoader inspectionsClassLoader;
    private Class[] ruleInspectionClasses;

    private final static String[] rulesets = {
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
    };
    private final static Pattern RULE_CLASS_PATTERN = Pattern.compile(".*class='(.*?)'.*");

    public void initComponent() {
        inspectionsClassLoader = new InspectionsClassLoader();
        initializeRuleInspectionClasses();
    }

    public void disposeComponent() {
        ruleInspectionClasses = null;
        inspectionsClassLoader = null;
    }

    @NotNull
    public String getComponentName() {
        return "CodeNarcComponent";
    }

    public Class[] getInspectionClasses() {
        return ruleInspectionClasses;
    }

    private void initializeRuleInspectionClasses() {
        List<Class> proxyclasses = new LinkedList<Class>();
        String[] rulesetfiles = null;

        try {
            rulesetfiles = getResourceListing(CodeNarc.class, "rulesets/");
        } catch (URISyntaxException | IOException e) {
            rulesetfiles = rulesets; // fallback
        }

        for (String ruleset : rulesetfiles) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + ruleset)));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    Matcher m = RULE_CLASS_PATTERN.matcher(line);
                    if (m.find()) {
                        Class clazz = inspectionsClassLoader.defineClass(new ImplementGetRuleClassGenerator(m.group(1), ruleset).toByteArray());
                        // initialize rule
                        proxyclasses.add(clazz);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    // silent
                }
            }
        }

        ruleInspectionClasses = proxyclasses.toArray(new Class[proxyclasses.size()]);
    }

    /**
     * A classloader which allows us to load the byte[] generated by ASM.
     */
    static final class InspectionsClassLoader extends URLClassLoader {

        public InspectionsClassLoader() {
            super(new URL[0], CodeNarcComponent.class.getClassLoader());
        }

        protected Class defineClass(byte[] data) {
            return super.defineClass(null, data, 0, data.length);
        }
    }

    /**
     * List directory contents for a resource folder. Not recursive.
     * This is basically a brute-force implementation.
     * Works for regular files and also JARs.
     *
     * @author Greg Briggs
     * @param clazz Any java class that lives in the same place as the resources you want.
     * @param path Should end with "/", but not start with one.
     * @return Just the name of each member item, not the full paths.
     * @throws URISyntaxException
     * @throws IOException
     */
    String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
        URL dirURL = clazz.getClassLoader().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            /* A file path: easy enough */
            return new File(dirURL.toURI()).list();
        }

        if (dirURL == null) {
            /*
             * In case of a jar file, we can't actually find a directory.
             * Have to assume the same jar as clazz.
             */
            String me = clazz.getName().replace(".", "/")+".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }

        if (dirURL.getProtocol().equals("jar")) {
            /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
            while(entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(path)) { //filter according to the path
                    if (name.substring(path.length()).isEmpty()) {
                        continue;
                    }

                    result.add(name);
                }
            }
            return result.toArray(new String[result.size()]);
        }

        throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
    }

    /**
     * A class generator which creates an implementation class for {@link CodeNarcInspectionTool}
     */
    private static class ImplementGetRuleClassGenerator implements Opcodes {
        private final String ruleClass;
        private final String ruleSet;
        private final static String codeNarcInspectionToolsTypeInternalName = Type.getInternalName(CodeNarcInspectionTool.class);
        private final static String stringTypeDescriptor = Type.getDescriptor(String.class);

        public ImplementGetRuleClassGenerator(String ruleClass, String ruleSet) {
            this.ruleClass = ruleClass;
            this.ruleSet = ruleSet;
        }

        public byte[] toByteArray() {
            ClassWriter cw = new ClassWriter(0);
            MethodVisitor mv;

            cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "org/codenarc/idea/CodeNarcInpectionTool$" + ruleClass.replaceAll("\\.","_"), null, codeNarcInspectionToolsTypeInternalName, null);

            {
                mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
                mv.visitCode();
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKESPECIAL, codeNarcInspectionToolsTypeInternalName, "<init>", "()V", false);
                mv.visitInsn(RETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            {
                mv = cw.visitMethod(ACC_PROTECTED, "getRuleClass", stringTypeDescriptor, null, null);
                mv.visitCode();
                mv.visitLdcInsn(ruleClass);
                mv.visitInsn(Opcodes.ARETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            {
                mv = cw.visitMethod(ACC_PROTECTED, "getRuleset", stringTypeDescriptor, null, null);
                mv.visitCode();
                mv.visitLdcInsn(ruleSet);
                mv.visitInsn(Opcodes.ARETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            cw.visitEnd();

            return cw.toByteArray();
        }

    }
}
