package org.codenarc.idea.inspections.junit;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.junit.SpockIgnoreRestUsedRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class SpockIgnoreRestUsedInspectionTool extends CodeNarcInspectionTool<SpockIgnoreRestUsedRule> {

    // this code has been generated from org.codenarc.rule.junit.SpockIgnoreRestUsedRule

    public static final String GROUP = "Junit";

    public SpockIgnoreRestUsedInspectionTool() {
        super(new SpockIgnoreRestUsedRule());
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


    public void setSpecificationClassNames(String value) {
        getRule().setSpecificationClassNames(value);
    }

    public String getSpecificationClassNames() {
        return getRule().getSpecificationClassNames();
    }


    public void setSpecificationSuperclassNames(String value) {
        getRule().setSpecificationSuperclassNames(value);
    }

    public String getSpecificationSuperclassNames() {
        return getRule().getSpecificationSuperclassNames();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}