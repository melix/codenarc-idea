package org.codenarc.idea.inspections.unnecessary;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.unnecessary.UnnecessaryConstructorRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnnecessaryConstructorInspectionTool extends CodeNarcInspectionTool<UnnecessaryConstructorRule> {

    // this code has been generated from org.codenarc.rule.unnecessary.UnnecessaryConstructorRule

    public static final String GROUP = "Unnecessary";

    public UnnecessaryConstructorInspectionTool() {
        super(new UnnecessaryConstructorRule());
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


    public void setIgnoreAnnotations(boolean value) {
        getRule().setIgnoreAnnotations(value);
    }

    public boolean getIgnoreAnnotations() {
        return getRule().getIgnoreAnnotations();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}