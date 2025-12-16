package org.codenarc.idea.ui;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.FormBuilder;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codenarc.rule.Rule;
import org.jetbrains.annotations.NonNls;
import org.jspecify.annotations.NullMarked;

import javax.swing.*;
import java.awt.*;

@NullMarked
public class SingleCheckboxOptionsPanel extends JPanel {
    public SingleCheckboxOptionsPanel(String label, Rule owner, @NonNls String property) {
        super(new BorderLayout());

        var groovyMetaClass = DefaultGroovyMethods.getMetaClass(owner);
        var isSelected = (groovyMetaClass.getProperty(owner, property) instanceof Boolean b) && b;

        var checkBox = new JBCheckBox(label, isSelected);
        checkBox.addActionListener(e ->
            groovyMetaClass.setProperty(owner, property, checkBox.isSelected())
        );

        var contentPanel = FormBuilder.createFormBuilder()
            .addComponent(checkBox)
            .addComponentFillVertically(new JPanel(), 0)
            .getPanel();

        add(contentPanel, BorderLayout.NORTH);
    }
}
