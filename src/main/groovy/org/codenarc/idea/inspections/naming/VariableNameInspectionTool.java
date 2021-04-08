package org.codenarc.idea.inspections.naming;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.naming.VariableNameRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class VariableNameInspectionTool extends CodeNarcInspectionTool<VariableNameRule> {

    // this code has been generated from org.codenarc.rule.naming.VariableNameRule

    public static final String GROUP = "Naming";

    public VariableNameInspectionTool() {
        super(new VariableNameRule());
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


    public void setFinalRegex(String value) {
        getRule().setFinalRegex(value);
    }

    public String getFinalRegex() {
        return getRule().getFinalRegex();
    }


    public void setIgnoreVariableNames(String value) {
        getRule().setIgnoreVariableNames(value);
    }

    public String getIgnoreVariableNames() {
        return getRule().getIgnoreVariableNames();
    }


    public void setRegex(String value) {
        getRule().setRegex(value);
    }

    public String getRegex() {
        return getRule().getRegex();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}