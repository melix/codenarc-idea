package org.codenarc.idea.quickfix;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.codenarc.idea.CodeNarcBundle;
import org.jetbrains.plugins.groovy.codeInspection.GroovyFix;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrStatement;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ReplaceStatementFix extends GroovyFix {
    private final Class<? extends GrStatement> target;
    private final String original;
    private final String replacement;

    public ReplaceStatementFix(Class<? extends GrStatement> target, String original, String replacement) {
        this.target = target;
        this.original = original;
        this.replacement = replacement;
    }

    @Override
    public String getFamilyName() {
        return CodeNarcBundle.message("replace.text", original, replacement);
    }

    @Override
    protected void doFix(Project project, ProblemDescriptor descriptor) throws IncorrectOperationException {
        replaceCode(descriptor.getPsiElement());
    }

    private boolean replaceCode(PsiElement element) {
        if (target.isInstance(element)) {
            replaceCode((GrStatement) element, element.getText());
            return true;
        }

        for (PsiElement child : element.getChildren()) {
            if (replaceCode(child)) {
                return true;
            }
        }

        return false;
    }

    private void replaceCode(GrStatement statement, String text) {
        replaceStatement(statement, text.replace(original, replacement));
    }
}