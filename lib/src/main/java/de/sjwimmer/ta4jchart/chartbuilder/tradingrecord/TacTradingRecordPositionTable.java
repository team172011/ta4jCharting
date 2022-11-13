package de.sjwimmer.ta4jchart.chartbuilder.tradingrecord;

import de.sjwimmer.ta4jchart.chartbuilder.TacTable;

import javax.swing.*;

public class TacTradingRecordPositionTable extends JTable implements TacTable {

    public TacTradingRecordPositionTable(TacTradingRecordPositionTableModel tradingTableModel) {
        super(tradingTableModel);
        setDefaultRenderer(String.class, defaultTextRenderer);
    }
}
