package org.codenarc.idea.inspections.imports;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.imports.UnnecessaryGroovyImportRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class UnnecessaryGroovyImportInspectionTool extends CodeNarcInspectionTool<UnnecessaryGroovyImportRule> {

    // this code has been generated from org.codenarc.rule.imports.UnnecessaryGroovyImportRule

    public static final String GROUP = "Imports";

    public UnnecessaryGroovyImportInspectionTool() {
        super(new UnnecessaryGroovyImportRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}