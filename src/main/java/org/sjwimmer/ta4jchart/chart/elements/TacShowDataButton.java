package org.sjwimmer.ta4jchart.chart.elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TacShowDataButton extends JToggleButton implements ActionListener {

    private final JPanel component;
    private final JComponent table;

    public TacShowDataButton(JTable table, JPanel component) {
        super("Show Data");
        this.component = component;
        this.table = new JScrollPane(table);

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isSelected()) {
            this.component.add(table, BorderLayout.EAST);
        } else {
            this.component.remove(table);
        }
        component.revalidate();
        component.repaint();
    }
}
