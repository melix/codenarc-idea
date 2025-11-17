package org.codenarc.idea.inspections.imports;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.imports.MisorderedStaticImportsRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class MisorderedStaticImportsInspectionTool extends CodeNarcInspectionTool<MisorderedStaticImportsRule> {

    // this code has been generated from org.codenarc.rule.imports.MisorderedStaticImportsRule

    public static final String GROUP = "Imports";

    public MisorderedStaticImportsInspectionTool() {
        super(new MisorderedStaticImportsRule());
        applyDefaultConfiguration(getRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setComesBefore(boolean value) {
        getRule().setComesBefore(value);
    }

    public boolean isComesBefore() {
        return getRule().isComesBefore();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
