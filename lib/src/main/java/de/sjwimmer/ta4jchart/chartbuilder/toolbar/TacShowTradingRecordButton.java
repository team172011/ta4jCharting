package de.sjwimmer.ta4jchart.chartbuilder.toolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TacShowTradingRecordButton extends JToggleButton implements ActionListener {

    private final JPanel mainPanel;
    private final JPanel table;

    public TacShowTradingRecordButton(JPanel tradingRecordPanel, JPanel mainPanel) {
        super("Show Trading Record");
        this.mainPanel = mainPanel;
        this.table = tradingRecordPanel;
        setToolTipText("Shows or hides tables that show table record information like positions or trades");
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isSelected()) {
            mainPanel.add(table, BorderLayout.WEST);
        } else {
            mainPanel.remove(table);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
