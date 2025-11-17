package org.codenarc.idea.inspections.formatting;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.formatting.IndentationRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class IndentationInspectionTool extends CodeNarcInspectionTool<IndentationRule> {

    // this code has been generated from org.codenarc.rule.formatting.IndentationRule

    public static final String GROUP = "Formatting";

    public IndentationInspectionTool() {
        super(new IndentationRule());
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


    public void setIndentUnderLabel(boolean value) {
        getRule().setIndentUnderLabel(value);
    }

    public boolean isIndentUnderLabel() {
        return getRule().isIndentUnderLabel();
    }


    public void setSpacesPerIndentLevel(int value) {
        getRule().setSpacesPerIndentLevel(value);
    }

    public int getSpacesPerIndentLevel() {
        return getRule().getSpacesPerIndentLevel();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
