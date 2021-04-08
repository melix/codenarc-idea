package org.codenarc.idea.inspections.generic;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.generic.IllegalClassReferenceRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class IllegalClassReferenceInspectionTool extends CodeNarcInspectionTool<IllegalClassReferenceRule> {

    // this code has been generated from org.codenarc.rule.generic.IllegalClassReferenceRule

    public static final String GROUP = "Generic";

    public IllegalClassReferenceInspectionTool() {
        super(new IllegalClassReferenceRule());
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


    public void setClassNames(String value) {
        getRule().setClassNames(value);
    }

    public String getClassNames() {
        return getRule().getClassNames();
    }


    public void setDoNotApplyToClassNames(String value) {
        getRule().setDoNotApplyToClassNames(value);
    }

    public String getDoNotApplyToClassNames() {
        return getRule().getDoNotApplyToClassNames();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}