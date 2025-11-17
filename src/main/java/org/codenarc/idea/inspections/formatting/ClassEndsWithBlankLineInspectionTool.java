package org.codenarc.idea.inspections.formatting;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.formatting.ClassEndsWithBlankLineRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class ClassEndsWithBlankLineInspectionTool extends CodeNarcInspectionTool<ClassEndsWithBlankLineRule> {

    // this code has been generated from org.codenarc.rule.formatting.ClassEndsWithBlankLineRule

    public static final String GROUP = "Formatting";

    public ClassEndsWithBlankLineInspectionTool() {
        super(new ClassEndsWithBlankLineRule());
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


    public void setBlankLineRequired(boolean value) {
        getRule().setBlankLineRequired(value);
    }

    public boolean isBlankLineRequired() {
        return getRule().isBlankLineRequired();
    }


    public void setDoNotApplyToClassNames(String value) {
        getRule().setDoNotApplyToClassNames(value);
    }

    public String getDoNotApplyToClassNames() {
        return getRule().getDoNotApplyToClassNames();
    }


    public void setIgnoreInnerClasses(boolean value) {
        getRule().setIgnoreInnerClasses(value);
    }

    public boolean isIgnoreInnerClasses() {
        return getRule().isIgnoreInnerClasses();
    }


    public void setIgnoreSingleLineClasses(boolean value) {
        getRule().setIgnoreSingleLineClasses(value);
    }

    public boolean isIgnoreSingleLineClasses() {
        return getRule().isIgnoreSingleLineClasses();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
