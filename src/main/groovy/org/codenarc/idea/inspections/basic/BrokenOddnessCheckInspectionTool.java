package org.codenarc.idea.inspections.basic;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.basic.BrokenOddnessCheckRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class BrokenOddnessCheckInspectionTool extends CodeNarcInspectionTool<BrokenOddnessCheckRule> {

    // this code has been generated from org.codenarc.rule.basic.BrokenOddnessCheckRule

    public static final String GROUP = "Basic";

    public BrokenOddnessCheckInspectionTool() {
        super(new BrokenOddnessCheckRule());
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