package org.codenarc.idea.ui

import com.intellij.ui.HyperlinkLabel
import com.intellij.ui.components.labels.LinkLabel
import cucumber.api.java.en_scouse.An
import groovy.transform.CompileStatic
import org.codenarc.idea.CodeNarcComponent
import org.codenarc.idea.CodeNarcInspectionTool
import org.codenarc.rule.Rule

import javax.swing.*
import java.awt.*
import java.lang.reflect.Modifier
import java.util.List

@CompileStatic
class Helpers {

    private static final List<String> excludedFieldNames = Collections.unmodifiableList(
        [
            'name',
            'description',
            'priority',
            'astVisitor',
            'astVisitorClass',
            'violationMessage',
            'compilerPhase',
            'class',
            'enabled'
        ]
    )
    private static final List<String> excludedFromAutoproxyingFieldNames = Collections.unmodifiableList(
        excludedFieldNames +
        [
            'applyToFilesMatching',
            'doNotApplyToFilesMatching',
            'applyToFileNames',
            'doNotApplyToFileNames'
        ]
    )

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
            if (Boolean.class ==  prop.type || boolean.class == prop.type) {
                subPanel = new SingleCheckboxOptionsPanel(sentence, instance.rule, prop.name)
            }
            else if (Integer.class == prop.type || int.class == prop.type) {
                subPanel = new SingleIntegerFieldOptionsPanel(sentence, instance.rule, prop.name)
            }
            else if (String.class == prop.type) {
                subPanel = new SingleTextFieldOptionsPanel(sentence, instance.rule, prop.name)
            }

            if (subPanel != null) {
                found = true
                panel.add(subPanel, constraints)
                constraints.gridy = row++
            }
        }

        HyperlinkLabel linkLabel = new HyperlinkLabel();
        linkLabel.setHyperlinkTarget("http://codenarc.sourceforge.net/codenarc-rules-${instance.getRuleset().toLowerCase()}.html#${instance.shortName}")
        linkLabel.setHyperlinkText('An explanation of the rule at the CodeNarc website')
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
        for (int i=1; i < buf.length()-1; i++) {
            if (
            Character.isLowerCase( buf.charAt(i-1) ) &&
                Character.isUpperCase( buf.charAt(i) ) &&
                Character.isLowerCase( buf.charAt(i+1) )
            ) {
                buf.insert(i++, ' ')
                buf.setCharAt(i, Character.toLowerCase(buf.charAt(i)))
            }
        }

        return buf.toString()
    }

    static List<MetaProperty> proxyableProps(Class clazz) {
        clazz.metaClass.properties
            .findAll { MetaProperty prop ->
                prop &&
                !excludedFromAutoproxyingFieldNames.contains(prop.name) &&
                ((prop instanceof MetaBeanProperty && ((MetaBeanProperty)prop).setter && ((MetaBeanProperty)prop).getter)) }
            .sort { prop -> prop.name }
    }

    static List<MetaProperty> optionableProps(Class clazz) {
        clazz.metaClass.properties
            .findAll { MetaProperty prop ->
                prop &&
                !excludedFieldNames.contains(prop.name)  &&
                ((prop instanceof MetaBeanProperty && ((MetaBeanProperty)prop).setter && ((MetaBeanProperty)prop).getter)) }
            .sort { prop -> prop.name }
    }

    static Class<?> getRuleClassInstance(String ruleClass) throws Throwable {
        ClassLoader classLoader = CodeNarcInspectionTool.class.getClassLoader()
        return Class.forName(ruleClass, true, classLoader)
    }

}
