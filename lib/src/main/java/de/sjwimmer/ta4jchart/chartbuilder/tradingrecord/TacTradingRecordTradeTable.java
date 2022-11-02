package de.sjwimmer.ta4jchart.chartbuilder.tradingrecord;

import de.sjwimmer.ta4jchart.chartbuilder.TacTable;

import javax.swing.*;
import javax.swing.table.TableModel;

public class TacTradingRecordTradeTable extends JTable implements TacTable {

    public TacTradingRecordTradeTable(TableModel model) {
        super(model);
        setDefaultRenderer(String.class, defaultTextRenderer);
    }
}
