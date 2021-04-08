package org.codenarc.idea.inspections.generic;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.generic.IllegalPackageReferenceRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class IllegalPackageReferenceInspectionTool extends CodeNarcInspectionTool<IllegalPackageReferenceRule> {

    // this code has been generated from org.codenarc.rule.generic.IllegalPackageReferenceRule

    public static final String GROUP = "Generic";

    public IllegalPackageReferenceInspectionTool() {
        super(new IllegalPackageReferenceRule());
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


    public void setPackageNames(String value) {
        getRule().setPackageNames(value);
    }

    public String getPackageNames() {
        return getRule().getPackageNames();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}