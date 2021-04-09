package org.codenarc.idea.inspections.serialization;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.serialization.SerialVersionUIDRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class SerialVersionUIDInspectionTool extends CodeNarcInspectionTool<SerialVersionUIDRule> {

    // this code has been generated from org.codenarc.rule.serialization.SerialVersionUIDRule

    public static final String GROUP = "Serialization";

    public SerialVersionUIDInspectionTool() {
        super(new SerialVersionUIDRule());
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

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}