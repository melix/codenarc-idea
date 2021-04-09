package org.codenarc.idea.inspections.logging

import com.intellij.codeInspection.LocalInspectionTool
import org.codenarc.idea.testing.InspectionSpec

class PrintlnInspectionToolSpec extends InspectionSpec {

    @Override
    protected LocalInspectionTool createInspection() {
        return new PrintlnInspectionTool()
    }

}
