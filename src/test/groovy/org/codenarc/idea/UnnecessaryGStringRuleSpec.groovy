package org.codenarc.idea

import org.codenarc.idea.testing.LightGroovyTestHelper
import spock.lang.AutoCleanup
import spock.lang.Specification

class UnnecessaryGStringRuleSpec extends Specification {

    @AutoCleanup LightGroovyTestHelper helper = LightGroovyTestHelper.groovy25().start {
        enableInspections(new CodeNarcInspectionToolProvider())
        // language=groovy
        configureByText('snippet.groovy', '''
                class Unnecessary {
                    
                    void someMethod() {
                        String something = "Should be single quote"
                    }
                     
                }

            ''')
    }

    void 'check unnecessary gstring'() {
        expect:
            helper.fixture.checkHighlighting()
    }

}
