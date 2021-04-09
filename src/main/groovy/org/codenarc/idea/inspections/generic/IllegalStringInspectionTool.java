package org.codenarc.idea.inspections.generic;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.generic.IllegalStringRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class IllegalStringInspectionTool extends CodeNarcInspectionTool<IllegalStringRule> {

    // this code has been generated from org.codenarc.rule.generic.IllegalStringRule

    public static final String GROUP = "Generic";

    public IllegalStringInspectionTool() {
        super(new IllegalStringRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setString(String value) {
        getRule().setString(value);
    }

    public String getString() {
        return getRule().getString();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}