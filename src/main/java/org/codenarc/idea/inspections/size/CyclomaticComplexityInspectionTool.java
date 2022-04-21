package org.codenarc.idea.inspections.size;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.size.CyclomaticComplexityRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class CyclomaticComplexityInspectionTool extends CodeNarcInspectionTool<CyclomaticComplexityRule> {

    // this code has been generated from org.codenarc.rule.size.CyclomaticComplexityRule

    public static final String GROUP = "Size";

    public CyclomaticComplexityInspectionTool() {
        super(new CyclomaticComplexityRule());
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


    public void setIgnoreMethodNames(String value) {
        getRule().setIgnoreMethodNames(value);
    }

    public String getIgnoreMethodNames() {
        return getRule().getIgnoreMethodNames();
    }


    public void setMaxClassAverageMethodComplexity(int value) {
        getRule().setMaxClassAverageMethodComplexity(value);
    }

    public int getMaxClassAverageMethodComplexity() {
        return getRule().getMaxClassAverageMethodComplexity();
    }


    public void setMaxClassComplexity(int value) {
        getRule().setMaxClassComplexity(value);
    }

    public int getMaxClassComplexity() {
        return getRule().getMaxClassComplexity();
    }


    public void setMaxMethodComplexity(int value) {
        getRule().setMaxMethodComplexity(value);
    }

    public int getMaxMethodComplexity() {
        return getRule().getMaxMethodComplexity();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
