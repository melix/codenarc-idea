package org.codenarc.idea.inspections.naming;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.naming.ClassNameSameAsFilenameRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class ClassNameSameAsFilenameInspectionTool extends CodeNarcInspectionTool<ClassNameSameAsFilenameRule> {

    // this code has been generated from org.codenarc.rule.naming.ClassNameSameAsFilenameRule

    public static final String GROUP = "Naming";

    public ClassNameSameAsFilenameInspectionTool() {
        super(new ClassNameSameAsFilenameRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}