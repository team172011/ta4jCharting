package org.sjwimmer.ta4jchart.chartbuilder.tradingrecord;

import org.sjwimmer.ta4jchart.chartbuilder.TacTable;

import javax.swing.*;

public class TacTradingRecordTradeTable extends JTable implements TacTable {

    public TacTradingRecordTradeTable() {
        setDefaultRenderer(String.class, defaultTextRenderer);
    }
}
