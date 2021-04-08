package org.codenarc.idea.inspections.unnecessary;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.unnecessary.UnnecessaryPackageReferenceRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnnecessaryPackageReferenceInspectionTool extends CodeNarcInspectionTool<UnnecessaryPackageReferenceRule> {

    // this code has been generated from org.codenarc.rule.unnecessary.UnnecessaryPackageReferenceRule

    public static final String GROUP = "Unnecessary";

    public UnnecessaryPackageReferenceInspectionTool() {
        super(new UnnecessaryPackageReferenceRule());
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

    // custom code can be written after this line and it will be preserved during the regeneration

}