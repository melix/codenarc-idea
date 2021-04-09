package org.codenarc.idea.inspections.serialization;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.serialization.EnumCustomSerializationIgnoredRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class EnumCustomSerializationIgnoredInspectionTool extends CodeNarcInspectionTool<EnumCustomSerializationIgnoredRule> {

    // this code has been generated from org.codenarc.rule.serialization.EnumCustomSerializationIgnoredRule

    public static final String GROUP = "Serialization";

    public EnumCustomSerializationIgnoredInspectionTool() {
        super(new EnumCustomSerializationIgnoredRule());
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