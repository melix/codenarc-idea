package org.codenarc.idea.inspections.groovyism

import com.intellij.codeInspection.LocalInspectionTool
import groovy.transform.CompileStatic
import org.codenarc.idea.testing.InspectionSpec

@CompileStatic
class AssignCollectionUniqueInspectionToolSpec extends InspectionSpec {

    @Override
    protected LocalInspectionTool createInspection() {
        return new AssignCollectionUniqueInspectionTool()
    }

    @Override
    protected Iterable<String> getClassesToInclude() {
        return Collections.singleton(JAVA_UTIL_LIST)
    }

}
