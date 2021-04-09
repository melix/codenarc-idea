package org.codenarc.idea.inspections.naming;

import javax.annotation.Generated;

import org.codenarc.idea.CodeNarcInspectionTool;
import org.codenarc.rule.naming.PackageNameMatchesFilePathRule;

@Generated("You can customize this class at the end of the file or remove this annotation to skip regeneration completely")
public class PackageNameMatchesFilePathInspectionTool extends CodeNarcInspectionTool<PackageNameMatchesFilePathRule> {

    // this code has been generated from org.codenarc.rule.naming.PackageNameMatchesFilePathRule

    public static final String GROUP = "Naming";

    public PackageNameMatchesFilePathInspectionTool() {
        super(new PackageNameMatchesFilePathRule());
    }

    @Override
    public String getRuleset() {
        return GROUP;
    }


    public void setGroupId(String value) {
        getRule().setGroupId(value);
    }

    public String getGroupId() {
        return getRule().getGroupId();
    }

    // custom code can be written after this line and it will be preserved during the regeneration

    // @Override
    // protected @NotNull Collection<LocalQuickFix> getQuickFixesFor(Violation violation, PsiElement violatingElement) {
    //     return Collections.singleton(myfix);
    // }
}