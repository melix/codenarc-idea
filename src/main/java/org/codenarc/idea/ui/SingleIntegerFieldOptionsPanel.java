package org.codenarc.idea.ui;

import com.intellij.ui.JBIntSpinner;
import com.intellij.util.ui.FormBuilder;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codenarc.rule.Rule;
import org.jetbrains.annotations.NonNls;
import org.jspecify.annotations.NullMarked;

import javax.swing.JPanel;
import java.awt.*;

@NullMarked
public class SingleIntegerFieldOptionsPanel extends JPanel {
    public SingleIntegerFieldOptionsPanel(String labelString, final Rule owner, @NonNls final String property) {
        this(labelString, owner, property, 0, 1000, 120);
    }

    public SingleIntegerFieldOptionsPanel(
        String labelString,
        final Rule owner,
        @NonNls final String property,
        int minValue,
        int maxValue,
        int defaultValue
    ) {
        super(new BorderLayout());

        var spinner = createValueField(owner, property, minValue, maxValue, defaultValue);
        var contentPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(labelString, spinner)
            .addComponentFillVertically(new JPanel(), 0)
            .getPanel();
        add(contentPanel, BorderLayout.NORTH);
    }

    public static JBIntSpinner createValueField(
        Rule owner,
        String property,
        int minValue,
        int maxValue,
        int defaultValue
    ) {
        var groovyMetaClass = DefaultGroovyMethods.getMetaClass(owner);
        var underlyingValue = groovyMetaClass.getProperty(owner, property);
        int initialValue = (underlyingValue instanceof Number number)
            ? number.intValue()
            : defaultValue;

        var spinner = new JBIntSpinner(initialValue, minValue, maxValue);
        spinner.addChangeListener(event ->
            groovyMetaClass.setProperty(owner, property, spinner.getNumber())
        );

        return spinner;
    }
}
