package org.codenarc.idea.inspections.groovyism

import com.intellij.codeInspection.LocalInspectionTool
import org.codenarc.idea.testing.InspectionSpec

class AssignCollectionSortInspectionToolSpec extends InspectionSpec {

    @Override
    protected LocalInspectionTool createInspection() {
        return new AssignCollectionSortInspectionTool()
    }

    @Override
    protected Iterable<String> getClassesToInclude() {
        return Collections.singleton(JAVA_UTIL_LIST)
    }

}
