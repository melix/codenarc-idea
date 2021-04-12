package org.codenarc.idea.inspections.convention;

import com.intellij.codeInsight.intention.AddAnnotationPsiFix;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiNameValuePair;
import groovy.transform.CompileDynamic;
import groovy.transform.CompileStatic;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.convention.CompileStaticRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class CompileStaticInspectionTool extends CodeNarcInspectionTool<CompileStaticRule> {

    // this code has been generated from org.codenarc.rule.convention.CompileStaticRule

    public static final String GROUP = "Convention";

    public CompileStaticInspectionTool() {
        super(new CompileStaticRule());
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

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected void applyDefaultConfiguration(CompileStaticRule rule) {
        rule.setDoNotApplyToFilesMatching("*Specification.groovy,*Spec.groovy,*Function.groovy");
    }

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        if (violatingElement instanceof PsiModifierListOwner) {
            return Arrays.asList(
                    new AddAnnotationPsiFix(CompileStatic.class.getName(), (PsiModifierListOwner) violatingElement, new PsiNameValuePair[0]),
                    new AddAnnotationPsiFix(CompileDynamic.class.getName(), (PsiModifierListOwner) violatingElement, new PsiNameValuePair[0]),
                    //  TODO: check if the annotation is on the classpath (= we are in the Grails project)
                    new AddAnnotationPsiFix("grails.compiler.GrailsCompileStatic", (PsiModifierListOwner) violatingElement, new PsiNameValuePair[0])
            );
        }
        return Collections.emptyList();
    }

}
