package org.codenarc.idea.inspections.groovyism;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.groovyism.AssignCollectionSortRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class AssignCollectionSortInspectionTool extends CodeNarcInspectionTool<AssignCollectionSortRule> {

    // this code has been generated from org.codenarc.rule.groovyism.AssignCollectionSortRule

    public static final String GROUP = "Groovyism";

    public AssignCollectionSortInspectionTool() {
        super(new AssignCollectionSortRule());
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