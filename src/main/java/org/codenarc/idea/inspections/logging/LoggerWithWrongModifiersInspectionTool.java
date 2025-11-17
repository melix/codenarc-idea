package org.codenarc.idea.inspections.logging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.logging.LoggerWithWrongModifiersRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class LoggerWithWrongModifiersInspectionTool extends CodeNarcInspectionTool<LoggerWithWrongModifiersRule> {

    // this code has been generated from org.codenarc.rule.logging.LoggerWithWrongModifiersRule

    public static final String GROUP = "Logging";

    public LoggerWithWrongModifiersInspectionTool() {
        super(new LoggerWithWrongModifiersRule());
        applyDefaultConfiguration(getRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setAllowNonStaticLogger(boolean value) {
        getRule().setAllowNonStaticLogger(value);
    }

    public boolean isAllowNonStaticLogger() {
        return getRule().isAllowNonStaticLogger();
    }


    public void setAllowProtectedLogger(boolean value) {
        getRule().setAllowProtectedLogger(value);
    }

    public boolean isAllowProtectedLogger() {
        return getRule().isAllowProtectedLogger();
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
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
