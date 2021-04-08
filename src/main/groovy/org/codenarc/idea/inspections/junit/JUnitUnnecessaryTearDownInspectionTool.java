package org.codenarc.idea.inspections.junit;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.junit.JUnitUnnecessaryTearDownRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class JUnitUnnecessaryTearDownInspectionTool extends CodeNarcInspectionTool<JUnitUnnecessaryTearDownRule> {

    // this code has been generated from org.codenarc.rule.junit.JUnitUnnecessaryTearDownRule

    public static final String GROUP = "Junit";

    public JUnitUnnecessaryTearDownInspectionTool() {
        super(new JUnitUnnecessaryTearDownRule());
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