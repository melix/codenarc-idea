package org.codenarc.idea.inspections.formatting;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.formatting.BracesForMethodRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class BracesForMethodInspectionTool extends CodeNarcInspectionTool<BracesForMethodRule> {

    // this code has been generated from org.codenarc.rule.formatting.BracesForMethodRule

    public static final String GROUP = "Formatting";

    public BracesForMethodInspectionTool() {
        super(new BracesForMethodRule());
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


    public void setSameLine(boolean value) {
        getRule().setSameLine(value);
    }

    public boolean getSameLine() {
        return getRule().getSameLine();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}