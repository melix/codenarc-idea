package org.codenarc.idea.inspections.comments;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.comments.ClassJavadocRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class ClassJavadocInspectionTool extends CodeNarcInspectionTool<ClassJavadocRule> {

    // this code has been generated from org.codenarc.rule.comments.ClassJavadocRule

    public static final String GROUP = "Comments";

    public ClassJavadocInspectionTool() {
        super(new ClassJavadocRule());
        applyDefaultConfiguration(getRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setApplyToNonMainClasses(boolean value) {
        getRule().setApplyToNonMainClasses(value);
    }

    public boolean isApplyToNonMainClasses() {
        return getRule().isApplyToNonMainClasses();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
