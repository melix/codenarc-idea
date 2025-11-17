package org.codenarc.idea.inspections.unnecessary;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.unnecessary.UnnecessaryIfStatementRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnnecessaryIfStatementInspectionTool extends CodeNarcInspectionTool<UnnecessaryIfStatementRule> {

    // this code has been generated from org.codenarc.rule.unnecessary.UnnecessaryIfStatementRule

    public static final String GROUP = "Unnecessary";

    public UnnecessaryIfStatementInspectionTool() {
        super(new UnnecessaryIfStatementRule());
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


    public void setCheckLastStatementImplicitElse(boolean value) {
        getRule().setCheckLastStatementImplicitElse(value);
    }

    public boolean isCheckLastStatementImplicitElse() {
        return getRule().isCheckLastStatementImplicitElse();
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
        return Collections.emptyList();
    }

}
