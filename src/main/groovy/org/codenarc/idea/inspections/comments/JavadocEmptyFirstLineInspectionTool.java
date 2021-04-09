package org.codenarc.idea.inspections.comments;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.comments.JavadocEmptyFirstLineRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class JavadocEmptyFirstLineInspectionTool extends CodeNarcInspectionTool<JavadocEmptyFirstLineRule> {

    // this code has been generated from org.codenarc.rule.comments.JavadocEmptyFirstLineRule

    public static final String GROUP = "Comments";

    public JavadocEmptyFirstLineInspectionTool() {
        super(new JavadocEmptyFirstLineRule());
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