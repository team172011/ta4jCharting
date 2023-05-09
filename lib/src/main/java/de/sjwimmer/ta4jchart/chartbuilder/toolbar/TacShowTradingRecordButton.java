package de.sjwimmer.ta4jchart.chartbuilder.toolbar;

import de.sjwimmer.ta4jchart.chartbuilder.tradingrecord.TradingRecordPanel;
import org.ta4j.core.TradingRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TacShowTradingRecordButton extends JToggleButton implements ActionListener {

    private final JPanel mainPanel;
    private final JPanel table;

    public TacShowTradingRecordButton(TradingRecord tradingRecord, JPanel mainPanel) {
        super("Trading Record Table");
        this.mainPanel = mainPanel;
        if (tradingRecord == null) {
            this.setEnabled(false);
            this.table = new JPanel(new BorderLayout());
        } else {
            this.table = new TradingRecordPanel(tradingRecord);
        }
        setToolTipText("Shows or hides tables with record information about positions and trades");
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
