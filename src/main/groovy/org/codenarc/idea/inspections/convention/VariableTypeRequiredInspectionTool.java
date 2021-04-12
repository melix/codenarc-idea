package org.codenarc.idea.inspections.convention;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.convention.VariableTypeRequiredRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class VariableTypeRequiredInspectionTool extends CodeNarcInspectionTool<VariableTypeRequiredRule> {

    // this code has been generated from org.codenarc.rule.convention.VariableTypeRequiredRule

    public static final String GROUP = "Convention";

    public VariableTypeRequiredInspectionTool() {
        super(new VariableTypeRequiredRule());
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


    public void setIgnoreVariableNames(String value) {
        getRule().setIgnoreVariableNames(value);
    }

    public String getIgnoreVariableNames() {
        return getRule().getIgnoreVariableNames();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
