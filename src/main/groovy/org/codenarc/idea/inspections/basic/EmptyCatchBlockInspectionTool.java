package org.codenarc.idea.inspections.basic;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.basic.EmptyCatchBlockRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class EmptyCatchBlockInspectionTool extends CodeNarcInspectionTool<EmptyCatchBlockRule> {

    // this code has been generated from org.codenarc.rule.basic.EmptyCatchBlockRule

    public static final String GROUP = "Basic";

    public EmptyCatchBlockInspectionTool() {
        super(new EmptyCatchBlockRule());
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


    public void setIgnoreRegex(String value) {
        getRule().setIgnoreRegex(value);
    }

    public String getIgnoreRegex() {
        return getRule().getIgnoreRegex();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}