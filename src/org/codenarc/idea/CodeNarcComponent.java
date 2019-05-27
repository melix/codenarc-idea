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
import com.intellij.openapi.components.BaseComponent;
import groovy.lang.MetaBeanProperty;
import groovy.lang.MetaProperty;
import org.apache.commons.lang3.StringUtils;
import org.codenarc.CodeNarc;
import org.apache.log4j.Logger;
import org.codenarc.idea.ui.Helpers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.org.objectweb.asm.ClassWriter;
import org.jetbrains.org.objectweb.asm.util.CheckClassAdapter;
import org.jetbrains.org.objectweb.asm.util.TraceClassVisitor;
import org.jetbrains.org.objectweb.asm.MethodVisitor;
import org.jetbrains.org.objectweb.asm.Opcodes;
import org.jetbrains.org.objectweb.asm.Type;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
 *
 * This component is responsible for loading the rules from the classpath, and "converting" them into IntelliJ IDEA
 * inspections. This is done by dynamically generating subclasses of {@link CodeNarcInspectionTool} class.
 *
 * @author Cédric Champeau
 */
public class CodeNarcComponent implements BaseComponent, InspectionToolProvider {

    static final String BASE_MESSAGES_BUNDLE = "codenarc-base-messages";
    private static final String RULESETS_PATH = "rulesets/";

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
    private final static Pattern RULE_GROUP_PATTERN = Pattern.compile(".*/([^.]*)\\.xml");

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

    @NotNull
    @Override
    public Class[] getInspectionClasses() {
        return ruleInspectionClasses;
    }

