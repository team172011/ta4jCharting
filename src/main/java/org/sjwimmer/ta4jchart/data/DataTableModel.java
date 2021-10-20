package org.sjwimmer.ta4jchart.data;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

public class DataTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 7271440542755921838L;

	private final Map<String, List<String>> data = new LinkedHashMap<>();
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	public void addEntries(String columnName, List<String> values) {
		this.data.put(columnName, values);
	}

	public void addEntries(OHLCDataset xyDataset) {
		final List<String> values = new ArrayList<>();
		final List<String> dates = new ArrayList<>();
		for (int i = 0; i < xyDataset.getItemCount(0); i++) {
			final Number close = xyDataset.getClose(0, i);
			final long x = xyDataset.getX(0, i).longValue();
			final java.time.LocalDateTime date = Instant.ofEpochMilli(x).atZone(ZoneId.systemDefault()).toLocalDateTime();
			values.add(String.valueOf(close));
			dates.add(DATE_TIME_FORMATTER.format(date));
		}
		this.addEntries("Date", dates);
		this.addEntries("Close", values);
	}

	public void addEntries(TimeSeriesCollection timeSeriesCollection){
		final List<String> values = new ArrayList<>();
		for(int i = 0; i < timeSeriesCollection.getSeries(0).getItemCount(); i ++){
			Number value = timeSeriesCollection.getSeries(0).getValue(i);
			values.add(String.valueOf(value));
		}
		this.addEntries(timeSeriesCollection.getSeriesKey(0).toString(), values);
	}
	
	@Override
	public int getRowCount() {
		Optional<List<String>> entry = data.values().stream().findAny();
		return entry.map(List::size).orElse(0);
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
