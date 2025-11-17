package org.codenarc.idea.quickfix;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.codenarc.idea.CodeNarcBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.codeInspection.GroovyFix;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrStatement;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.blocks.GrCodeBlock;

import java.util.Arrays;

public class AddExplicitReturnQuickFix extends GroovyFix {

    @Override
    protected void doFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) throws IncorrectOperationException {
        PsiElement psiElement = problemDescriptor.getPsiElement();

        GrCodeBlock body = Arrays.stream(psiElement.getChildren()).filter(e -> e instanceof GrCodeBlock).findFirst().map(GrCodeBlock.class::cast).orElse(null);

        if (body == null) {
            return;
        }

        GrStatement[] statements = body.getStatements();

        if (statements.length == 0) {
            return;
        }

        GrStatement lastStatement = statements[statements.length - 1];

        replaceStatement(lastStatement, "return " + lastStatement.getText());

        System.out.println(lastStatement);
    }

    @Override
    public @IntentionFamilyName @NotNull String getFamilyName() {
        return CodeNarcBundle.message("add.explicit.return");
    }

}