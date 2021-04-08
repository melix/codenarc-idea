package org.codenarc.idea.inspections.grails;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.grails.GrailsPublicControllerMethodRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class GrailsPublicControllerMethodInspectionTool extends CodeNarcInspectionTool<GrailsPublicControllerMethodRule> {

    // this code has been generated from org.codenarc.rule.grails.GrailsPublicControllerMethodRule

    public static final String GROUP = "Grails";

    public GrailsPublicControllerMethodInspectionTool() {
        super(new GrailsPublicControllerMethodRule());
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

    // custom code can be written after this line and it will be preserved during the regeneration

}