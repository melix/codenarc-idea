package org.codenarc.idea.inspections.size;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.size.ParameterCountRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class ParameterCountInspectionTool extends CodeNarcInspectionTool<ParameterCountRule> {

    // this code has been generated from org.codenarc.rule.size.ParameterCountRule

    public static final String GROUP = "Size";

    public ParameterCountInspectionTool() {
        super(new ParameterCountRule());
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


    public void setIgnoreOverriddenMethods(boolean value) {
        getRule().setIgnoreOverriddenMethods(value);
    }

    public boolean isIgnoreOverriddenMethods() {
        return getRule().isIgnoreOverriddenMethods();
    }


    public void setMaxParameters(int value) {
        getRule().setMaxParameters(value);
    }

    public int getMaxParameters() {
        return getRule().getMaxParameters();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
