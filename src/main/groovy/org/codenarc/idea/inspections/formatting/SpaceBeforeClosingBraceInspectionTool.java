package org.codenarc.idea.inspections.formatting;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.formatting.SpaceBeforeClosingBraceRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class SpaceBeforeClosingBraceInspectionTool extends CodeNarcInspectionTool<SpaceBeforeClosingBraceRule> {

    // this code has been generated from org.codenarc.rule.formatting.SpaceBeforeClosingBraceRule

    public static final String GROUP = "Formatting";

    public SpaceBeforeClosingBraceInspectionTool() {
        super(new SpaceBeforeClosingBraceRule());
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


    public void setCheckClosureMapEntryValue(boolean value) {
        getRule().setCheckClosureMapEntryValue(value);
    }

    public boolean getCheckClosureMapEntryValue() {
        return getRule().getCheckClosureMapEntryValue();
    }


    public void setDoNotApplyToClassNames(String value) {
        getRule().setDoNotApplyToClassNames(value);
    }

    public String getDoNotApplyToClassNames() {
        return getRule().getDoNotApplyToClassNames();
    }


    public void setIgnoreEmptyBlock(boolean value) {
        getRule().setIgnoreEmptyBlock(value);
    }

    public boolean getIgnoreEmptyBlock() {
        return getRule().getIgnoreEmptyBlock();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}