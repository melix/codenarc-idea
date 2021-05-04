package org.codenarc.idea.inspections.imports

import com.intellij.codeInspection.LocalInspectionTool
import groovy.transform.CompileDynamic
import org.codenarc.idea.testing.InspectionSpec

@CompileDynamic
class NoWildcardImportsInspectionToolSpec extends InspectionSpec {

    @Override
    protected LocalInspectionTool createInspection() {
        return new NoWildcardImportsInspectionTool()
    }

    @Override
    protected Iterable<String> getClassesToInclude() {
        return [COM_EXAMPLE_ONE, COM_EXAMPLE_TWO, ORG_EXAMPLE_THREE, ORG_EXAMPLE_FOUR]
    }
}
