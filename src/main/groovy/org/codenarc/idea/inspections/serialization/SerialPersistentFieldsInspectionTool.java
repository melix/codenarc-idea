package org.codenarc.idea.inspections.serialization;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.serialization.SerialPersistentFieldsRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class SerialPersistentFieldsInspectionTool extends CodeNarcInspectionTool<SerialPersistentFieldsRule> {

    // this code has been generated from org.codenarc.rule.serialization.SerialPersistentFieldsRule

    public static final String GROUP = "Serialization";

    public SerialPersistentFieldsInspectionTool() {
        super(new SerialPersistentFieldsRule());
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