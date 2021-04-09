package org.codenarc.idea.inspections.formatting;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.formatting.ClassStartsWithBlankLineRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class ClassStartsWithBlankLineInspectionTool extends CodeNarcInspectionTool<ClassStartsWithBlankLineRule> {

    // this code has been generated from org.codenarc.rule.formatting.ClassStartsWithBlankLineRule

    public static final String GROUP = "Formatting";

    public ClassStartsWithBlankLineInspectionTool() {
        super(new ClassStartsWithBlankLineRule());
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


    public void setBlankLineRequired(boolean value) {
        getRule().setBlankLineRequired(value);
    }

    public boolean getBlankLineRequired() {
        return getRule().getBlankLineRequired();
    }


    public void setDoNotApplyToClassNames(String value) {
        getRule().setDoNotApplyToClassNames(value);
    }

    public String getDoNotApplyToClassNames() {
        return getRule().getDoNotApplyToClassNames();
    }


    public void setIgnoreInnerClasses(boolean value) {
        getRule().setIgnoreInnerClasses(value);
    }

    public boolean getIgnoreInnerClasses() {
        return getRule().getIgnoreInnerClasses();
    }


    public void setIgnoreSingleLineClasses(boolean value) {
        getRule().setIgnoreSingleLineClasses(value);
    }

    public boolean getIgnoreSingleLineClasses() {
        return getRule().getIgnoreSingleLineClasses();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}