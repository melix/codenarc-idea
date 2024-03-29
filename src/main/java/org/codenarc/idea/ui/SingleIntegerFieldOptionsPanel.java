package org.codenarc.idea.ui;

import com.intellij.ui.DocumentAdapter;
import com.intellij.util.ui.UIUtil;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codenarc.rule.Rule;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.Document;
import javax.swing.text.NumberFormatter;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.text.ParseException;

public class SingleIntegerFieldOptionsPanel extends JPanel {
    public SingleIntegerFieldOptionsPanel(String labelString, final Rule owner, @NonNls final String property) {
        this(labelString, owner, property, 6);
    }

    public SingleIntegerFieldOptionsPanel(String labelString, final Rule owner, @NonNls final String property, int integerFieldColumns) {
        super(new GridBagLayout());
        final JLabel label = new JLabel(labelString);
        final JFormattedTextField valueField = createIntegerFieldTrackingValue(owner, property, integerFieldColumns);
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets.right = UIUtil.DEFAULT_HGAP;
        constraints.weightx = 0.0;
        constraints.anchor = GridBagConstraints.BASELINE_LEADING;
        constraints.fill = GridBagConstraints.NONE;
        add(label, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets.right = 0;
        constraints.anchor = GridBagConstraints.BASELINE_LEADING;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(valueField, constraints);
    }

    public static JFormattedTextField createIntegerFieldTrackingValue(@NotNull Rule owner, @NotNull String property, int integerFieldColumns) {
        JFormattedTextField valueField = new JFormattedTextField();
        valueField.setEnabled(true);
        valueField.setColumns(integerFieldColumns);
        setupIntegerFieldTrackingValue(valueField, owner, property);
        return valueField;
    }

    /**
     * Sets integer number format to JFormattedTextField instance,
     * sets value of JFormattedTextField instance to object's field value,
     * synchronizes object's field value with the value of JFormattedTextField instance.
     *
     * @param textField JFormattedTextField instance
     * @param owner     an object whose field is synchronized with {@code textField}
     * @param property  object's field name for synchronization
     */
    public static void setupIntegerFieldTrackingValue(final JFormattedTextField textField, final Rule owner, final String property) {
        NumberFormat formatter = NumberFormat.getIntegerInstance();
        formatter.setParseIntegerOnly(true);
        textField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(formatter)));
        textField.setValue(DefaultGroovyMethods.getMetaClass(owner).getProperty(owner, property));
        final Document document = textField.getDocument();
        document.addDocumentListener(new DocumentAdapter() {
            @Override
            public void textChanged(@NotNull DocumentEvent e) {
                try {
                    textField.commitEdit();
                } catch (ParseException ex) {
                    throw new IllegalArgumentException(ex);
                }
                DefaultGroovyMethods.getMetaClass(owner).setProperty(owner, property, ((Number) textField.getValue()).intValue());
            }

        });
    }

}
