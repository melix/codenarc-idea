package org.codenarc.idea.inspections.unused;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.unused.UnusedPrivateFieldRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnusedPrivateFieldInspectionTool extends CodeNarcInspectionTool<UnusedPrivateFieldRule> {

    // this code has been generated from org.codenarc.rule.unused.UnusedPrivateFieldRule

    public static final String GROUP = "Unused";

    public UnusedPrivateFieldInspectionTool() {
        super(new UnusedPrivateFieldRule());
        applyDefaultConfiguration(getRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setAllowConstructorOnlyUsages(boolean value) {
        getRule().setAllowConstructorOnlyUsages(value);
    }

    public boolean isAllowConstructorOnlyUsages() {
        return getRule().isAllowConstructorOnlyUsages();
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


    public void setIgnoreClassesAnnotatedWithNames(String value) {
        getRule().setIgnoreClassesAnnotatedWithNames(value);
    }

    public String getIgnoreClassesAnnotatedWithNames() {
        return getRule().getIgnoreClassesAnnotatedWithNames();
    }


    public void setIgnoreFieldNames(String value) {
        getRule().setIgnoreFieldNames(value);
    }

    public String getIgnoreFieldNames() {
        return getRule().getIgnoreFieldNames();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
