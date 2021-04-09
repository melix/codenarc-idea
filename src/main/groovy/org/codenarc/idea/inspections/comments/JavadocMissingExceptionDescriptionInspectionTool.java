package org.codenarc.idea.inspections.comments;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.comments.JavadocMissingExceptionDescriptionRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class JavadocMissingExceptionDescriptionInspectionTool extends CodeNarcInspectionTool<JavadocMissingExceptionDescriptionRule> {

    // this code has been generated from org.codenarc.rule.comments.JavadocMissingExceptionDescriptionRule

    public static final String GROUP = "Comments";

    public JavadocMissingExceptionDescriptionInspectionTool() {
        super(new JavadocMissingExceptionDescriptionRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setAllowMultiline(boolean value) {
        getRule().setAllowMultiline(value);
    }

    public boolean isAllowMultiline() {
        return getRule().isAllowMultiline();
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