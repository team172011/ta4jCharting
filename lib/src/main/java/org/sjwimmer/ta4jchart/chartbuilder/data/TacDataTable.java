package org.sjwimmer.ta4jchart.chartbuilder.data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.sjwimmer.ta4jchart.chartbuilder.TacTable;

public class TacDataTable extends JTable implements TacTable {

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
