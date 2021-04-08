package org.codenarc.idea.inspections.junit;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.junit.UseAssertSameInsteadOfAssertTrueRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UseAssertSameInsteadOfAssertTrueInspectionTool extends CodeNarcInspectionTool<UseAssertSameInsteadOfAssertTrueRule> {

    // this code has been generated from org.codenarc.rule.junit.UseAssertSameInsteadOfAssertTrueRule

    public static final String GROUP = "Junit";

    public UseAssertSameInsteadOfAssertTrueInspectionTool() {
        super(new UseAssertSameInsteadOfAssertTrueRule());
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

}