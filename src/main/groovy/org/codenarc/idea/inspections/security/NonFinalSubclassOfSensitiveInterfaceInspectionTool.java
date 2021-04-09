package org.codenarc.idea.inspections.security;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.security.NonFinalSubclassOfSensitiveInterfaceRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class NonFinalSubclassOfSensitiveInterfaceInspectionTool extends CodeNarcInspectionTool<NonFinalSubclassOfSensitiveInterfaceRule> {

    // this code has been generated from org.codenarc.rule.security.NonFinalSubclassOfSensitiveInterfaceRule

    public static final String GROUP = "Security";

    public NonFinalSubclassOfSensitiveInterfaceInspectionTool() {
        super(new NonFinalSubclassOfSensitiveInterfaceRule());
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

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}