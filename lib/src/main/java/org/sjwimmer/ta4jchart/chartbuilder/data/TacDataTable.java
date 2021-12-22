package org.sjwimmer.ta4jchart.chartbuilder.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sjwimmer.ta4jchart.chartbuilder.TacTable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TacDataTable extends JTable implements TacTable {

    private final static Logger log = LogManager.getLogger(TacDataTable.class);
    private final Map<Class<?>, TableCellRenderer> rendererMap = new HashMap<>();

    {
        rendererMap.put(Date.class, dateRenderer);
        rendererMap.put(LocalDateTime.class, dateTimeRenderer);
    }

    public TacDataTable(TacDataTableModel dataTableModel) {
        super(dataTableModel);
        setDefaultRenderer(Object.class, (table, value, isSelected, hasFocus, row, column) ->
                { // wrap in proxy render, getDefaultRenderer always requests renderer for Object.class
                    return rendererMap.computeIfAbsent(value.getClass(), i -> defaultTextRenderer)
                        .getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
        );
    }
}
