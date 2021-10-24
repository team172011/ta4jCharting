package org.sjwimmer.ta4jchart.data;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataTableModelTest {

    @Test
    public void testDataModel() {
        final String columnName = "Test";
        final List<String> columnValues = Arrays.asList("A", "B", "C", "1", "2", "3");
        DataTableModel dataTableModel = new DataTableModel();
        dataTableModel.addEntries(columnName, columnValues);

        assertEquals(columnName, dataTableModel.getColumnName(0));
        assertEquals(1, dataTableModel.getColumnCount());
        assertEquals(Object.class, dataTableModel.getColumnClass(0));

        for(var i = 0; i < columnValues.size(); i++){
            assertEquals(columnValues.get(i), dataTableModel.getValueAt(i,0));
        }
    }
}