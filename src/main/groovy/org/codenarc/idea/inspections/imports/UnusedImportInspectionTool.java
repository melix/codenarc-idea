package org.codenarc.idea.inspections.imports;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.imports.UnusedImportRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnusedImportInspectionTool extends CodeNarcInspectionTool<UnusedImportRule> {

    // this code has been generated from org.codenarc.rule.imports.UnusedImportRule

    public static final String GROUP = "Imports";

    public UnusedImportInspectionTool() {
        super(new UnusedImportRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}