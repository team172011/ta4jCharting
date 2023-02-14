package de.sjwimmer.ta4jchart.chartbuilder.data;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataTableModelTest {

    @Test
    public void testDataModel() {
        final String columnName = "Test";
        final List<Object> columnValues = Arrays.asList("A", "B", "C", "1", "2", "3");
        TacDataTableModel dataTableModel = new TacDataTableModel();
        dataTableModel.addEntries(columnName, columnValues);

        assertEquals(columnName, dataTableModel.getColumnName(0));
        assertEquals(1, dataTableModel.getColumnCount());
        assertEquals(Object.class, dataTableModel.getColumnClass(0));

        for(int i = 0; i < columnValues.size(); i++){
            assertEquals(columnValues.get(i), dataTableModel.getValueAt(i,0));
        }
    }
}