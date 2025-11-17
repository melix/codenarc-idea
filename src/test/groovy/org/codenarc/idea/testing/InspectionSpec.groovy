// codenarc-disable
package org.codenarc.idea.testing

import com.agorapulse.testing.fixt.Fixt
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.intention.EmptyIntentionAction
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.actions.CleanupInspectionIntention
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.WriteIntentReadAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import groovy.transform.CompileDynamic
import org.jetbrains.annotations.NotNull
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@CompileDynamic
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

    public static final String JAVA_LANG_STRING = '''
        package java.lang;
        public class String { }
    '''

    public static final String COM_EXAMPLE_ONE = '''
        package com.example;
        public class One { }
    '''

    public static final String COM_EXAMPLE_TWO = '''
        package com.example;
        public class Two { }
    '''

    public static final String ORG_EXAMPLE_THREE = '''
        package org.example;
        public class Three { }
    '''

    public static final String ORG_EXAMPLE_FOUR = '''
        package org.example;
        public class Four { }
    '''

    @Shared
    Fixt fixt = Fixt.create(getClass())

    LocalInspectionTool inspection = createInspection()

    String fileName =  "${UUID.randomUUID()}.groovy"

    @AutoCleanup FixtureHelper helper = FixtureHelper.groovy40().start { JavaCodeInsightTestFixture fixture ->
        enableInspections(inspection)

        configure(fixture)

        for (String classToInclude in classesToInclude) {
            addClass(classToInclude)
        }

        configureByText(fileName, readBeforeFile(fixture))
    }

    void 'check highlighting and then apply fix one by one'() {
        expect:
            checkHighlighting()
        when:
            List<IntentionAction> fixes = relevantFixes
        then:
        fixes.size() == 2

        when:
            applyFix fixes.first()
            applyFix fixes.last()
        then:
            checkResult()
    }

    void 'check highlighting and then apply fix all'() {
        expect:
            checkHighlighting()
        when:
            List<IntentionAction> fixes = relevantFixes
        then:
            fixes.size() == 2

        when:
            triggerFirstFixAll()
        then:
            checkResult()
    }

    protected boolean checkHighlighting() {
        try {
            helper.fixture.checkHighlighting()
            commitAndSave()
            return true
        } catch (org.junit.ComparisonFailure comparisonFailure) {
            assert comparisonFailure.actual == comparisonFailure.expected
            return false
        }
    }

    protected boolean checkResult() {
        try {
            helper.fixture.checkResult readAfterFile()
            return true
        } catch (junit.framework.ComparisonFailure comparisonFailure) {
            assert comparisonFailure.actual == comparisonFailure.expected
            return false
        }
    }

    @SuppressWarnings('FactoryMethodName')
    protected abstract LocalInspectionTool createInspection()

    protected Iterable<String> getClassesToInclude() {
        return Collections.emptyList()
    }

    @SuppressWarnings(['ImplicitClosureParameter', 'Instanceof'])
    protected List<IntentionAction> getRelevantFixes() {
        return helper.fixture.getAllQuickFixes(fileName).findAll { !(it instanceof EmptyIntentionAction) }
    }

    @SuppressWarnings('EmptyMethodInAbstractClass')
    protected void configure(JavaCodeInsightTestFixture fixture) {
        // implemented in subclasses
    }

    protected String readFile(String file) {
        String content = fixt.readText(file)
        if (!content) {
            throw new IllegalArgumentException("Content for $file is missing. Please, create the file in the fixtures directory.")
        }
        return content
    }

    protected void triggerFirstFixAll() {
        final highlightInfoList = helper.fixture.doHighlighting()
        HighlightInfo info = highlightInfoList.find { it.findRegisteredQuickFix { descriptor, textRange ->
          var toolId = descriptor.getToolId()
          if (toolId != null && toolId.startsWith('CodeNarc.')) {
            return descriptor;
          } else {
            return null;
          }
        }}

        assert info, 'There is a highlighted warning element'

        IntentionAction action = null

        ApplicationManager.application.runReadAction {
            HighlightInfo.IntentionActionDescriptor intentionActionDescriptor = info
              .findRegisteredQuickFix { descriptor, textRange ->
              return descriptor
              }
            PsiElement someElement = helper.fixture.file.findElementAt(0)

            assert someElement, 'There is an element for the highlighted info'
            List<IntentionAction> options = intentionActionDescriptor.getOptions(someElement, helper.fixture.editor)

            assert options, 'Options present for the highlighted info'

            action = options.find { it instanceof CleanupInspectionIntention }
        }

        assert action, 'Clean up action is present'

        applyFix action
    }

    void applyFix(IntentionAction action) {
      helper.fixture.launchAction(action)
      commitAndSave()
    }

    void commitAndSave() {
      ApplicationManager.application.invokeAndWait {
        WriteIntentReadAction.run {
          PsiDocumentManager.getInstance(helper.fixture.project).commitAllDocuments()
          FileDocumentManager.getInstance().saveAllDocuments()
        }
      }
    }

    @SuppressWarnings('TrailingWhitespace')
    @NotNull
    protected String readBeforeFile(JavaCodeInsightTestFixture fixture) {
        String before = fixt.readText('before.txt')

        if (before) {
            return before
        }

        fixt.writeText('before.txt', """
        package rules
        
        /**
         * Class to demonstrate code violations. Files <code>before.txt</code> and <code>fixed.txt</code>
         * are only generated when missing so you need to delete these files if you want to regenerated them.
         */
        class ${getClass().simpleName}Snippet {
        
            void doSomething() {
                // TODO: write offending code
            }

        }
        """.stripIndent().trim())

        throw new IllegalStateException('Source before.txt file has been created. Please, add the violating code')
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
