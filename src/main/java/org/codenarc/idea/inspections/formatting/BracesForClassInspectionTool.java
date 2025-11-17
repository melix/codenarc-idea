package org.codenarc.idea.inspections.formatting;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.formatting.BracesForClassRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class BracesForClassInspectionTool extends CodeNarcInspectionTool<BracesForClassRule> {

    // this code has been generated from org.codenarc.rule.formatting.BracesForClassRule

    public static final String GROUP = "Formatting";

    public BracesForClassInspectionTool() {
        super(new BracesForClassRule());
        applyDefaultConfiguration(getRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setSameLine(boolean value) {
        getRule().setSameLine(value);
    }

    public boolean isSameLine() {
        return getRule().isSameLine();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
