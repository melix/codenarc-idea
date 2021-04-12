package org.codenarc.idea.inspections.unnecessary

import com.intellij.codeInspection.LocalInspectionTool
import groovy.transform.CompileDynamic
import org.codenarc.idea.testing.InspectionSpec

@CompileDynamic
class UnnecessaryDotClassInspectionToolSpec extends InspectionSpec {

    @Override
    protected LocalInspectionTool createInspection() {
        return new UnnecessaryDotClassInspectionTool()
    }

    @Override
    protected Iterable<String> getClassesToInclude() {
        return [
                JAVA_LANG_STRING
        ]
    }
}
