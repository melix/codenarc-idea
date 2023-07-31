package org.codenarc.idea.inspections.design;

import com.intellij.codeInsight.daemon.impl.quickfix.AddDefaultConstructorFix;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiModifier;
import com.intellij.util.containers.JBIterable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.design.AbstractClassWithoutAbstractMethodRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.codeInspection.bugs.GrModifierFix;
import org.jetbrains.plugins.groovy.codeInspection.bugs.GrRemoveModifierFix;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class AbstractClassWithoutAbstractMethodInspectionTool extends CodeNarcInspectionTool<AbstractClassWithoutAbstractMethodRule> {

    // this code has been generated from org.codenarc.rule.design.AbstractClassWithoutAbstractMethodRule

    public static final String GROUP = "Design";

    public AbstractClassWithoutAbstractMethodInspectionTool() {
        super(new AbstractClassWithoutAbstractMethodRule());
        applyDefaultConfiguration(getRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setApplyToClassNames(String value) {
        getRule().setApplyToClassNames(value);
    }

    public String getApplyToClassNames() {
        return getRule().getApplyToClassNames();
    }


    public void setDoNotApplyToClassNames(String value) {
        getRule().setDoNotApplyToClassNames(value);
    }

    public String getDoNotApplyToClassNames() {
        return getRule().getDoNotApplyToClassNames();
    }


    public void setEnhancedMode(boolean value) {
        getRule().setEnhancedMode(value);
    }

    public boolean isEnhancedMode() {
        return getRule().isEnhancedMode();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        PsiClass theClass = JBIterable.generate(violatingElement, PsiElement::getParent).takeWhile(e -> !(e instanceof PsiFile)).filter(PsiClass.class).first();
        if (theClass == null) {
            return Collections.singletonList(new GrRemoveModifierFix(PsiModifier.ABSTRACT));
        }
        return Arrays.<LocalQuickFix>asList(
                new GrModifierFix(theClass, PsiModifier.ABSTRACT, true, false, GrModifierFix.MODIFIER_LIST_CHILD),
                new AddDefaultConstructorFix(theClass, PsiModifier.PROTECTED)
        );
    }

}
