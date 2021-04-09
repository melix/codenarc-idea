package org.codenarc.idea.inspections.generic;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.generic.IllegalClassMemberRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class IllegalClassMemberInspectionTool extends CodeNarcInspectionTool<IllegalClassMemberRule> {

    // this code has been generated from org.codenarc.rule.generic.IllegalClassMemberRule

    public static final String GROUP = "Generic";

    public IllegalClassMemberInspectionTool() {
        super(new IllegalClassMemberRule());
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


    public void setIgnoreMethodsWithAnnotationNames(String value) {
        getRule().setIgnoreMethodsWithAnnotationNames(value);
    }

    public String getIgnoreMethodsWithAnnotationNames() {
        return getRule().getIgnoreMethodsWithAnnotationNames();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
