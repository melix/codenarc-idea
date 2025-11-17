package org.codenarc.idea.inspections.unnecessary;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.unnecessary.UnnecessaryToStringRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnnecessaryToStringInspectionTool extends CodeNarcInspectionTool<UnnecessaryToStringRule> {

    // this code has been generated from org.codenarc.rule.unnecessary.UnnecessaryToStringRule

    public static final String GROUP = "Unnecessary";

    public UnnecessaryToStringInspectionTool() {
        super(new UnnecessaryToStringRule());
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


    public void setCheckAssignments(boolean value) {
        getRule().setCheckAssignments(value);
    }

    public boolean isCheckAssignments() {
        return getRule().isCheckAssignments();
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
