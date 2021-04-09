package org.codenarc.idea.inspections.imports;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.imports.DuplicateImportRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class DuplicateImportInspectionTool extends CodeNarcInspectionTool<DuplicateImportRule> {

    // this code has been generated from org.codenarc.rule.imports.DuplicateImportRule

    public static final String GROUP = "Imports";

    public DuplicateImportInspectionTool() {
        super(new DuplicateImportRule());
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