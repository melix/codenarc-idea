package org.codenarc.idea.ui

import com.intellij.ui.HyperlinkLabel
import groovy.transform.CompileStatic
import org.codenarc.idea.CodeNarcInspectionTool

import javax.swing.*
import java.awt.*
import java.util.List

@CompileStatic
class Helpers {

    private static final List<String> EXCLUDED_STATIC_FIELDS = Collections.unmodifiableList(
        [
            'name',
            'description',
            'priority',
            'astVisitor',
            'astVisitorClass',
            'violationMessage',
            'compilerPhase',
            'class',
            'enabled',
        ]
    )

    private static final List<String> EXCLUDED_FROM_AUTO_PROXYING_FIELD_NAMES = Collections.unmodifiableList(
        EXCLUDED_STATIC_FIELDS +
        [
            'applyToFilesMatching',
            'doNotApplyToFilesMatching',
            'applyToFileNames',
            'doNotApplyToFileNames'
        ]
    )

    @SuppressWarnings('CodeNarc.FactoryMethodName')
    static JPanel createOptionsPanel(CodeNarcInspectionTool instance) {
        assert instance

        MetaClass ruleMetaClass = instance.rule.metaClass

        if (!ruleMetaClass.properties) {
            return null
        }

        boolean found = false
        int row = 0

        JPanel panel = new JPanel(new GridBagLayout())
        final GridBagConstraints constraints = new GridBagConstraints()
        constraints.gridx = 0
        constraints.gridy = 0
        constraints.weightx = 1.0
        constraints.weighty = 1.0
        constraints.insets.right = 0
        constraints.anchor = GridBagConstraints.BASELINE_LEADING
        constraints.fill = GridBagConstraints.HORIZONTAL

        optionableProps(ruleMetaClass.theClass).each {
            MetaBeanProperty prop = it as MetaBeanProperty

            JPanel subPanel = null
            String sentence = camelCaseToSentence(prop.name)
            if (Boolean ==  prop.type || boolean.class == prop.type) {
                subPanel = new SingleCheckboxOptionsPanel(sentence, instance.rule, prop.name)
            }
            else if (Integer == prop.type || int.class == prop.type) {
                subPanel = new SingleIntegerFieldOptionsPanel(sentence, instance.rule, prop.name)
            }
            else if (String == prop.type) {
                subPanel = new SingleTextFieldOptionsPanel(sentence, instance.rule, prop.name)
            }

            if (subPanel != null) {
                found = true
                panel.add(subPanel, constraints)
                constraints.gridy = row++
            }
        }

        HyperlinkLabel linkLabel = new HyperlinkLabel()

        String fragment = instance.rule.class.simpleName.toLowerCase().replace('rule', '-rule')

        linkLabel.hyperlinkTarget = "https://codenarc.org/codenarc-rules-${instance.ruleset.toLowerCase()}.html#$fragment"
        linkLabel.hyperlinkText = 'An explanation of the rule at the CodeNarc website'

        panel.add(linkLabel, constraints)

        if (found) {
            return panel
        }

        return null
    }

    static String camelCaseToSentence(String camelCased) {
        StringBuilder buf = new StringBuilder(camelCased)
        if (!buf.length()) {
            return camelCased
        }
        buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)))
        for (int i = 1; i < buf.length() - 1; i++) {
            if (
            Character.isLowerCase( buf.charAt(i - 1) ) &&
                Character.isUpperCase( buf.charAt(i) ) &&
                Character.isLowerCase( buf.charAt(i + 1) )
            ) {
                buf.insert(i++, ' ')
                buf.setCharAt(i, Character.toLowerCase(buf.charAt(i)))
            }
        }

        return buf.toString()
    }

    @SuppressWarnings('CodeNarc.Instanceof')
    static List<MetaProperty> proxyableProps(Class clazz) {
        return clazz.metaClass.properties
            .findAll { MetaProperty prop ->
                prop &&
                !EXCLUDED_FROM_AUTO_PROXYING_FIELD_NAMES.contains(prop.name) &&
                ((prop instanceof MetaBeanProperty && ((MetaBeanProperty)prop).setter && ((MetaBeanProperty)prop).getter)) }
            .sort { prop -> prop.name }
    }

    static List<MetaProperty> optionableProps(Class clazz) {
        clazz.metaClass.properties
            .findAll { MetaProperty prop ->
                prop &&
                !EXCLUDED_STATIC_FIELDS.contains(prop.name)  &&
                ((prop instanceof MetaBeanProperty && ((MetaBeanProperty)prop).setter && ((MetaBeanProperty)prop).getter)) }
            .sort { prop -> prop.name }
    }

    static Class<?> getRuleClassInstance(String ruleClass) throws Throwable {
        ClassLoader classLoader = CodeNarcInspectionTool.getClassLoader()
        return Class.forName(ruleClass, true, classLoader)
    }

}
