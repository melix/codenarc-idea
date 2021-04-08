package org.codenarc.idea.inspections.unused;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.unused.UnusedPrivateFieldRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnusedPrivateFieldInspectionTool extends CodeNarcInspectionTool<UnusedPrivateFieldRule> {

    // this code has been generated from org.codenarc.rule.unused.UnusedPrivateFieldRule

    public static final String GROUP = "Unused";

    public UnusedPrivateFieldInspectionTool() {
        super(new UnusedPrivateFieldRule());
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


    public void setIgnoreFieldNames(String value) {
        getRule().setIgnoreFieldNames(value);
    }

    public String getIgnoreFieldNames() {
        return getRule().getIgnoreFieldNames();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}