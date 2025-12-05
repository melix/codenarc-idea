package org.codenarc.idea.ui;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codenarc.rule.Rule;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class SingleCheckboxOptionsPanel extends JPanel {

    public SingleCheckboxOptionsPanel(@NotNull String label, @NotNull Rule owner, @NonNls String property) {
        super(new GridBagLayout());
        final Boolean selected = (Boolean) DefaultGroovyMethods.getMetaClass(owner).getProperty(owner, property);
        final JCheckBox checkBox = new JCheckBox(label, selected != null && selected);
        final ButtonModel model = checkBox.getModel();
        final SingleCheckboxChangeListener listener = new SingleCheckboxChangeListener(owner, property, model);
        model.addChangeListener(listener);

        checkBox.setEnabled(true);

        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(checkBox, constraints);
    }

    private static class SingleCheckboxChangeListener implements ChangeListener {

        private final Rule owner;
        private final String property;
        private final ButtonModel model;

        public SingleCheckboxChangeListener(Rule owner, String property, ButtonModel model) {
            this.owner = owner;
            this.property = property;
            this.model = model;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            DefaultGroovyMethods.getMetaClass(owner).setProperty(owner, property, model.isSelected());
        }
    }
}
