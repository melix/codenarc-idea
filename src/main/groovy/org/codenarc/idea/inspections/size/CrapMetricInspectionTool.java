package org.codenarc.idea.inspections.size;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.size.CrapMetricRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class CrapMetricInspectionTool extends CodeNarcInspectionTool<CrapMetricRule> {

    // this code has been generated from org.codenarc.rule.size.CrapMetricRule

    public static final String GROUP = "Size";

    public CrapMetricInspectionTool() {
        super(new CrapMetricRule());
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


    public void setCoberturaXmlFile(String value) {
        getRule().setCoberturaXmlFile(value);
    }

    public String getCoberturaXmlFile() {
        return getRule().getCoberturaXmlFile();
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


    public void setMaxClassAverageMethodCrapScore(java.math.BigDecimal value) {
        getRule().setMaxClassAverageMethodCrapScore(value);
    }

    public java.math.BigDecimal getMaxClassAverageMethodCrapScore() {
        return getRule().getMaxClassAverageMethodCrapScore();
    }


    public void setMaxClassCrapScore(java.math.BigDecimal value) {
        getRule().setMaxClassCrapScore(value);
    }

    public java.math.BigDecimal getMaxClassCrapScore() {
        return getRule().getMaxClassCrapScore();
    }


    public void setMaxMethodCrapScore(java.math.BigDecimal value) {
        getRule().setMaxMethodCrapScore(value);
    }

    public java.math.BigDecimal getMaxMethodCrapScore() {
        return getRule().getMaxMethodCrapScore();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
