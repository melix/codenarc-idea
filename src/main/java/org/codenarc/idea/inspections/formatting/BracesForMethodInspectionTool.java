package org.codenarc.idea.inspections.formatting;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.formatting.BracesForMethodRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class BracesForMethodInspectionTool extends CodeNarcInspectionTool<BracesForMethodRule> {

    // this code has been generated from org.codenarc.rule.formatting.BracesForMethodRule

    public static final String GROUP = "Formatting";

    public BracesForMethodInspectionTool() {
        super(new BracesForMethodRule());
        applyDefaultConfiguration(getRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setAllowBraceOnNextLineForMultilineDeclarations(boolean value) {
        getRule().setAllowBraceOnNextLineForMultilineDeclarations(value);
    }

    public boolean getAllowBraceOnNextLineForMultilineDeclarations() {
        return getRule().getAllowBraceOnNextLineForMultilineDeclarations();
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


    public void setSameLine(boolean value) {
        getRule().setSameLine(value);
    }

    public boolean getSameLine() {
        return getRule().getSameLine();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
