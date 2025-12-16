package org.codenarc.idea.ui;

import com.intellij.ui.HyperlinkLabel;
import groovy.lang.MetaBeanProperty;
import groovy.lang.MetaClass;
import groovy.lang.MetaProperty;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NullMarked
public class Helpers {
    private static final List<String> EXCLUDED_STATIC_FIELDS = List.of(
        "name", "description", "priority", "astVisitor", "astVisitorClass", "violationMessage", "compilerPhase",
        "class", "enabled"
    );

    private static final List<String> EXCLUDED_FROM_AUTO_PROXYING_FIELD_NAMES = Stream.of(
            EXCLUDED_STATIC_FIELDS,
            List.of("applyToFilesMatching", "doNotApplyToFilesMatching", "applyToFileNames", "doNotApplyToFileNames")
    ).flatMap(List::stream).toList();

    public static @Nullable JPanel createOptionsPanel(final CodeNarcInspectionTool<?> instance) {
        MetaClass ruleMetaClass = DefaultGroovyMethods.getMetaClass(instance.getRule());

        if (ruleMetaClass.getProperties().isEmpty()) {
            return null;
        }

        boolean found = false;
        int row = 0;

        JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets.right = 0;
        constraints.anchor = GridBagConstraints.BASELINE_LEADING;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        for (MetaProperty it : optionableProps(ruleMetaClass.getTheClass())) {
            MetaBeanProperty prop = (MetaBeanProperty) it;

            JPanel subPanel = null;
            String sentence = camelCaseToSentence(prop.getName());
            if (Boolean.class.equals(prop.getType()) || boolean.class.equals(prop.getType())) {
                subPanel = new SingleCheckboxOptionsPanel(sentence, instance.getRule(), prop.getName());
            } else if (Integer.class.equals(prop.getType()) || int.class.equals(prop.getType())) {
                subPanel = new SingleIntegerFieldOptionsPanel(sentence, instance.getRule(), prop.getName());
            } else if (String.class.equals(prop.getType())) {
                subPanel = new SingleTextFieldOptionsPanel(sentence, instance.getRule(), prop.getName());
            }


            if (subPanel != null) {
                found = true;
                panel.add(subPanel, constraints);
                constraints.gridy = row++;
            }
        }

        HyperlinkLabel linkLabel = new HyperlinkLabel();

        String fragment = instance.getRule().getClass().getSimpleName()
            .toLowerCase(Locale.ROOT)
            .replace("rule", "-rule");

        linkLabel.setHyperlinkTarget(
            "https://codenarc.org/codenarc-rules-" + instance.getRuleset().toLowerCase(Locale.ROOT) + ".html#" +
                fragment
        );
        linkLabel.setHyperlinkText("An explanation of the rule at the CodeNarc website");

        panel.add(linkLabel, constraints);

        if (found) {
            return panel;
        }

        return null;
    }

    public static String camelCaseToSentence(String camelCased) {
        if (camelCased.isBlank()) {
            return camelCased;
        }

        StringBuilder buf = new StringBuilder(camelCased);
        buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
        for (int i = 1; i < buf.length() - 1; i++) {
            if (
                Character.isLowerCase(buf.charAt(i - 1)) &&
                Character.isUpperCase(buf.charAt(i)) &&
                Character.isLowerCase(buf.charAt(i + 1))
            ) {
                buf.insert(i++, " ");
                buf.setCharAt(i, Character.toLowerCase(buf.charAt(i)));
            }
        }

        return buf.toString();
    }

    public static List<MetaProperty> proxyableProps(Class<?> clazz) {
        return DefaultGroovyMethods.getMetaClass(clazz).getProperties().stream()
                .filter(p ->
                    p instanceof MetaBeanProperty _p &&
                    !EXCLUDED_FROM_AUTO_PROXYING_FIELD_NAMES.contains(_p.getName()) &&
                    _p.getSetter() != null &&
                    _p.getGetter() != null
                )
                .sorted(Comparator.comparing(MetaProperty::getName))
                .toList();
    }

    public static List<MetaProperty> optionableProps(Class<?> clazz) {
        return DefaultGroovyMethods.getMetaClass(clazz).getProperties().stream()
                .filter(p ->
                    p instanceof MetaBeanProperty _p &&
                    !EXCLUDED_STATIC_FIELDS.contains(_p.getName()) &&
                    _p.getSetter() != null &&
                    _p.getGetter() != null
                )
                .sorted(Comparator.comparing(MetaProperty::getName))
                .collect(Collectors.toList());
    }

    public static Class<?> getRuleClassInstance(String ruleClass) throws Throwable {
        ClassLoader classLoader = CodeNarcInspectionTool.class.getClassLoader();
        return Class.forName(ruleClass, true, classLoader);
    }
}
