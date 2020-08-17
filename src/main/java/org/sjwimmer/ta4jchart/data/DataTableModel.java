package org.sjwimmer.ta4jchart.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.table.AbstractTableModel;

public class DataTableModel extends AbstractTableModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7271440542755921838L;
	
	private final Map<String, List<Number>> data = new LinkedHashMap<>();


	public void addEntry(String columnName, List<Number> values) {
		this.data.put(columnName, values);
	}
	
	@Override
	public int getRowCount() {
		Optional<List<Number>> entry = data.values().stream().findAny();
		if(entry.isPresent()) {
			return entry.get().size();
		}
		return 0;
	}

	@Override
    public String getColumnName(int col) {
        return data.keySet().toArray()[col].toString();
    }
    
	@Override
	public int getColumnCount() {
		return data.keySet().toArray().length;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String columnName = getColumnName(columnIndex);
		return data.get(columnName).get(rowIndex);
	}

}
