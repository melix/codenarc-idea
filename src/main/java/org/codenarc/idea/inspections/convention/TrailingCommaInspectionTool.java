package org.codenarc.idea.inspections.convention;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.psi.PsiElement;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.Generated;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.Violation;
import org.codenarc.rule.convention.TrailingCommaRule;
import org.jetbrains.annotations.NotNull;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class TrailingCommaInspectionTool extends CodeNarcInspectionTool<TrailingCommaRule> {

    // this code has been generated from org.codenarc.rule.convention.TrailingCommaRule

    public static final String GROUP = "Convention";

    public TrailingCommaInspectionTool() {
        super(new TrailingCommaRule());
        applyDefaultConfiguration(getRule());
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


    public void setCheckList(boolean value) {
        getRule().setCheckList(value);
    }

    public boolean isCheckList() {
        return getRule().isCheckList();
    }


    public void setCheckMap(boolean value) {
        getRule().setCheckMap(value);
    }

    public boolean isCheckMap() {
        return getRule().isCheckMap();
    }


    public void setDoNotApplyToClassNames(String value) {
        getRule().setDoNotApplyToClassNames(value);
    }

    public String getDoNotApplyToClassNames() {
        return getRule().getDoNotApplyToClassNames();
    }


    public void setIgnoreSingleElementList(boolean value) {
        getRule().setIgnoreSingleElementList(value);
    }

    public boolean isIgnoreSingleElementList() {
        return getRule().isIgnoreSingleElementList();
    }


    public void setIgnoreSingleElementMap(boolean value) {
        getRule().setIgnoreSingleElementMap(value);
    }

    public boolean isIgnoreSingleElementMap() {
        return getRule().isIgnoreSingleElementMap();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
