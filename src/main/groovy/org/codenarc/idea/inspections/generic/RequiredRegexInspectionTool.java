package org.codenarc.idea.inspections.generic;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.generic.RequiredRegexRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class RequiredRegexInspectionTool extends CodeNarcInspectionTool<RequiredRegexRule> {

    // this code has been generated from org.codenarc.rule.generic.RequiredRegexRule

    public static final String GROUP = "Generic";

    public RequiredRegexInspectionTool() {
        super(new RequiredRegexRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setRegex(String value) {
        getRule().setRegex(value);
    }

    public String getRegex() {
        return getRule().getRegex();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}