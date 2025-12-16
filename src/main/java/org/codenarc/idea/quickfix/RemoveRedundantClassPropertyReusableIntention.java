package org.codenarc.idea.quickfix;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.plugins.groovy.intentions.base.Intention;
import org.jetbrains.plugins.groovy.intentions.base.PsiElementPredicate;
import org.jetbrains.plugins.groovy.intentions.style.RemoveRedundantClassPropertyIntention;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class RemoveRedundantClassPropertyReusableIntention extends Intention implements ReusableIntention {
    private final RemoveRedundantClassPropertyIntention delegate = new RemoveRedundantClassPropertyIntention();

    @Override
    public Class<?> getDelegateClass() {
        return delegate.getClass();
    }

    @Override
    public void processIntention(PsiElement element, Project project, @Nullable Editor editor) throws IncorrectOperationException {
        if (element instanceof GrReferenceExpression ref) {
            if (ref.getQualifier() != null) {
                ref.replaceWithExpression(ref.getQualifier(), true);
            }
        }
    }

    @Override
    public PsiElementPredicate getElementPredicate() {
        return element -> {
            if (element instanceof GrReferenceExpression ref) {
                if ("class".equals(ref.getReferenceName())) {
                    PsiElement qualifier = ref.getQualifier();
                    if (qualifier instanceof GrReferenceExpression _qualifier) {
                        if (_qualifier.resolve() instanceof PsiClass) {
                            return true;
                        }
                    }
                }
            }

            return false;
        };
    }
}