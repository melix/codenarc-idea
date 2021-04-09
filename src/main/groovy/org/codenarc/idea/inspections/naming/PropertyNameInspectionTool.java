package org.codenarc.idea.inspections.naming;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.naming.PropertyNameRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class PropertyNameInspectionTool extends CodeNarcInspectionTool<PropertyNameRule> {

    // this code has been generated from org.codenarc.rule.naming.PropertyNameRule

    public static final String GROUP = "Naming";

    public PropertyNameInspectionTool() {
        super(new PropertyNameRule());
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


    public void setFinalRegex(String value) {
        getRule().setFinalRegex(value);
    }

    public String getFinalRegex() {
        return getRule().getFinalRegex();
    }


    public void setIgnorePropertyNames(String value) {
        getRule().setIgnorePropertyNames(value);
    }

    public String getIgnorePropertyNames() {
        return getRule().getIgnorePropertyNames();
    }


    public void setRegex(String value) {
        getRule().setRegex(value);
    }

    public String getRegex() {
        return getRule().getRegex();
    }


    public void setStaticFinalRegex(String value) {
        getRule().setStaticFinalRegex(value);
    }

    public String getStaticFinalRegex() {
        return getRule().getStaticFinalRegex();
    }


    public void setStaticRegex(String value) {
        getRule().setStaticRegex(value);
    }

    public String getStaticRegex() {
        return getRule().getStaticRegex();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}