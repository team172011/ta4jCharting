package de.sjwimmer.ta4jchart.chartbuilder;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDateTime;

public interface TacTable {

    TableCellRenderer defaultTextRenderer = (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)  -> {
        JLabel jLabel = new JLabel();
        jLabel.setText(value.toString());
        TacTable.setLabelStyle(row, isSelected, jLabel);
        return jLabel;
    };

    TableCellRenderer dateRenderer = (table, value, isSelected, hasFocus, row, column) ->  {
        JLabel jLabel = new JLabel();
        TacTable.setLabelStyle(row, isSelected, jLabel);
        try {
            jLabel.setText(GlobalConstants.DATE_FORMATTER.valueToString(value));
        } catch (ParseException e) {
            jLabel.setText(value.toString());
        }
        return jLabel;
    };

    TableCellRenderer dateTimeRenderer = (table, value, isSelected, hasFocus, row, column) -> {
        JLabel jLabel = new JLabel(GlobalConstants.DATE_TIME_FORMATTER.format((LocalDateTime) value));
        TacTable.setLabelStyle(row, isSelected, jLabel);
        return jLabel;
    };

    static void setLabelStyle(int row, boolean isSelected, JLabel jLabel) {
        if(!isSelected && row % 2 == 0) {
            jLabel.setBackground(jLabel.getBackground().darker());
            jLabel.setOpaque(true);
        } else if(isSelected) {
            jLabel.setBackground(Color.BLUE);
            jLabel.setOpaque(true);
            jLabel.setForeground(Color.WHITE);
        }
    }
}
