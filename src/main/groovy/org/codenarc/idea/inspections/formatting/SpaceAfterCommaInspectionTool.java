package org.codenarc.idea.inspections.formatting;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.formatting.SpaceAfterCommaRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class SpaceAfterCommaInspectionTool extends CodeNarcInspectionTool<SpaceAfterCommaRule> {

    // this code has been generated from org.codenarc.rule.formatting.SpaceAfterCommaRule

    public static final String GROUP = "Formatting";

    public SpaceAfterCommaInspectionTool() {
        super(new SpaceAfterCommaRule());
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