package rules

/**
 * Class to demonstrate code violations. Files <code>before.txt</code> and <code>fixed.txt</code>
 * are only generated when missing so you need to delete these files if you want to regenerated them.
 */
class DuplicateStringLiteralInspectionToolSpecSnippet {

    void doSomething() {
        String one = 'same'
        String two = 'same'
        String three = 'other'
        String four = 'other'
    }

}
