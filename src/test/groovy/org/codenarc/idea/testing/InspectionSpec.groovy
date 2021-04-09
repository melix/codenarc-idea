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
import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import org.jetbrains.annotations.NotNull
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

abstract class InspectionSpec extends Specification {

    public static final String JAVA_UTIL_LIST = '''
        package java.util;
        public class List {
            public List sort() { return this }
            public List sort(boolean mutate) { return this }
            public List unique() { return this }
            public List unique(boolean mutate) { return this }
        }
    '''

    private static final String FILE_NAME = 'Snippet.groovy'

    @Shared
    Fixt fixt = Fixt.create(getClass())

    LocalInspectionTool inspection = createInspection()

    @AutoCleanup FixtureHelper helper = FixtureHelper.groovy25().start {
        enableInspections(inspection)

        for (String classToInclude in classesToInclude) {
            addClass(classToInclude)
        }

        configureByText(FILE_NAME, readBeforeFile(it))
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
            helper.fixture.checkResult readAfterFile()
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
            helper.fixture.checkResult readAfterFile()
    }

    protected abstract LocalInspectionTool createInspection()

    protected Iterable<String> getClassesToInclude() {
        return Collections.emptyList()
    }

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

    @SuppressWarnings('TrailingWhitespace')
    @NotNull
    protected String readBeforeFile(JavaCodeInsightTestFixture fixture) {
        String before = fixt.readText('before.txt')

        if (before) {
            return before
        }

        String snippet = fixt.readText(FILE_NAME)

        if (snippet) {
            PsiFile file = fixture.configureByText(FILE_NAME, snippet)
            List<HighlightInfo> infos = fixture.doHighlighting()
            HighlightingDataExtractor data = new HighlightingDataExtractor(
                    fixture.editor.document, true, true, false, false
            )
            String highlighted = data.extractResult(file, infos, snippet)

            fixt.writeText('before.txt', highlighted)

            throw new IllegalStateException('Highlighted file before.txt has been created. Please, check its contents')
        }

        fixt.writeText(FILE_NAME, """
        package rules
        
        /**
         * Class to demonstrate code violations. Files <code>before.txt</code> and <code>fixed.txt</code>
         * are only generated when missing so you need to delete these files if you want to regenerated them.
         */
        class ${getClass().name}Snippet {
        
            void doSomething() {
                // TODO: write offending code
            }

        }
        """.stripIndent().trim())

        throw new IllegalStateException('Source Snippet.groovy file has been created. Please, add the violating code')
    }

    protected String readAfterFile() {
        String after = fixt.readText('fixed.txt')

        if (after) {
            return after
        }

        fixt.writeText('fixed.txt', helper.fixture.file.text)

        throw new IllegalStateException('Verification file fixed.txt has been created. Please, check its contents')
    }

}
