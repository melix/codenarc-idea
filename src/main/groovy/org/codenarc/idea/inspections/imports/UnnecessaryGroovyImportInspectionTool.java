package org.codenarc.idea.inspections.imports;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.imports.UnnecessaryGroovyImportRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnnecessaryGroovyImportInspectionTool extends CodeNarcInspectionTool<UnnecessaryGroovyImportRule> {

    // this code has been generated from org.codenarc.rule.imports.UnnecessaryGroovyImportRule

    public static final String GROUP = "Imports";

    public UnnecessaryGroovyImportInspectionTool() {
        super(new UnnecessaryGroovyImportRule());
        applyDefaultConfiguration(getRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
