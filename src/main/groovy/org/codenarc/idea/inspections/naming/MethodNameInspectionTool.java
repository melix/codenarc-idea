package org.codenarc.idea.inspections.naming;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.naming.MethodNameRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class MethodNameInspectionTool extends CodeNarcInspectionTool<MethodNameRule> {

    // this code has been generated from org.codenarc.rule.naming.MethodNameRule

    public static final String GROUP = "Naming";

    public MethodNameInspectionTool() {
        super(new MethodNameRule());
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


    public void setIgnoreMethodNames(String value) {
        getRule().setIgnoreMethodNames(value);
    }

    public String getIgnoreMethodNames() {
        return getRule().getIgnoreMethodNames();
    }


    public void setRegex(String value) {
        getRule().setRegex(value);
    }

    public String getRegex() {
        return getRule().getRegex();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}