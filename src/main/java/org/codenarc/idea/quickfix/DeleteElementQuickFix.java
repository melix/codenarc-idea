package org.codenarc.idea.quickfix;

import com.intellij.codeInsight.daemon.impl.quickfix.DeleteElementFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.siyeh.ig.psiutils.CommentTracker;
import org.jetbrains.plugins.groovy.codeInspection.GroovyFix;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DeleteElementQuickFix extends GroovyFix {
    private final DeleteElementFix delegate;
    private final PsiElement element;

    public DeleteElementQuickFix(PsiElement element) {
        this.element = element;
        delegate = new DeleteElementFix(element);
    }

    @Override
    protected void doFix(Project project, ProblemDescriptor problemDescriptor) throws IncorrectOperationException {
        new CommentTracker().deleteAndRestoreComments(element);
    }

    @Override
    public @IntentionFamilyName String getFamilyName() {
        return delegate.getFamilyName();
    }
}
