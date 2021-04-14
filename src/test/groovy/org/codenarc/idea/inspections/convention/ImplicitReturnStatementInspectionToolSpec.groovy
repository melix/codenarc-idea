package org.codenarc.idea.inspections.convention

import com.intellij.codeInspection.LocalInspectionTool
import groovy.transform.CompileDynamic
import org.codenarc.idea.testing.InspectionSpec

@CompileDynamic
class ImplicitReturnStatementInspectionToolSpec extends InspectionSpec {

    @Override
    protected LocalInspectionTool createInspection() {
        return new ImplicitReturnStatementInspectionTool()
    }

    @Override
    protected Iterable<String> getClassesToInclude() {
        return Collections.singleton(JAVA_LANG_STRING)
    }

}
