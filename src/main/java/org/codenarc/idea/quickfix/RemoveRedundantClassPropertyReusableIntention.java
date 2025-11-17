package org.codenarc.idea.quickfix;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.intentions.base.Intention;
import org.jetbrains.plugins.groovy.intentions.base.PsiElementPredicate;
import org.jetbrains.plugins.groovy.intentions.style.RemoveRedundantClassPropertyIntention;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrExpression;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;

public class RemoveRedundantClassPropertyReusableIntention extends Intention implements ReusableIntention {
    private final RemoveRedundantClassPropertyIntention delegate = new RemoveRedundantClassPropertyIntention();

    @Override
    public Class<?> getDelegateClass() {
        return delegate.getClass();
    }

    @Override
    public void processIntention(@NotNull PsiElement element, @NotNull Project project, Editor editor) throws IncorrectOperationException {
        if (element instanceof GrReferenceExpression ref) {
            ref.replaceWithExpression((GrExpression)ref.getQualifier(), true);
        }
    }

    @NotNull
    @Override
    public PsiElementPredicate getElementPredicate() {
        return (element) -> {
            boolean var10000;
            if (element instanceof GrReferenceExpression ref) {
                if ("class".equals(ref.getReferenceName())) {
                    PsiElement patt1231$temp = ref.getQualifier();
                    if (patt1231$temp instanceof GrReferenceExpression) {
                        GrReferenceExpression qualifier = (GrReferenceExpression)patt1231$temp;
                        if (qualifier.resolve() instanceof PsiClass) {
                            var10000 = true;
                            return var10000;
                        }
                    }
                }
            }

            var10000 = false;
            return var10000;
        };
    }

}