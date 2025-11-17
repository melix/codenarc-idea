package org.codenarc.idea.quickfix;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.intentions.base.Intention;
import org.jetbrains.plugins.groovy.intentions.base.PsiElementPredicate;
import org.jetbrains.plugins.groovy.intentions.conversions.strings.ConvertGStringToStringIntention;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral;
import org.jetbrains.plugins.groovy.lang.psi.impl.PsiImplUtil;

public class ConvertGStringToStringReusableIntention extends Intention implements ReusableIntention {
  private final ConvertGStringToStringIntention delegate = new ConvertGStringToStringIntention();

  @Override
  public Class<?> getDelegateClass() {
    return delegate.getClass();
  }

  @Override
  public void processIntention(@NotNull PsiElement element, @NotNull Project project, Editor editor) throws IncorrectOperationException {
    final GrLiteral exp = (GrLiteral)element;
    PsiImplUtil.replaceExpression(ConvertGStringToStringIntention.convertGStringLiteralToStringLiteral(exp), exp);
  }

  @NotNull
  @Override
  public PsiElementPredicate getElementPredicate() {
    return delegate.getElementPredicate();
  }
}