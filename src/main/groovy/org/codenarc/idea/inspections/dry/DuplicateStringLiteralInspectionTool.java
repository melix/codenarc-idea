package org.codenarc.idea.inspections.dry;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.idea.quickfix.RefactoringQuickFix;
import org.codenarc.rule.Violation;
import org.codenarc.rule.dry.DuplicateStringLiteralRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.GroovyBundle;
import org.jetbrains.plugins.groovy.refactoring.introduce.GrIntroduceContext;
import org.jetbrains.plugins.groovy.refactoring.introduce.GrIntroduceDialog;
import org.jetbrains.plugins.groovy.refactoring.introduce.constant.GrIntroduceConstantHandler;
import org.jetbrains.plugins.groovy.refactoring.introduce.constant.GrIntroduceConstantSettings;

import javax.annotation.Generated;
import java.util.Collection;
import java.util.Collections;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class DuplicateStringLiteralInspectionTool extends CodeNarcInspectionTool<DuplicateStringLiteralRule> {

    // this code has been generated from org.codenarc.rule.dry.DuplicateStringLiteralRule

    public static final String GROUP = "Dry";

    public DuplicateStringLiteralInspectionTool() {
        super(new DuplicateStringLiteralRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setApplyToClassNames(String value) {
        getRule().setApplyToClassNames(value);
    }

    public String getApplyToClassNames() {
        return getRule().getApplyToClassNames();
    }


    public void setDoNotApplyToClassNames(String value) {
        getRule().setDoNotApplyToClassNames(value);
    }

    public String getDoNotApplyToClassNames() {
        return getRule().getDoNotApplyToClassNames();
    }


    public void setDuplicateStringMinimumLength(Integer value) {
        getRule().setDuplicateStringMinimumLength(value);
    }

    public Integer getDuplicateStringMinimumLength() {
        return getRule().getDuplicateStringMinimumLength();
    }


    public void setIgnoreStrings(String value) {
        getRule().setIgnoreStrings(value);
    }

    public String getIgnoreStrings() {
        return getRule().getIgnoreStrings();
    }


    public void setIgnoreStringsDelimiter(char value) {
        getRule().setIgnoreStringsDelimiter(value);
    }

    public char getIgnoreStringsDelimiter() {
        return getRule().getIgnoreStringsDelimiter();
    }

    // custom code can be written after this line and it will be preserved during the regeneration


    @Override
    protected void applyDefaultConfiguration(DuplicateStringLiteralRule rule) {
        rule.setDoNotApplyToFilesMatching(SPECIFICATION_FILENAMES);
    }

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.singleton(RefactoringQuickFix.from(GroovyBundle.message("introduce.constant.title"), ctx -> new GrIntroduceConstantHandler() {
            @Override
            protected @NotNull GrIntroduceDialog<GrIntroduceConstantSettings> getDialog(@NotNull GrIntroduceContext context) {
                return super.getDialog(context);
            }
        }));
    }

}
