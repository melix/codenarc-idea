package org.codenarc.idea.inspections.generic;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.generic.IllegalRegexRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class IllegalRegexInspectionTool extends CodeNarcInspectionTool<IllegalRegexRule> {

    // this code has been generated from org.codenarc.rule.generic.IllegalRegexRule

    public static final String GROUP = "Generic";

    public IllegalRegexInspectionTool() {
        super(new IllegalRegexRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setRegex(String value) {
        getRule().setRegex(value);
    }

    public String getRegex() {
        return getRule().getRegex();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}