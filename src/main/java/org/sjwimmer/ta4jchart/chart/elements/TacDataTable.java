package org.sjwimmer.ta4jchart.chart.elements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sjwimmer.ta4jchart.chart.GlobalConstants;
import org.sjwimmer.ta4jchart.chart.elements.data.DataTableModel;
import org.ta4j.core.Trade;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

public class TacDataTable extends JTable {

    private final Logger log = LogManager.getLogger(TacDataTable.class);

    public TacDataTable(DataTableModel dataTableModel) {
        super(dataTableModel);
        getColumnModel().getColumn(0).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            try {
                if(value instanceof Date) {
                    return new JTextField(GlobalConstants.DATE_FORMATTER.valueToString((Date) value));
                } else if (value instanceof LocalDateTime) {
                    return new JTextField(GlobalConstants.DATE_TIME_FORMATTER.format((LocalDateTime)value));
                }
            } catch (ParseException e) {
                log.error("", e);
            }
            return new JTextField(value.toString());
        });


        setDefaultRenderer(Object.class, (table, value, isSelected, hasFocus, row, column) -> {
            if (value instanceof Number) {
                return new JTextField(value.toString());
            } else if (value instanceof Trade) {
                Trade trade = (Trade) value;
                JTextField tradeInfo = new JTextField();
                if(trade.isBuy()) {
                    tradeInfo.setText("BUY");
                    tradeInfo.setBackground(Color.GREEN);
                } else {
                    tradeInfo.setText("SEL");
                    tradeInfo.setBackground(Color.RED);
                }
                return tradeInfo;
            }
            else {
                return new JTextField(value.toString());
            }
        });
    }
}
