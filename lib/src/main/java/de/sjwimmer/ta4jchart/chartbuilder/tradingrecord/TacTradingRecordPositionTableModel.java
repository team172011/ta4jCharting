package de.sjwimmer.ta4jchart.chartbuilder.tradingrecord;

import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Table model for a TradingRecord
 */
public class TacTradingRecordPositionTableModel extends AbstractTableModel {

    private final TradingRecord tradingRecord;

    private final List<String> columnNames = new ArrayList<>();
    private final Map<Integer, Function<Position, Object>> columnFunctions = new HashMap<>();

    {
        columnNames.add("Position");
        columnNames.add("Holding Cost");
        columnNames.add("PositionCost");
        columnNames.add("Gross Profit");
        columnNames.add("Gross Return");
        columnNames.add("Profit");

        columnFunctions.put(1, p -> String.format("%.2f", p.getHoldingCost().doubleValue()));
        columnFunctions.put(2, p -> String.format("%.2f", p.getPositionCost().doubleValue()));
        columnFunctions.put(3, p -> String.format("%.2f", p.getGrossProfit().doubleValue()));
        columnFunctions.put(4, p -> String.format("%.2f", p.getGrossReturn().doubleValue()));
        columnFunctions.put(5, p -> String.format("%.2f", p.getProfit().doubleValue()));
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public TacTradingRecordPositionTableModel(TradingRecord tradingRecord) {
        this.tradingRecord = tradingRecord;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public int getRowCount() {
        if(tradingRecord == null) {
            return 0;
        }
        return tradingRecord.getPositionCount();
    }

    @Override
    public int getColumnCount() {
        return columnFunctions.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Position currentPosition = tradingRecord.getPositions().get(rowIndex);
        return columnFunctions.getOrDefault(columnIndex, pIn -> "Position " + rowIndex ).apply(currentPosition);
    }

    public TradingRecord getTradingRecord() {
        return tradingRecord;
    }

    public void setColumFunction(int column, Function<Position, Object> function) {
        columnFunctions.put(column, function);
    }
}
