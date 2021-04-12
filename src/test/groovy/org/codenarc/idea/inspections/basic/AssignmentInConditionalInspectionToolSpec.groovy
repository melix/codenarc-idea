package org.codenarc.idea.inspections.basic

import com.intellij.codeInspection.LocalInspectionTool
import groovy.transform.CompileDynamic
import org.codenarc.idea.testing.InspectionSpec

@CompileDynamic
class AssignmentInConditionalInspectionToolSpec extends InspectionSpec {

    @Override
    protected LocalInspectionTool createInspection() {
        return new AssignmentInConditionalInspectionTool()
    }

    @Override
    protected Iterable<String> getClassesToInclude() {
        return Collections.singleton(JAVA_LANG_STRING)
    }
}
