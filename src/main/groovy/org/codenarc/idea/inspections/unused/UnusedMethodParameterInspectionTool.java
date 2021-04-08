package org.codenarc.idea.inspections.unused;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.unused.UnusedMethodParameterRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnusedMethodParameterInspectionTool extends CodeNarcInspectionTool<UnusedMethodParameterRule> {

    // this code has been generated from org.codenarc.rule.unused.UnusedMethodParameterRule

    public static final String GROUP = "Unused";

    public UnusedMethodParameterInspectionTool() {
        super(new UnusedMethodParameterRule());
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


    public void setIgnoreClassRegex(String value) {
        getRule().setIgnoreClassRegex(value);
    }

    public String getIgnoreClassRegex() {
        return getRule().getIgnoreClassRegex();
    }


    public void setIgnoreRegex(String value) {
        getRule().setIgnoreRegex(value);
    }

    public String getIgnoreRegex() {
        return getRule().getIgnoreRegex();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}