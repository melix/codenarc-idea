package org.codenarc.idea.testing

import com.agorapulse.testing.fixt.Fixt
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.actions.CleanupInspectionIntention
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Pair
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

abstract class InspectionSpec extends Specification {

    private static final String FILE_NAME = 'Snippet.groovy'

    @Shared
    Fixt fixt = Fixt.create(getClass())

    LocalInspectionTool inspection = createInspection()

    @AutoCleanup FixtureHelper helper = FixtureHelper.groovy25().start {
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
            triggerFirstFixAll()
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

    protected void triggerFirstFixAll() {
        HighlightInfo info = helper.fixture.doHighlighting().find { it.quickFixActionRanges }
        assert info, 'There is a highlighted warning element'

        IntentionAction action = null

        ApplicationManager.application.runReadAction {
            Pair<HighlightInfo.IntentionActionDescriptor, TextRange> marker = info.quickFixActionRanges[0]
            PsiElement someElement = helper.fixture.file.findElementAt(0)

            assert someElement, 'There is an element for the highlighted info'
            List<IntentionAction> options = marker.first.getOptions(someElement, helper.fixture.editor)

            assert options, 'Options present for the highlighted info'

            action = options.find { it instanceof CleanupInspectionIntention }
        }

        assert action, 'Clean up action is present'

        helper.fixture.launchAction action
    }

}
