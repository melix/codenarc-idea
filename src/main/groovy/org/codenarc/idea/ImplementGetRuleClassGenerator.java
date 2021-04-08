package org.codenarc.idea;

import groovy.lang.MetaBeanProperty;
import groovy.lang.MetaProperty;
import org.apache.commons.lang3.StringUtils;
import org.codenarc.idea.ui.Helpers;
import org.jetbrains.org.objectweb.asm.ClassWriter;
import org.jetbrains.org.objectweb.asm.MethodVisitor;
import org.jetbrains.org.objectweb.asm.Opcodes;
import org.jetbrains.org.objectweb.asm.Type;
import org.jetbrains.org.objectweb.asm.util.CheckClassAdapter;
import org.jetbrains.org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

public class ImplementGetRuleClassGenerator implements Opcodes {
    public ImplementGetRuleClassGenerator(final String ruleClass, String ruleSet) {
        this.ruleClass = ruleClass;
        this.ruleSet = ruleSet;
        implementedClassInternalName = "org/codenarc/idea/CodeNarcInspectionTool$" + ruleClass.replaceAll("\\.", "_");
    }

    public byte[] toByteArray() {
        PrintWriter printWriter = new PrintWriter(System.out);
        ClassWriter cv = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        TraceClassVisitor tcv = new TraceClassVisitor(cv, printWriter);
        CheckClassAdapter cw = new CheckClassAdapter(tcv, false);
//            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES)
        MethodVisitor mv;

        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, implementedClassInternalName, null, codeNarcInspectionToolsTypeInternalName, null);

        mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, codeNarcInspectionToolsTypeInternalName, "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        mv = cw.visitMethod(ACC_PROTECTED, "getRuleClass", stringTypeDescriptor, null, null);
        mv.visitCode();
        mv.visitLdcInsn(ruleClass);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        mv = cw.visitMethod(ACC_PROTECTED, "getRuleset", stringTypeDescriptor, null, null);
        mv.visitCode();
        mv.visitLdcInsn(ruleSet);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        try {
            Class<?> ruleClassInstance = Helpers.getRuleClassInstance(ruleClass);
            String ruleClassInternalName = Type.getInternalName(ruleClassInstance);
            String ruleClassDescriptor = Type.getDescriptor(ruleClassInstance);

            for (MetaProperty prop : Helpers.proxyableProps(ruleClassInstance)) {
                String getter;
                String setter;

                if (prop instanceof MetaBeanProperty) {
                    MetaBeanProperty beanProp = (MetaBeanProperty) prop;
                    getter = beanProp.getGetter().getName();
                    setter = beanProp.getSetter().getName();
                } else {
                    String capitalizedPropName = StringUtils.capitalize(prop.getName());
                    getter = "get" + capitalizedPropName;
                    setter = "set" + capitalizedPropName;
                }


                Class<?> propType = prop.getType();
                String propDescriptor = Type.getDescriptor(propType);

                mv = cw.visitMethod(ACC_PUBLIC, setter, "(" + propDescriptor + ")V", null, null);
                mv.visitCode();
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, implementedClassInternalName, "rule", ruleClassDescriptor);
                prepareRegister(mv, propType);
                mv.visitMethodInsn(INVOKEVIRTUAL, ruleClassInternalName, setter, "(" + propDescriptor + ")V", false);
                mv.visitInsn(RETURN);
                mv.visitMaxs(0, 0);
                mv.visitEnd();

                mv = cw.visitMethod(ACC_PUBLIC, getter, "()" + propDescriptor, null, null);
                mv.visitCode();
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, implementedClassInternalName, "rule", ruleClassDescriptor);
                mv.visitMethodInsn(INVOKEVIRTUAL, ruleClassInternalName, getter, "()" + propDescriptor, false);
                typedReturn(mv, propType);
                mv.visitMaxs(0, 0);
                mv.visitEnd();
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


        cw.visitEnd();

//            return cw.toByteArray()
        return cv.toByteArray();
    }

    private static void prepareRegister(MethodVisitor mv, Class<?> propType) {
        if (propType.equals(Boolean.TYPE) || propType.equals(Integer.TYPE) || propType.equals(Byte.TYPE) || propType.equals(Character.TYPE) || propType.equals(Short.TYPE)) {
            mv.visitVarInsn(ILOAD, 1);
            return;

        } else if (propType.equals(Long.TYPE)) {
            mv.visitVarInsn(LLOAD, 1);
            return;

        } else if (propType.equals(Float.TYPE)) {
            mv.visitVarInsn(FLOAD, 1);
            return;

        } else if (propType.equals(Double.TYPE)) {
            mv.visitVarInsn(DLOAD, 1);
            return;

        }

        mv.visitVarInsn(ALOAD, 1);
    }

    private static void typedReturn(MethodVisitor mv, Class propType) {
        if (propType.equals(Boolean.TYPE) || propType.equals(Integer.TYPE) || propType.equals(Byte.TYPE) || propType.equals(Character.TYPE) || propType.equals(Short.TYPE)) {
            mv.visitInsn(IRETURN);
            return;

        } else if (propType.equals(Long.TYPE)) {
            mv.visitInsn(LRETURN);
            return;

        } else if (propType.equals(Float.TYPE)) {
            mv.visitInsn(FRETURN);
            return;

        } else if (propType.equals(Double.TYPE)) {
            mv.visitInsn(DRETURN);
            return;

        }

        mv.visitInsn(ARETURN);
    }

    private final String ruleClass;
    private final String ruleSet;
    private static final String codeNarcInspectionToolsTypeInternalName = Type.getInternalName(CodeNarcInspectionTool.class);
    private final String implementedClassInternalName;
    private static final String stringTypeDescriptor = "()" + Type.getDescriptor(String.class);

}
