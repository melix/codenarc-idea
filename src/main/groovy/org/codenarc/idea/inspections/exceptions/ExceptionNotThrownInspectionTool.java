package org.codenarc.idea.inspections.exceptions;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.exceptions.ExceptionNotThrownRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class ExceptionNotThrownInspectionTool extends CodeNarcInspectionTool<ExceptionNotThrownRule> {

    // this code has been generated from org.codenarc.rule.exceptions.ExceptionNotThrownRule

    public static final String GROUP = "Exceptions";

    public ExceptionNotThrownInspectionTool() {
        super(new ExceptionNotThrownRule());
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

    // custom code can be written after this line and it will be preserved during the regeneration

}