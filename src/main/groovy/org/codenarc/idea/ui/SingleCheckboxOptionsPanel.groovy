package org.codenarc.idea.ui

import groovy.transform.CompileStatic
import org.codenarc.rule.Rule
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull

import javax.swing.ButtonModel
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import java.awt.GridBagConstraints
import java.awt.GridBagLayout

@CompileStatic
class SingleCheckboxOptionsPanel extends JPanel {

    SingleCheckboxOptionsPanel(@NotNull String label,
                                      @NotNull Rule owner,
                                      @NonNls String property) {
        super(new GridBagLayout())
        final boolean selected = owner.metaClass.getProperty(owner, property)
        final JCheckBox checkBox = new JCheckBox(label, selected)
        final ButtonModel model = checkBox.getModel()
        final SingleCheckboxChangeListener listener = new SingleCheckboxChangeListener(owner, property, model)
        model.addChangeListener(listener)

        checkBox.enabled = true

        final GridBagConstraints constraints = new GridBagConstraints()
        constraints.gridx = 0
        constraints.gridy = 0
        constraints.weightx = 1.0
        constraints.weighty = 1.0
        constraints.anchor = GridBagConstraints.FIRST_LINE_START
        constraints.fill = GridBagConstraints.HORIZONTAL
        add(checkBox, constraints)
    }

    private static class SingleCheckboxChangeListener
        implements ChangeListener {

        private final Rule owner
        private final String property
        private final ButtonModel model

        SingleCheckboxChangeListener(Rule owner, String property, ButtonModel model) {
            this.owner = owner
            this.property = property
            this.model = model
        }

        @Override
        void stateChanged(ChangeEvent e) {
            owner.metaClass.setProperty(owner, property, model.selected)
        }
    }

}
