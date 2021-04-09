package org.codenarc.idea.inspections.convention;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.convention.NoTabCharacterRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class NoTabCharacterInspectionTool extends CodeNarcInspectionTool<NoTabCharacterRule> {

    // this code has been generated from org.codenarc.rule.convention.NoTabCharacterRule

    public static final String GROUP = "Convention";

    public NoTabCharacterInspectionTool() {
        super(new NoTabCharacterRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setTabCharacter(String value) {
        getRule().setTabCharacter(value);
    }

    public String getTabCharacter() {
        return getRule().getTabCharacter();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}