package org.codenarc.idea.testing

import com.agorapulse.testing.fixt.Fixt
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.IntentionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

abstract class InspectionSpec extends Specification {

    private static final String FILE_NAME = 'Snippet.groovy'

    @Shared
    Fixt fixt = Fixt.create(getClass())

    LocalInspectionTool inspection = createInspection()

    @AutoCleanup LightGroovyTestHelper helper = LightGroovyTestHelper.groovy25().start {
        enableInspections(inspection)
        configureByText(FILE_NAME, readFile('before.txt'))
    }

    void 'check highlighting and then apply fix one by one'() {
        expect:
            helper.fixture.checkHighlighting()
        when:
            List<IntentionAction> fixes = helper.fixture.getAllQuickFixes(FILE_NAME)
        then:
            fixes.size() == 2

        when:
            helper.fixture.launchAction fixes.first()
            helper.fixture.launchAction fixes.last()
        then:
            helper.fixture.checkResult readFile('fixed.txt')
    }

    void 'check highlighting and then apply fix all'() {
        expect:
            helper.fixture.checkHighlighting()
        when:
            List<IntentionAction> fixes = helper.fixture.getAllQuickFixes(FILE_NAME)
        then:
            fixes.size() == 2

        when:
            helper.fixture.launchAction IntentionManager.instance.createFixAllIntention(
                    new LocalInspectionToolWrapper(inspection),
                    fixes.first()
            )
        then:
            helper.fixture.checkResult readFile('fixed.txt')
    }

    protected abstract LocalInspectionTool createInspection()

    protected String readFile(String file) {
        String content = fixt.readText(file)
        if (!content) {
            throw new IllegalArgumentException("Content for $file is missing. Please, create the file in the fixtures directory.")
        }
        return content
    }

}
