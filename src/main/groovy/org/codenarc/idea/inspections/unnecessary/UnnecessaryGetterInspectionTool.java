package org.codenarc.idea.inspections.unnecessary;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.unnecessary.UnnecessaryGetterRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnnecessaryGetterInspectionTool extends CodeNarcInspectionTool<UnnecessaryGetterRule> {

    // this code has been generated from org.codenarc.rule.unnecessary.UnnecessaryGetterRule

    public static final String GROUP = "Unnecessary";

    public UnnecessaryGetterInspectionTool() {
        super(new UnnecessaryGetterRule());
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


    public void setCheckIsMethods(boolean value) {
        getRule().setCheckIsMethods(value);
    }

    public boolean getCheckIsMethods() {
        return getRule().getCheckIsMethods();
    }


    public void setDoNotApplyToClassNames(String value) {
        getRule().setDoNotApplyToClassNames(value);
    }

    public String getDoNotApplyToClassNames() {
        return getRule().getDoNotApplyToClassNames();
    }


    public void setIgnoreMethodNames(String value) {
        getRule().setIgnoreMethodNames(value);
    }

    public String getIgnoreMethodNames() {
        return getRule().getIgnoreMethodNames();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}