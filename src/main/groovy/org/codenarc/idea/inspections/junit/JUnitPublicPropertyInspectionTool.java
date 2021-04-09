package org.codenarc.idea.inspections.junit;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.junit.JUnitPublicPropertyRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class JUnitPublicPropertyInspectionTool extends CodeNarcInspectionTool<JUnitPublicPropertyRule> {

    // this code has been generated from org.codenarc.rule.junit.JUnitPublicPropertyRule

    public static final String GROUP = "Junit";

    public JUnitPublicPropertyInspectionTool() {
        super(new JUnitPublicPropertyRule());
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


    public void setIgnorePropertyNames(String value) {
        getRule().setIgnorePropertyNames(value);
    }

    public String getIgnorePropertyNames() {
        return getRule().getIgnorePropertyNames();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}