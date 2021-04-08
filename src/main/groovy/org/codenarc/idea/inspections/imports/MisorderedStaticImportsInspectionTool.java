package org.codenarc.idea.inspections.imports;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.imports.MisorderedStaticImportsRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class MisorderedStaticImportsInspectionTool extends CodeNarcInspectionTool<MisorderedStaticImportsRule> {

    // this code has been generated from org.codenarc.rule.imports.MisorderedStaticImportsRule

    public static final String GROUP = "Imports";

    public MisorderedStaticImportsInspectionTool() {
        super(new MisorderedStaticImportsRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setComesBefore(boolean value) {
        getRule().setComesBefore(value);
    }

    public boolean getComesBefore() {
        return getRule().getComesBefore();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}