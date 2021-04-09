package org.codenarc.idea.inspections.formatting;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.formatting.SpaceAfterClosingBraceRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class SpaceAfterClosingBraceInspectionTool extends CodeNarcInspectionTool<SpaceAfterClosingBraceRule> {

    // this code has been generated from org.codenarc.rule.formatting.SpaceAfterClosingBraceRule

    public static final String GROUP = "Formatting";

    public SpaceAfterClosingBraceInspectionTool() {
        super(new SpaceAfterClosingBraceRule());
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


    public void setCheckClosureMapEntryValue(boolean value) {
        getRule().setCheckClosureMapEntryValue(value);
    }

    public boolean getCheckClosureMapEntryValue() {
        return getRule().getCheckClosureMapEntryValue();
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