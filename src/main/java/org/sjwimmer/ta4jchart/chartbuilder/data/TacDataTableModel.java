package org.sjwimmer.ta4jchart.chartbuilder.data;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYSeries;
import org.sjwimmer.ta4jchart.chartbuilder.converter.TacBarDataset;

import javax.swing.table.AbstractTableModel;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class TacDataTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 7271440542755921838L;

	private final List<LocalDateTime> dates = new ArrayList<java.time.LocalDateTime>();
	private final List<Number> closes = new ArrayList<>();

	private final Map<String, List<Object>> data = new LinkedHashMap<>();

	public void addEntries(String columnName, List<Object> values) {
		this.data.put(columnName, values);
	}

	public void addEntries(TacBarDataset barDataset) {
		final List<Object> values = new ArrayList<>();
		XYSeries series = barDataset.getSeries(0);
		for(int i = 0; i < series.getItemCount(); i++) {
			values.add(String.format("%.3f", series.getX(i).doubleValue()));
		}
		this.addEntries(barDataset.getSeriesKey(0).toString(), values);
	}

	public void addEntries(OHLCDataset xyDataset) {
		for (int i = 0; i < xyDataset.getItemCount(0); i++) {
			final Number close = xyDataset.getClose(0, i);
			final long x = xyDataset.getX(0, i).longValue();
			final java.time.LocalDateTime date = Instant.ofEpochMilli(x).atZone(ZoneId.systemDefault()).toLocalDateTime();
			this.closes.add(close);
			this.dates.add(date);
		}
	}

	public void addEntries(TimeSeriesCollection timeSeriesCollection){
		final List<Object> values = new ArrayList<>();
		for(int i = 0; i < timeSeriesCollection.getSeries(0).getItemCount(); i ++){
			Number value = timeSeriesCollection.getSeries(0).getValue(i);
			values.add(String.format("%.3f", value.doubleValue()));
		}
		this.addEntries(timeSeriesCollection.getSeriesKey(0).toString(), values);
	}
	
	@Override
	public int getRowCount() {
		Optional<List<Object>> entry = data.values().stream().findAny();
		return entry.map(List::size).orElse(0);
	}

	@Override
    public String getColumnName(int col) {
		if(col == 0){
			return "Date";
		} else if(col == 1) {
			return "Close";
		}
        return data.keySet().toArray()[col].toString();
    }
    
	@Override
	public int getColumnCount() {
		return data.keySet().size();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex == 0){
			return dates.get(rowIndex);
		} else if(columnIndex == 1) {
			return closes.get(rowIndex);
		}
		String columnName = getColumnName(columnIndex);
		return data.get(columnName).get(rowIndex);
	}

}
