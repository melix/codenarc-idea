package org.codenarc.idea.inspections.generic;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.generic.StatelessClassRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class StatelessClassInspectionTool extends CodeNarcInspectionTool<StatelessClassRule> {

    // this code has been generated from org.codenarc.rule.generic.StatelessClassRule

    public static final String GROUP = "Generic";

    public StatelessClassInspectionTool() {
        super(new StatelessClassRule());
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


    public void setIgnoreFieldNames(String value) {
        getRule().setIgnoreFieldNames(value);
    }

    public String getIgnoreFieldNames() {
        return getRule().getIgnoreFieldNames();
    }


    public void setIgnoreFieldTypes(String value) {
        getRule().setIgnoreFieldTypes(value);
    }

    public String getIgnoreFieldTypes() {
        return getRule().getIgnoreFieldTypes();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}