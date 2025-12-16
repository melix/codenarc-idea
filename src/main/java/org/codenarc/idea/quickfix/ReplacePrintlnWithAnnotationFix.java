package org.codenarc.idea.quickfix;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.IncorrectOperationException;
import groovy.util.logging.Slf4j;
import org.codenarc.idea.CodeNarcBundle;
import org.jetbrains.plugins.groovy.codeInspection.GroovyFix;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ReplacePrintlnWithAnnotationFix extends GroovyFix {
    @Override
    protected void doFix(Project project, ProblemDescriptor descriptor) throws IncorrectOperationException {
        PsiClass topLevelClass = PsiUtil.getTopLevelClass(descriptor.getPsiElement());

        if (topLevelClass == null) {
            return;
        }

        var annotationFqn = Slf4j.class.getName();
        var modifierList = topLevelClass.getModifierList();
        if (modifierList != null && modifierList.findAnnotation(annotationFqn) == null) {
            PsiAnnotation annotation = modifierList.addAnnotation(annotationFqn);
            JavaCodeStyleManager.getInstance(project).shortenClassReferences(annotation);
        }

        new ReplaceStatementFix(GrMethodCall.class, "println", "log.info").applyFix(project, descriptor);
    }

    @Override
    public String getFamilyName() {
        return CodeNarcBundle.message("use.logging.instead.of.println");
    }
}
