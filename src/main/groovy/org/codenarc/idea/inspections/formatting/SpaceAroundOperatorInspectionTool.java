package org.codenarc.idea.inspections.formatting;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.formatting.SpaceAroundOperatorRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class SpaceAroundOperatorInspectionTool extends CodeNarcInspectionTool<SpaceAroundOperatorRule> {

    // this code has been generated from org.codenarc.rule.formatting.SpaceAroundOperatorRule

    public static final String GROUP = "Formatting";

    public SpaceAroundOperatorInspectionTool() {
        super(new SpaceAroundOperatorRule());
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


    public void setIgnoreParameterDefaultValueAssignments(boolean value) {
        getRule().setIgnoreParameterDefaultValueAssignments(value);
    }

    public boolean getIgnoreParameterDefaultValueAssignments() {
        return getRule().getIgnoreParameterDefaultValueAssignments();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}