package org.codenarc.idea.inspections.groovyism;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.groovyism.ExplicitCallToPlusMethodRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class ExplicitCallToPlusMethodInspectionTool extends CodeNarcInspectionTool<ExplicitCallToPlusMethodRule> {

    // this code has been generated from org.codenarc.rule.groovyism.ExplicitCallToPlusMethodRule

    public static final String GROUP = "Groovyism";

    public ExplicitCallToPlusMethodInspectionTool() {
        super(new ExplicitCallToPlusMethodRule());
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


    public void setIgnoreThisReference(boolean value) {
        getRule().setIgnoreThisReference(value);
    }

    public boolean getIgnoreThisReference() {
        return getRule().getIgnoreThisReference();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}