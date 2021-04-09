package org.codenarc.idea.inspections.jdbc;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.jdbc.JdbcConnectionReferenceRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class JdbcConnectionReferenceInspectionTool extends CodeNarcInspectionTool<JdbcConnectionReferenceRule> {

    // this code has been generated from org.codenarc.rule.jdbc.JdbcConnectionReferenceRule

    public static final String GROUP = "Jdbc";

    public JdbcConnectionReferenceInspectionTool() {
        super(new JdbcConnectionReferenceRule());
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

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}