package org.codenarc.idea.inspections.unnecessary;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.unnecessary.UnnecessaryObjectReferencesRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnnecessaryObjectReferencesInspectionTool extends CodeNarcInspectionTool<UnnecessaryObjectReferencesRule> {

    // this code has been generated from org.codenarc.rule.unnecessary.UnnecessaryObjectReferencesRule

    public static final String GROUP = "Unnecessary";

    public UnnecessaryObjectReferencesInspectionTool() {
        super(new UnnecessaryObjectReferencesRule());
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


    public void setMaxReferencesAllowed(int value) {
        getRule().setMaxReferencesAllowed(value);
    }

    public int getMaxReferencesAllowed() {
        return getRule().getMaxReferencesAllowed();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}