package org.codenarc.idea.inspections.size;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.size.AbcMetricRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class AbcMetricInspectionTool extends CodeNarcInspectionTool<AbcMetricRule> {

    // this code has been generated from org.codenarc.rule.size.AbcMetricRule

    public static final String GROUP = "Size";

    public AbcMetricInspectionTool() {
        super(new AbcMetricRule());
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


    public void setMaxClassAbcScore(int value) {
        getRule().setMaxClassAbcScore(value);
    }

    public int getMaxClassAbcScore() {
        return getRule().getMaxClassAbcScore();
    }


    public void setMaxClassAverageMethodAbcScore(int value) {
        getRule().setMaxClassAverageMethodAbcScore(value);
    }

    public int getMaxClassAverageMethodAbcScore() {
        return getRule().getMaxClassAverageMethodAbcScore();
    }


    public void setMaxMethodAbcScore(int value) {
        getRule().setMaxMethodAbcScore(value);
    }

    public int getMaxMethodAbcScore() {
        return getRule().getMaxMethodAbcScore();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
