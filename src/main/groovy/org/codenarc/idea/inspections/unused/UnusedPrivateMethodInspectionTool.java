package org.codenarc.idea.inspections.unused;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.unused.UnusedPrivateMethodRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnusedPrivateMethodInspectionTool extends CodeNarcInspectionTool<UnusedPrivateMethodRule> {

    // this code has been generated from org.codenarc.rule.unused.UnusedPrivateMethodRule

    public static final String GROUP = "Unused";

    public UnusedPrivateMethodInspectionTool() {
        super(new UnusedPrivateMethodRule());
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


    public void setIgnoreMethodsWithAnnotationNames(String value) {
        getRule().setIgnoreMethodsWithAnnotationNames(value);
    }

    public String getIgnoreMethodsWithAnnotationNames() {
        return getRule().getIgnoreMethodsWithAnnotationNames();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}