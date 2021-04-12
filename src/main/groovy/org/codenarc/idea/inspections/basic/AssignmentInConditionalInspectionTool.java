package org.codenarc.idea.inspections.basic;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.idea.quickfix.ReplaceStatementFix;
import org.codenarc.rule.Violation;
import org.codenarc.rule.basic.AssignmentInConditionalRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class AssignmentInConditionalInspectionTool extends CodeNarcInspectionTool<AssignmentInConditionalRule> {

    // this code has been generated from org.codenarc.rule.basic.AssignmentInConditionalRule

    public static final String GROUP = "Basic";

    public AssignmentInConditionalInspectionTool() {
        super(new AssignmentInConditionalRule());
        applyDefaultConfiguration(getRule());
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

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.singleton(new ReplaceStatementFix(GrAssignmentExpression.class, "=", "=="));
    }

}
