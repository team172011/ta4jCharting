package org.sjwimmer.ta4jchart.chartbuilder.tradingrecord;

import org.ta4j.core.Position;
import org.ta4j.core.Trade;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Table model for a TradingRecord
 */
public class TacTradingRecordTradeTableModel extends AbstractTableModel {

    private final Position position;

    private final List<String> columnNames = new ArrayList<>();
    private final Map<Integer, Function<Trade, Object>> columnFunctions = new HashMap<>();

    {
        columnNames.add("Type");
        columnNames.add("Index");
        columnNames.add("Amount");
        columnNames.add("Cost");
        columnNames.add("Net Price");
        columnNames.add("Value");
        columnNames.add("Price Per Asset");

        columnFunctions.put(0, t -> String.format("%s", t.getType()));
        columnFunctions.put(1, t -> String.format("%s", t.getIndex()));
        columnFunctions.put(2, t -> String.format("%.2f", t.getAmount().doubleValue()));
        columnFunctions.put(3, t -> String.format("%.2f", t.getCost().doubleValue()));
        columnFunctions.put(4, t -> String.format("%.2f", t.getNetPrice().doubleValue()));
        columnFunctions.put(5, t -> String.format("%.2f", t.getValue().doubleValue()));
        columnFunctions.put(6, t -> String.format("%.2f", t.getPricePerAsset().doubleValue()));
    }

    public TacTradingRecordTradeTableModel(Position position) {
        this.position = position;
        assert columnFunctions.size() == columnNames.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public int getRowCount() {
        if(position == null) {
            return 0;
        }
        return 2;
    }

    @Override
    public int getColumnCount() {
        return columnFunctions.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final Trade currentTrade;
        if(rowIndex == 0) {
            currentTrade = position.getEntry();
        } else {
            currentTrade = position.getExit();
        }
        return columnFunctions.getOrDefault(columnIndex, pIn -> "Position " + rowIndex ).apply(currentTrade);
    }

    public Position getPosition() {
        return position;
    }

    public void setColumFunction(int column, Function<Trade, Object> function) {
        columnFunctions.put(column, function);
    }
}
