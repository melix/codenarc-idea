package org.codenarc.idea.ui;

import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codenarc.rule.Rule;
import org.jetbrains.annotations.NonNls;
import org.jspecify.annotations.NullMarked;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.Objects;

@NullMarked
public class SingleTextFieldOptionsPanel extends JPanel {
    public SingleTextFieldOptionsPanel(
        String label,
        final Rule owner,
        @NonNls final String property
    ) {
        this(label, owner, property, 32);
    }

    public SingleTextFieldOptionsPanel(
        String label,
        Rule owner,
        @NonNls String property,
        int textFieldColumns
    ) {
        super(new BorderLayout());

        var valueField = createValueField(owner, property, textFieldColumns);
        var contentPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(label, valueField)
            .addComponentFillVertically(new JPanel(), 0)
            .getPanel();

        add(contentPanel, BorderLayout.NORTH);
    }

    private static JBTextField createValueField(Rule owner, String property, int textFieldColumns) {
        var groovyMetaClass = DefaultGroovyMethods.getMetaClass(owner);
        var underlyingValue = Objects.requireNonNullElse(groovyMetaClass.getProperty(owner, property), "");

        var field = new JBTextField(textFieldColumns);
        field.setText(underlyingValue.toString());
        field.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent e) {
                groovyMetaClass.setProperty(owner, property, field.getText());
            }
        });

        return field;
    }
}
