package org.codenarc.idea.inspections.comments;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.comments.ClassJavadocRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class ClassJavadocInspectionTool extends CodeNarcInspectionTool<ClassJavadocRule> {

    // this code has been generated from org.codenarc.rule.comments.ClassJavadocRule

    public static final String GROUP = "Comments";

    public ClassJavadocInspectionTool() {
        super(new ClassJavadocRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setApplyToNonMainClasses(boolean value) {
        getRule().setApplyToNonMainClasses(value);
    }

    public boolean getApplyToNonMainClasses() {
        return getRule().getApplyToNonMainClasses();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

}