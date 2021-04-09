package org.codenarc.idea.inspections.design;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.design.StatelessSingletonRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class StatelessSingletonInspectionTool extends CodeNarcInspectionTool<StatelessSingletonRule> {

    // this code has been generated from org.codenarc.rule.design.StatelessSingletonRule

    public static final String GROUP = "Design";

    public StatelessSingletonInspectionTool() {
        super(new StatelessSingletonRule());
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


    public void setInstanceRegex(String value) {
        getRule().setInstanceRegex(value);
    }

    public String getInstanceRegex() {
        return getRule().getInstanceRegex();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}