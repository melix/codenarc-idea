package org.codenarc.idea.inspections.dry;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.dry.DuplicateMapLiteralRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class DuplicateMapLiteralInspectionTool extends CodeNarcInspectionTool<DuplicateMapLiteralRule> {

    // this code has been generated from org.codenarc.rule.dry.DuplicateMapLiteralRule

    public static final String GROUP = "Dry";

    public DuplicateMapLiteralInspectionTool() {
        super(new DuplicateMapLiteralRule());
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

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected void applyDefaultConfiguration(DuplicateMapLiteralRule rule) {
        rule.setDoNotApplyToFilesMatching(SPECIFICATION_FILENAMES);
    }

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        // TODO: extract constant
        return Collections.emptyList();
    }

}
