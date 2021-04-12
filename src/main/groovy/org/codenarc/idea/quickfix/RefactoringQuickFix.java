package org.codenarc.idea.quickfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiEditorUtil;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.actions.BaseRefactoringAction;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.SlowOperations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.codeInspection.GroovyFix;

import java.util.function.Function;

public class RefactoringQuickFix extends GroovyFix {

    public static LocalQuickFix from(String familyName, Function<DataContext, RefactoringActionHandler> handlerCreator) {
        return new RefactoringQuickFix(familyName, handlerCreator);
    }

    private final Function<DataContext, RefactoringActionHandler> handlerFunction;
    private final String familyName;

    private RefactoringQuickFix(String familyName, Function<DataContext, RefactoringActionHandler> handlerFunction) {
        this.familyName = familyName;
        this.handlerFunction = handlerFunction;
    }

    @Override
    protected void doFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) throws IncorrectOperationException {

        SimpleDataContext.Builder contextBuilder = SimpleDataContext.builder();

        contextBuilder
                .add(CommonDataKeys.PROJECT, project)
                .add(CommonDataKeys.PSI_FILE, descriptor.getPsiElement().getContainingFile())
                .add(CommonDataKeys.PSI_ELEMENT, descriptor.getPsiElement())
                .add(LangDataKeys.CONTEXT_LANGUAGES, new Language[]{descriptor.getPsiElement().getLanguage()})
                .add(LangDataKeys.LANGUAGE, descriptor.getPsiElement().getLanguage());

        Editor editor = PsiEditorUtil.findEditor(descriptor.getPsiElement());

        if (editor != null) {
            contextBuilder
                    .add(CommonDataKeys.EDITOR, editor)
                    .add(CommonDataKeys.CARET, editor.getCaretModel().getPrimaryCaret());
        }

        refactor(project, contextBuilder.build());
    }

    @Override
    public @NotNull String getFamilyName() {
        return familyName;
    }

    private void refactor(Project project, DataContext dataContext) {
        if (project == null) return;

        SlowOperations.allowSlowOperations(() -> BaseRefactoringAction.performRefactoringAction(project, dataContext, handlerFunction.apply(dataContext)));
    }

}
