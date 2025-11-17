package org.codenarc.idea.inspections.dry;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.dry.DuplicateNumberLiteralRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class DuplicateNumberLiteralInspectionTool extends CodeNarcInspectionTool<DuplicateNumberLiteralRule> {

    // this code has been generated from org.codenarc.rule.dry.DuplicateNumberLiteralRule

    public static final String GROUP = "Dry";

    public DuplicateNumberLiteralInspectionTool() {
        super(new DuplicateNumberLiteralRule());
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


    public void setDuplicateNumberMinimumValue(Integer value) {
        getRule().setDuplicateNumberMinimumValue(value);
    }

    public Integer getDuplicateNumberMinimumValue() {
        return getRule().getDuplicateNumberMinimumValue();
    }


    public void setIgnoreNumbers(String value) {
        getRule().setIgnoreNumbers(value);
    }

    public String getIgnoreNumbers() {
        return getRule().getIgnoreNumbers();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
