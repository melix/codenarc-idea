package org.codenarc.idea.inspections.grails;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.grails.GrailsStatelessServiceRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class GrailsStatelessServiceInspectionTool extends CodeNarcInspectionTool<GrailsStatelessServiceRule> {

    // this code has been generated from org.codenarc.rule.grails.GrailsStatelessServiceRule

    public static final String GROUP = "Grails";

    public GrailsStatelessServiceInspectionTool() {
        super(new GrailsStatelessServiceRule());
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


    public void setIgnoreFieldTypes(String value) {
        getRule().setIgnoreFieldTypes(value);
    }

    public String getIgnoreFieldTypes() {
        return getRule().getIgnoreFieldTypes();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}