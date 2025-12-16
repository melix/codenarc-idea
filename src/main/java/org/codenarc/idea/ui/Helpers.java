package org.codenarc.idea.ui;

import com.intellij.ui.HyperlinkLabel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import groovy.lang.MetaBeanProperty;
import groovy.lang.MetaProperty;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codenarc.idea.CodeNarcInspectionTool;
import org.jspecify.annotations.NullMarked;

import javax.swing.JPanel;
import java.util.*;
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

    public static JPanel createOptionsPanel(final CodeNarcInspectionTool<?> instance) {
        var rule = instance.getRule();
        var builder = FormBuilder.createFormBuilder();
        for (var it : getOptionableProps(rule.getClass())) {
            if (!(it instanceof MetaBeanProperty prop)) {
                continue;
            }

            var label = camelCaseToSentence(prop.getName());
            var type = prop.getType();

            if (isBoolean(type)) {
                builder.addComponent(new SingleCheckboxOptionsPanel(label, rule, prop.getName()));
            } else if (isInteger(type)) {
                builder.addComponent(new SingleIntegerFieldOptionsPanel(label, rule, prop.getName()));
            } else if (String.class.equals(type)) {
                builder.addComponent(new SingleTextFieldOptionsPanel(label, rule, prop.getName()));
            }
        }

        builder.addVerticalGap(JBUI.scale(10));
        builder.addComponent(createDocumentationLink(instance));

        return builder.addComponentFillVertically(new JPanel(), 0).getPanel();
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

    public static List<MetaProperty> proxyableProps(Class<?> aClass) {
        return DefaultGroovyMethods.getMetaClass(aClass).getProperties().stream()
                .filter(p ->
                    p instanceof MetaBeanProperty _p &&
                    !EXCLUDED_FROM_AUTO_PROXYING_FIELD_NAMES.contains(_p.getName()) &&
                    _p.getSetter() != null &&
                    _p.getGetter() != null
                )
                .sorted(Comparator.comparing(MetaProperty::getName))
                .toList();
    }

    private static List<MetaProperty> getOptionableProps(Class<?> aClass) {
        return DefaultGroovyMethods.getMetaClass(aClass).getProperties().stream()
            .filter(p ->
                p instanceof MetaBeanProperty _p &&
                !EXCLUDED_STATIC_FIELDS.contains(_p.getName()) &&
                _p.getSetter() != null &&
                _p.getGetter() != null
            )
            .sorted(Comparator.comparing(MetaProperty::getName))
            .toList();
    }

    private static HyperlinkLabel createDocumentationLink(CodeNarcInspectionTool<?> instance) {
        var linkLabel = new HyperlinkLabel("An explanation of the rule at the CodeNarc website");
        var ruleName = instance.getRule().getClass().getSimpleName();

        var url = String.format(
            "https://codenarc.org/codenarc-rules-%s.html#%s",
            instance.getRuleset().toLowerCase(Locale.ROOT),
            ruleName.toLowerCase(Locale.ROOT).replace("rule", "-rule")
        );

        linkLabel.setHyperlinkTarget(url);
        return linkLabel;
    }

    private static boolean isBoolean(Class<?> type) {
        return Boolean.class.equals(type) || boolean.class.equals(type);
    }

    private static boolean isInteger(Class<?> type) {
        return Integer.class.equals(type) || int.class.equals(type);
    }
}
