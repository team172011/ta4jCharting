package de.sjwimmer.ta4jchart.chartbuilder.tradingrecord;

import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;

import javax.swing.*;
import java.awt.*;

public class TradingRecordPanel extends JPanel {

    private TacTradingRecordTradeTable tacTradingRecordTradeTable;
    private final TradingRecord tradingRecord;
    public TradingRecordPanel(TradingRecord tradingRecord) {
        super();
        this.tradingRecord = tradingRecord;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(UIManager.getColor("Button.background"));
        add(createPositionsView(tradingRecord));
        add(new JScrollPane(tacTradingRecordTradeTable));
    }

    private Component createPositionsView(TradingRecord tradingRecord) {
        final TacTradingRecordPositionTableModel tacTradingRecordPositionTableModel = new TacTradingRecordPositionTableModel(tradingRecord);
        final TacTradingRecordPositionTable tacTradingRecordPositionTable = new TacTradingRecordPositionTable(tacTradingRecordPositionTableModel);
        tacTradingRecordPositionTable.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) {
                final int selectedRow = tacTradingRecordPositionTable.getSelectedRow();
                final Position position = tradingRecord.getPositions().get(selectedRow);
                final TacTradingRecordTradeTableModel tacTradingRecordTradeTableModel = new TacTradingRecordTradeTableModel(position);
                if(this.tacTradingRecordTradeTable == null) {
                    this.tacTradingRecordTradeTable = new TacTradingRecordTradeTable(tacTradingRecordTradeTableModel);
                } else {
                    this.tacTradingRecordTradeTable.setModel(tacTradingRecordTradeTableModel);
                }
            }
        });
        if(tradingRecord.getPositionCount() > 0) {
            tacTradingRecordPositionTable.setRowSelectionInterval(0,0);
        }
        return new JScrollPane(tacTradingRecordPositionTable);
    }
}
