package org.codenarc.idea.inspections.generic;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.generic.IllegalSubclassRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class IllegalSubclassInspectionTool extends CodeNarcInspectionTool<IllegalSubclassRule> {

    // this code has been generated from org.codenarc.rule.generic.IllegalSubclassRule

    public static final String GROUP = "Generic";

    public IllegalSubclassInspectionTool() {
        super(new IllegalSubclassRule());
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


    public void setSuperclassNames(String value) {
        getRule().setSuperclassNames(value);
    }

    public String getSuperclassNames() {
        return getRule().getSuperclassNames();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
