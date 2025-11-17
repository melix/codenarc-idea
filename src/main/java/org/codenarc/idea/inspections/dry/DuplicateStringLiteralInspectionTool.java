package org.codenarc.idea.inspections.dry;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.dry.DuplicateStringLiteralRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class DuplicateStringLiteralInspectionTool extends CodeNarcInspectionTool<DuplicateStringLiteralRule> {

    // this code has been generated from org.codenarc.rule.dry.DuplicateStringLiteralRule

    public static final String GROUP = "Dry";

    public DuplicateStringLiteralInspectionTool() {
        super(new DuplicateStringLiteralRule());
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


    public void setDuplicateStringMinimumLength(Integer value) {
        getRule().setDuplicateStringMinimumLength(value);
    }

    public Integer getDuplicateStringMinimumLength() {
        return getRule().getDuplicateStringMinimumLength();
    }


    public void setIgnoreStrings(String value) {
        getRule().setIgnoreStrings(value);
    }

    public String getIgnoreStrings() {
        return getRule().getIgnoreStrings();
    }


    public void setIgnoreStringsDelimiter(char value) {
        getRule().setIgnoreStringsDelimiter(value);
    }

    public char getIgnoreStringsDelimiter() {
        return getRule().getIgnoreStringsDelimiter();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
