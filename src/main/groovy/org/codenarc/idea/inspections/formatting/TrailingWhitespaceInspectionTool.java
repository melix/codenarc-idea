package org.codenarc.idea.inspections.formatting;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.formatting.TrailingWhitespaceRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class TrailingWhitespaceInspectionTool extends CodeNarcInspectionTool<TrailingWhitespaceRule> {

    // this code has been generated from org.codenarc.rule.formatting.TrailingWhitespaceRule

    public static final String GROUP = "Formatting";

    public TrailingWhitespaceInspectionTool() {
        super(new TrailingWhitespaceRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}