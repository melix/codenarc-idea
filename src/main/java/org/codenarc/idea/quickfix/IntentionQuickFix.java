package org.codenarc.idea.quickfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiEditorUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.plugins.groovy.codeInspection.GroovyFix;
import org.jetbrains.plugins.groovy.intentions.GroovyIntentionsBundle;
import org.jetbrains.plugins.groovy.intentions.base.PsiElementPredicate;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class IntentionQuickFix extends GroovyFix {
  public static LocalQuickFix from(ReusableIntention action) {
    return new IntentionQuickFix(action);
  }

  private final ReusableIntention intention;

  private IntentionQuickFix(ReusableIntention intention) {
    this.intention = intention;
  }

  @Override
  @NonNull
  public String getFamilyName() {
    return GroovyIntentionsBundle.message(getPrefix(intention.getDelegateClass()) + ".family.name");
  }

  @Override
  protected void doFix(@NonNull Project project, @NonNull ProblemDescriptor descriptor) throws IncorrectOperationException {
    Editor editor = PsiEditorUtil.findEditor(descriptor.getPsiElement());
    callOnProblemDescriptor(descriptor.getPsiElement(), project, editor);
  }

  private static @Nullable PsiElement findMatchingElement(PsiElement violatingElement, PsiElementPredicate predicate) {
    if (predicate.satisfiedBy(violatingElement)) {
      return violatingElement;
    }
    for (PsiElement child : violatingElement.getChildren()) {
      PsiElement theElement = findMatchingElement(child, predicate);
      if (theElement != null) {
        return theElement;
      }
    }
    return null;
  }

  private void callOnProblemDescriptor(@NonNull PsiElement element, @NonNull Project project, Editor editor) {
    if (element instanceof PsiFile el) {
      // single call
      intention.invoke(project, editor, el);
      return;
    }

    PsiFile file = element.getContainingFile();

    if (!element.isValid()) {
      return;
    }

    // requires a reflective call to processIntention otherwise Fix All does not work
    PsiElementPredicate predicate = intention.getElementPredicate();
    PsiElement found = findMatchingElement(element, predicate);

    if (found == null) {
      // invoke original intention
      intention.invoke(project, editor, file);
      return;
    }

    intention.processIntention(found, project, editor);
  }

  static String getPrefix(Class<?> aClass) {
    final String name = aClass.getSimpleName();
    final StringBuilder buffer = new StringBuilder(name.length() + 10);
    buffer.append(Character.toLowerCase(name.charAt(0)));
    for (int i = 1; i < name.length(); i++) {
      final char c = name.charAt(i);
      if (Character.isUpperCase(c)) {
        buffer.append('.');
        buffer.append(Character.toLowerCase(c));
      }
      else {
        buffer.append(c);
      }
    }
    return buffer.toString();
  }
}