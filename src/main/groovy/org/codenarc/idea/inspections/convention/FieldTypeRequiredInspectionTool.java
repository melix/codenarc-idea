package org.codenarc.idea.inspections.convention;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.convention.FieldTypeRequiredRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class FieldTypeRequiredInspectionTool extends CodeNarcInspectionTool<FieldTypeRequiredRule> {

    // this code has been generated from org.codenarc.rule.convention.FieldTypeRequiredRule

    public static final String GROUP = "Convention";

    public FieldTypeRequiredInspectionTool() {
        super(new FieldTypeRequiredRule());
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