    private void initializeRuleInspectionClasses() {
        List<Class> proxyclasses = new LinkedList<>();
        String[] rulesetfiles;

        try {
            rulesetfiles = getResourceListing();
        } catch (URISyntaxException | IOException e) {
            rulesetfiles = rulesets; // fallback
        }

        for (String ruleset : rulesetfiles) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + ruleset)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher m = RULE_CLASS_PATTERN.matcher(line);
                    if (m.find()) {
                        String groupName = ruleset;
                        Matcher n = RULE_GROUP_PATTERN.matcher(ruleset);
                        if (n.find())
                        {
                            groupName = Character.toUpperCase(n.group(1).charAt(0)) + n.group(1).substring(1);
                        }
                        Class clazz = inspectionsClassLoader.defineClass(new ImplementGetRuleClassGenerator(m.group(1), groupName).toByteArray());
                        // initialize rule
                        proxyclasses.add(clazz);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassFormatError e) {
                e.printStackTrace();
                throw (e);
            }
            // silent
        }

        ruleInspectionClasses = proxyclasses.toArray(new Class[0]);
    }

    /**
     * A classloader which allows us to load the byte[] generated by ASM.
     */
    static final class InspectionsClassLoader extends URLClassLoader {

        InspectionsClassLoader() {
            super(new URL[0], CodeNarcComponent.class.getClassLoader());
        }

        Class defineClass(byte[] data) {
            return super.defineClass(null, data, 0, data.length);
        }
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
    private String[] getResourceListing() throws URISyntaxException, IOException {
        URL dirURL = CodeNarc.class.getClassLoader().getResource(RULESETS_PATH);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            /* A file path: easy enough */
            return new File(dirURL.toURI()).list();
        }

        if (dirURL == null) {
            /*
             * In case of a jar file, we can't actually find a directory.
             * Have to assume the same jar as clazz.
             */
            String me = CodeNarc.class.getName().replace(".", "/")+".class";
            dirURL = CodeNarc.class.getClassLoader().getResource(me);
        }

        if (dirURL != null) {
            String proto = dirURL.getProtocol();
            if (proto != null && proto.equals("jar")) {
                /* A JAR path */
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
                JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                Set<String> result = new HashSet<>(); //avoid duplicates in case it is a subdirectory
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(RULESETS_PATH)) { //filter according to the path
                        if (name.substring(9).isEmpty()) {
                            continue;
                        }

                        result.add(name);
                    }
                }
                return result.toArray(new String[0]);
            }
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
        private final String implementedClassInternalName;
        private final static String stringTypeDescriptor = "()" + Type.getDescriptor(String.class);

        ImplementGetRuleClassGenerator(String ruleClass, String ruleSet) {
            this.ruleClass = ruleClass;
            this.ruleSet = ruleSet;
            implementedClassInternalName = "org/codenarc/idea/CodeNarcInspectionTool$" + ruleClass.replaceAll("\\.","_");
        }

        byte[] toByteArray() {
//            PrintWriter printWriter = new PrintWriter(System.out);
//            ClassWriter cv = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//            TraceClassVisitor tcv = new TraceClassVisitor(cv, printWriter);
//            CheckClassAdapter cw = new CheckClassAdapter(tcv, false);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            MethodVisitor mv;

            cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, implementedClassInternalName, null, codeNarcInspectionToolsTypeInternalName, null);

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
                mv.visitInsn(ARETURN);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
            {
                mv = cw.visitMethod(ACC_PROTECTED, "getRuleset", stringTypeDescriptor, null, null);
                mv.visitCode();
                mv.visitLdcInsn(ruleSet);
                mv.visitInsn(ARETURN);
                mv.visitMaxs(0, 0);
                mv.visitEnd();
            }

            try {
                Class<?> ruleClassInstance = Helpers.getRuleClassInstance(ruleClass);
                String ruleClassInternalName = Type.getInternalName(ruleClassInstance);
                String ruleClassDescriptor = Type.getDescriptor(ruleClassInstance);

                for (MetaProperty prop : Helpers.proxyableProps(ruleClassInstance)) {
                    String getter;
                    String setter;

                    if (prop instanceof MetaBeanProperty) {
                        MetaBeanProperty beanProp = (MetaBeanProperty)prop;
                        getter = beanProp.getGetter().getName();
                        setter = beanProp.getSetter().getName();
                    }
                    else {
                        String capitalizedPropName = StringUtils.capitalize(prop.getName());
                        getter = "get" + capitalizedPropName;
                        setter = "set" + capitalizedPropName;
                    }

                    Class propType = prop.getType();
                    String propDescriptor = Type.getDescriptor(propType);

                    {
                        mv = cw.visitMethod(ACC_PUBLIC, setter, "(" + propDescriptor + ")V", null, null);
                        mv.visitCode();
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitFieldInsn(GETFIELD, implementedClassInternalName, "rule", ruleClassDescriptor);
                        prepareRegister(mv, propType);
                        mv.visitMethodInsn(INVOKEVIRTUAL, ruleClassInternalName, setter, "(" + propDescriptor + ")V", false);
                        mv.visitInsn(RETURN);
                        mv.visitMaxs(0, 0);
                        mv.visitEnd();
                    }
                    {
                        mv = cw.visitMethod(ACC_PUBLIC, getter, "()" + propDescriptor, null, null);
                        mv.visitCode();
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitFieldInsn(GETFIELD, implementedClassInternalName, "rule", ruleClassDescriptor);
                        mv.visitMethodInsn(INVOKEVIRTUAL, ruleClassInternalName, getter, "()" + propDescriptor, false);
                        typedReturn(mv, propType);
                        mv.visitMaxs(0, 0);
                        mv.visitEnd();
                    }
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            cw.visitEnd();

            return cw.toByteArray();
//            return cv.toByteArray();
        }

        private void prepareRegister(MethodVisitor mv, Class propType) {
            if (propType == Boolean.TYPE || propType == Integer.TYPE || propType == Byte.TYPE || propType == Character.TYPE || propType == Short.TYPE) {
                mv.visitVarInsn(ILOAD, 1);
                return;
            }
            else if (propType == Long.TYPE) {
                mv.visitVarInsn(LLOAD, 1);
                return;
            }
            else if (propType == Float.TYPE) {
                mv.visitVarInsn(FLOAD, 1);
                return;
            }
            else if (propType == Double.TYPE) {
                mv.visitVarInsn(DLOAD, 1);
                return;
            }
            mv.visitVarInsn(ALOAD, 1);
        }

        private void typedReturn(MethodVisitor mv, Class propType) {
            if (propType == Boolean.TYPE || propType == Integer.TYPE || propType == Byte.TYPE || propType == Character.TYPE || propType == Short.TYPE) {
                mv.visitInsn(IRETURN);
                return;
            }
            else if (propType == Long.TYPE) {
                mv.visitInsn(LRETURN);
                return;
            }
            else if (propType == Float.TYPE) {
                mv.visitInsn(FRETURN);
                return;
            }
            else if (propType == Double.TYPE) {
                mv.visitInsn(DRETURN);
                return;
            }
            mv.visitInsn(ARETURN);
        }
    }

}
