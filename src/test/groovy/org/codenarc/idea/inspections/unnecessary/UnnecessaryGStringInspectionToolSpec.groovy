package org.codenarc.idea.inspections.unnecessary

import com.intellij.codeInspection.LocalInspectionTool
import org.codenarc.idea.testing.InspectionSpec

class UnnecessaryGStringInspectionToolSpec extends InspectionSpec {

    @Override
    protected LocalInspectionTool createInspection() {
        return new UnnecessaryGStringInspectionTool()
    }

}
