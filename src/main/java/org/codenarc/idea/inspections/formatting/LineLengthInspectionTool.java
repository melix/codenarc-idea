package org.codenarc.idea.inspections.formatting;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.formatting.LineLengthRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class LineLengthInspectionTool extends CodeNarcInspectionTool<LineLengthRule> {

    // this code has been generated from org.codenarc.rule.formatting.LineLengthRule

    public static final String GROUP = "Formatting";

    public LineLengthInspectionTool() {
        super(new LineLengthRule());
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


    public void setIgnoreImportStatements(boolean value) {
        getRule().setIgnoreImportStatements(value);
    }

    public boolean isIgnoreImportStatements() {
        return getRule().isIgnoreImportStatements();
    }


    public void setIgnoreLineRegex(String value) {
        getRule().setIgnoreLineRegex(value);
    }

    public String getIgnoreLineRegex() {
        return getRule().getIgnoreLineRegex();
    }


    public void setIgnorePackageStatements(boolean value) {
        getRule().setIgnorePackageStatements(value);
    }

    public boolean isIgnorePackageStatements() {
        return getRule().isIgnorePackageStatements();
    }


    public void setLength(int value) {
        getRule().setLength(value);
    }

    public int getLength() {
        return getRule().getLength();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
