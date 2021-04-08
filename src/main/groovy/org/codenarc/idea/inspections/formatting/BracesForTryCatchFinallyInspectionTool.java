package org.codenarc.idea.inspections.formatting;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.formatting.BracesForTryCatchFinallyRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class BracesForTryCatchFinallyInspectionTool extends CodeNarcInspectionTool<BracesForTryCatchFinallyRule> {

    // this code has been generated from org.codenarc.rule.formatting.BracesForTryCatchFinallyRule

    public static final String GROUP = "Formatting";

    public BracesForTryCatchFinallyInspectionTool() {
        super(new BracesForTryCatchFinallyRule());
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


    public void setCatchOnSameLineAsClosingBrace(Boolean value) {
        getRule().setCatchOnSameLineAsClosingBrace(value);
    }

    public Boolean getCatchOnSameLineAsClosingBrace() {
        return getRule().getCatchOnSameLineAsClosingBrace();
    }


    public void setCatchOnSameLineAsOpeningBrace(Boolean value) {
        getRule().setCatchOnSameLineAsOpeningBrace(value);
    }

    public Boolean getCatchOnSameLineAsOpeningBrace() {
        return getRule().getCatchOnSameLineAsOpeningBrace();
    }


    public void setDoNotApplyToClassNames(String value) {
        getRule().setDoNotApplyToClassNames(value);
    }

    public String getDoNotApplyToClassNames() {
        return getRule().getDoNotApplyToClassNames();
    }


    public void setFinallyOnSameLineAsClosingBrace(Boolean value) {
        getRule().setFinallyOnSameLineAsClosingBrace(value);
    }

    public Boolean getFinallyOnSameLineAsClosingBrace() {
        return getRule().getFinallyOnSameLineAsClosingBrace();
    }


    public void setFinallyOnSameLineAsOpeningBrace(Boolean value) {
        getRule().setFinallyOnSameLineAsOpeningBrace(value);
    }

    public Boolean getFinallyOnSameLineAsOpeningBrace() {
        return getRule().getFinallyOnSameLineAsOpeningBrace();
    }


    public void setSameLine(boolean value) {
        getRule().setSameLine(value);
    }

    public boolean getSameLine() {
        return getRule().getSameLine();
    }


    public void setValidateCatch(boolean value) {
        getRule().setValidateCatch(value);
    }

    public boolean getValidateCatch() {
        return getRule().getValidateCatch();
    }


    public void setValidateFinally(boolean value) {
        getRule().setValidateFinally(value);
    }

    public boolean getValidateFinally() {
        return getRule().getValidateFinally();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}