package org.sjwimmer.ta4jchart.chartbuilder.tradingrecord;

import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;

import javax.swing.*;
import java.awt.*;

public class TradingRecordPanel extends JPanel {

    private final TacTradingRecordTradeTable tacTradingRecordTradeTable = new TacTradingRecordTradeTable();

    public TradingRecordPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    private void buildPanel(TradingRecord tradingRecord) {
        add(createPositionsView(tradingRecord));
        add(new JSeparator());
        add(new JScrollPane(tacTradingRecordTradeTable));
        add(new JSeparator());
    }

    private Component createPositionsView(TradingRecord tradingRecord) {
        final TacTradingRecordPositionTableModel tacTradingRecordPositionTableModel = new TacTradingRecordPositionTableModel(tradingRecord);
        final TacTradingRecordPositionTable tacTradingRecordPositionTable = new TacTradingRecordPositionTable(tacTradingRecordPositionTableModel);
        tacTradingRecordPositionTable.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) {
                final int selectedRow = tacTradingRecordPositionTable.getSelectedRow();
                final Position position = tradingRecord.getPositions().get(selectedRow);
                tacTradingRecordTradeTable.setModel(new TacTradingRecordTradeTableModel(position));
            }
        });

        tacTradingRecordPositionTable.setRowSelectionInterval(0,0);
        return new JScrollPane(tacTradingRecordPositionTable);
    }


    public void setTradingRecord(TradingRecord tradingRecord) {
        buildPanel(tradingRecord);
    }
}
