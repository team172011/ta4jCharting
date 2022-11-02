package de.sjwimmer.ta4jchart.chartbuilder.toolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TacShowDataButton extends JToggleButton implements ActionListener {

    private final JPanel dataPanel;
    private final JPanel mainPanel;

    public TacShowDataButton(JPanel dataPanel, JPanel mainPanel) {
        super("Show Data");
        this.dataPanel = dataPanel;
        this.mainPanel = mainPanel;
        setToolTipText("Shows or hides the data table showing a table for prices and indicator values");
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isSelected()) {
            this.mainPanel.add(dataPanel, BorderLayout.EAST);
        } else {
            this.mainPanel.remove(dataPanel);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
