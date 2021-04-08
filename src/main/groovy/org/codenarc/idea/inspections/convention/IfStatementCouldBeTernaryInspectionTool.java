package org.codenarc.idea.inspections.convention;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.convention.IfStatementCouldBeTernaryRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class IfStatementCouldBeTernaryInspectionTool extends CodeNarcInspectionTool<IfStatementCouldBeTernaryRule> {

    // this code has been generated from org.codenarc.rule.convention.IfStatementCouldBeTernaryRule

    public static final String GROUP = "Convention";

    public IfStatementCouldBeTernaryInspectionTool() {
        super(new IfStatementCouldBeTernaryRule());
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


    public void setCheckLastStatementImplicitElse(boolean value) {
        getRule().setCheckLastStatementImplicitElse(value);
    }

    public boolean getCheckLastStatementImplicitElse() {
        return getRule().getCheckLastStatementImplicitElse();
    }


    public void setDoNotApplyToClassNames(String value) {
        getRule().setDoNotApplyToClassNames(value);
    }

    public String getDoNotApplyToClassNames() {
        return getRule().getDoNotApplyToClassNames();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}