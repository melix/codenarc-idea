package org.codenarc.idea.inspections.unnecessary

import com.intellij.codeInspection.LocalInspectionTool
import groovy.transform.CompileDynamic
import org.codenarc.idea.testing.InspectionSpec

@CompileDynamic
class UnnecessaryGStringInspectionToolSpec extends InspectionSpec {

    @Override
    protected LocalInspectionTool createInspection() {
        return new UnnecessaryGStringInspectionTool()
    }

}
