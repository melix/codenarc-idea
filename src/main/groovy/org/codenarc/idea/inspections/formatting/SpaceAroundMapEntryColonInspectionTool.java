package org.codenarc.idea.inspections.formatting;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.formatting.SpaceAroundMapEntryColonRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class SpaceAroundMapEntryColonInspectionTool extends CodeNarcInspectionTool<SpaceAroundMapEntryColonRule> {

    // this code has been generated from org.codenarc.rule.formatting.SpaceAroundMapEntryColonRule

    public static final String GROUP = "Formatting";

    public SpaceAroundMapEntryColonInspectionTool() {
        super(new SpaceAroundMapEntryColonRule());
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


    public void setCharacterAfterColonRegex(String value) {
        getRule().setCharacterAfterColonRegex(value);
    }

    public String getCharacterAfterColonRegex() {
        return getRule().getCharacterAfterColonRegex();
    }


    public void setCharacterBeforeColonRegex(String value) {
        getRule().setCharacterBeforeColonRegex(value);
    }

    public String getCharacterBeforeColonRegex() {
        return getRule().getCharacterBeforeColonRegex();
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