package org.codenarc.idea.quickfix;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.intentions.base.PsiElementPredicate;

/**
 * Exposes internal protected methods of {@link org.jetbrains.plugins.groovy.intentions.base.Intention} to allow using it as quick fix.
 */
public interface ReusableIntention extends IntentionAction {
  Class<?> getDelegateClass();

  void processIntention(@NotNull PsiElement element, @NotNull Project project, Editor editor) throws IncorrectOperationException;

  @NotNull
  PsiElementPredicate getElementPredicate();

}