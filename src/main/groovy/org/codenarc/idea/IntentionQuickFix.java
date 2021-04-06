package org.codenarc.idea;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiEditorUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.codeInspection.GroovyFix;
import org.jetbrains.plugins.groovy.intentions.base.Intention;

public class IntentionQuickFix extends GroovyFix {

    public static LocalQuickFix from(Intention action) {
        return new IntentionQuickFix(action);
    }

    private final Intention intention;

    private IntentionQuickFix(Intention intention) {
        this.intention = intention;
    }

    @Override
    protected void doFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) throws IncorrectOperationException {
        intention.invoke(project, PsiEditorUtil.findEditor(descriptor.getPsiElement()), descriptor.getPsiElement().getContainingFile());
    }

    @Override
    public @IntentionFamilyName @NotNull String getFamilyName() {
        return intention.getFamilyName();
    }

}
