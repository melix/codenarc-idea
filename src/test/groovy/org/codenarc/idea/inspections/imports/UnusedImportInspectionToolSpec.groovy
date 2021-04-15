package org.codenarc.idea.inspections.imports

import com.intellij.codeInspection.LocalInspectionTool
import groovy.transform.CompileDynamic
import org.codenarc.idea.testing.InspectionSpec

@CompileDynamic
class UnusedImportInspectionToolSpec extends InspectionSpec {

    @Override
    protected LocalInspectionTool createInspection() {
        return new UnusedImportInspectionTool()
    }

    @Override
    protected Iterable<String> getClassesToInclude() {
        return [JAVA_LANG_STRING, JAVA_UTIL_LIST]
    }

}
