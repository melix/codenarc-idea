package org.codenarc.idea.quickfix;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.intentions.base.PsiElementPredicate;
import org.jetbrains.plugins.groovy.intentions.style.RemoveUnnecessaryReturnIntention;

public class RemoveUnnecessaryReturnReusableIntention extends RemoveUnnecessaryReturnIntention implements ReusableIntention {

    @Override
    public void processIntention(@NotNull PsiElement element, @NotNull Project project, Editor editor) throws IncorrectOperationException {
        super.processIntention(element, project, editor);
    }

    @NotNull
    @Override
    public PsiElementPredicate getElementPredicate() {
        return super.getElementPredicate();
    }

}
