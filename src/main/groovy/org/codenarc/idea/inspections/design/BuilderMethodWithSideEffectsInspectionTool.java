package org.codenarc.idea.inspections.design;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.design.BuilderMethodWithSideEffectsRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class BuilderMethodWithSideEffectsInspectionTool extends CodeNarcInspectionTool<BuilderMethodWithSideEffectsRule> {

    // this code has been generated from org.codenarc.rule.design.BuilderMethodWithSideEffectsRule

    public static final String GROUP = "Design";

    public BuilderMethodWithSideEffectsInspectionTool() {
        super(new BuilderMethodWithSideEffectsRule());
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


    public void setMethodNameRegex(String value) {
        getRule().setMethodNameRegex(value);
    }

    public String getMethodNameRegex() {
        return getRule().getMethodNameRegex();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}