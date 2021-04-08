package org.codenarc.idea.inspections.generic;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.generic.RequiredStringRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class RequiredStringInspectionTool extends CodeNarcInspectionTool<RequiredStringRule> {

    // this code has been generated from org.codenarc.rule.generic.RequiredStringRule

    public static final String GROUP = "Generic";

    public RequiredStringInspectionTool() {
        super(new RequiredStringRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setString(String value) {
        getRule().setString(value);
    }

    public String getString() {
        return getRule().getString();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}