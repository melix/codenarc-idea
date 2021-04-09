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

    public boolean getCheckList() {
        return getRule().getCheckList();
    }


    public void setCheckMap(boolean value) {
        getRule().setCheckMap(value);
    }

    public boolean getCheckMap() {
        return getRule().getCheckMap();
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

    public boolean getIgnoreSingleElementList() {
        return getRule().getIgnoreSingleElementList();
    }


    public void setIgnoreSingleElementMap(boolean value) {
        getRule().setIgnoreSingleElementMap(value);
    }

    public boolean getIgnoreSingleElementMap() {
        return getRule().getIgnoreSingleElementMap();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    @Override
    protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
        return Collections.emptyList();
    }

}
