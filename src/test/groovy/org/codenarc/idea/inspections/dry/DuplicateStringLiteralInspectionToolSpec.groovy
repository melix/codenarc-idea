package org.codenarc.idea.inspections.dry

import com.intellij.codeInspection.LocalInspectionTool
import org.codenarc.idea.testing.InspectionSpec

class DuplicateStringLiteralInspectionToolSpec extends InspectionSpec {

    @Override
    protected LocalInspectionTool createInspection() {
        return new DuplicateStringLiteralInspectionTool()
    }

    @Override
    protected Iterable<String> getClassesToInclude() {
        return Collections.singleton(JAVA_LANG_STRING)
    }

}
