package org.codenarc.idea.quickfix;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.plugins.groovy.intentions.base.PsiElementPredicate;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Exposes internal protected methods of {@link org.jetbrains.plugins.groovy.intentions.base.Intention} to allow using
 * it as quick fix.
 */
@NullMarked
public interface ReusableIntention extends IntentionAction {
  Class<?> getDelegateClass();

  void processIntention(
      PsiElement element,
      Project project,
      @Nullable Editor editor
  ) throws IncorrectOperationException;

  PsiElementPredicate getElementPredicate();
}