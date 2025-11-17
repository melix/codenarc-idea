package org.codenarc.idea.inspections.imports;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.idea.quickfix.ReplaceOnDemandImportFix;
import org.codenarc.rule.Violation;
import org.codenarc.rule.imports.NoWildcardImportsRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.lang.psi.api.toplevel.imports.GrImportStatement;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class NoWildcardImportsInspectionTool extends CodeNarcInspectionTool<NoWildcardImportsRule> {

    // this code has been generated from org.codenarc.rule.imports.NoWildcardImportsRule

    public static final String GROUP = "Imports";

    public NoWildcardImportsInspectionTool() {
        super(new NoWildcardImportsRule());
        applyDefaultConfiguration(getRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setIgnoreImports(boolean value) {
        getRule().setIgnoreImports(value);
    }

    public boolean isIgnoreImports() {
        return getRule().isIgnoreImports();
    }


    public void setIgnoreStaticImports(boolean value) {
        getRule().setIgnoreStaticImports(value);
    }

    public boolean isIgnoreStaticImports() {
        return getRule().isIgnoreStaticImports();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected void applyDefaultConfiguration(NoWildcardImportsRule rule) {
        // useful for DSLs
        rule.setIgnoreStaticImports(true);
    }

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        if (violatingElement instanceof GrImportStatement) {
            GrImportStatement importStatement = (GrImportStatement) violatingElement;
            if (!importStatement.isStatic()) {
                return Collections.singleton(new ReplaceOnDemandImportFix());
            }
        }
        return Collections.emptyList();
    }

}
