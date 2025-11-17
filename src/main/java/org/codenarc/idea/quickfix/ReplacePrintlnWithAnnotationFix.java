package org.codenarc.idea.quickfix;

import com.intellij.codeInsight.intention.AddAnnotationPsiFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.IncorrectOperationException;
import groovy.util.logging.Slf4j;
import org.codenarc.idea.CodeNarcBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.codeInspection.GroovyFix;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall;

public class ReplacePrintlnWithAnnotationFix extends GroovyFix {

    @Override
    protected void doFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) throws IncorrectOperationException {
        PsiClass topLevelClass = PsiUtil.getTopLevelClass(descriptor.getPsiElement());

        if (topLevelClass == null) {
            return;
        }

        new AddAnnotationPsiFix(Slf4j.class.getName(), topLevelClass, new PsiNameValuePair[0]).applyFix();
        new ReplaceStatementFix(GrMethodCall.class, "println", "log.info").applyFix(project, descriptor);
    }

    @Override
    public @NotNull String getFamilyName() {
        return CodeNarcBundle.message("use.logging.instead.of.println");
    }
}