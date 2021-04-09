package org.codenarc.idea.inspections.size;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.size.ClassSizeRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class ClassSizeInspectionTool extends CodeNarcInspectionTool<ClassSizeRule> {

    // this code has been generated from org.codenarc.rule.size.ClassSizeRule

    public static final String GROUP = "Size";

    public ClassSizeInspectionTool() {
        super(new ClassSizeRule());
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


    public void setMaxLines(int value) {
        getRule().setMaxLines(value);
    }

    public int getMaxLines() {
        return getRule().getMaxLines();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}