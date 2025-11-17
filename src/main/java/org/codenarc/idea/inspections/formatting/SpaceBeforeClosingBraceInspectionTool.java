package org.codenarc.idea.inspections.formatting;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.formatting.SpaceBeforeClosingBraceRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class SpaceBeforeClosingBraceInspectionTool extends CodeNarcInspectionTool<SpaceBeforeClosingBraceRule> {

    // this code has been generated from org.codenarc.rule.formatting.SpaceBeforeClosingBraceRule

    public static final String GROUP = "Formatting";

    public SpaceBeforeClosingBraceInspectionTool() {
        super(new SpaceBeforeClosingBraceRule());
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


    public void setCheckClosureMapEntryValue(boolean value) {
        getRule().setCheckClosureMapEntryValue(value);
    }

    public boolean isCheckClosureMapEntryValue() {
        return getRule().isCheckClosureMapEntryValue();
    }


    public void setDoNotApplyToClassNames(String value) {
        getRule().setDoNotApplyToClassNames(value);
    }

    public String getDoNotApplyToClassNames() {
        return getRule().getDoNotApplyToClassNames();
    }


    public void setIgnoreEmptyBlock(boolean value) {
        getRule().setIgnoreEmptyBlock(value);
    }

    public boolean isIgnoreEmptyBlock() {
        return getRule().isIgnoreEmptyBlock();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
