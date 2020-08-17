package org.sjwimmer.ta4jchart.plotter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;

public class BarSeriesPlotterImpl implements BarSeriesPlotter {

	Map<String, BarSeries> barSeries = new LinkedHashMap<>();
	
	Map<String, List<Number>> openData = new LinkedHashMap<>();
	Map<String, List<Number>> highData = new LinkedHashMap<>();
	Map<String, List<Number>> lowData = new LinkedHashMap<>();
	Map<String, List<Number>> closeData = new LinkedHashMap<>();
	Map<String, List<Date>> dates = new LinkedHashMap<>();

	@Override
	public void addBarSeries(BarSeries barSeries) {
		Objects.requireNonNull(barSeries, "Bar Series cannot be null!");
		Objects.requireNonNull(barSeries.getName(), "Name of BarSeries cannot be null!");
		final String barSeriesName = barSeries.getName();
		this.barSeries.put(barSeriesName, barSeries);
		for(int i = barSeries.getBeginIndex(); i <= barSeries.getEndIndex(); i++) {
			addData(barSeriesName, barSeries.getBar(i));
		}
	}

	private void addData(String barSeriesName, Bar bar) {
		openData.computeIfAbsent(barSeriesName, v -> new ArrayList<>()).add(bar.getClosePrice().getDelegate());
		highData.computeIfAbsent(barSeriesName, v -> new ArrayList<>()).add(bar.getHighPrice().getDelegate());
		lowData.computeIfAbsent(barSeriesName, v -> new ArrayList<>()).add(bar.getLowPrice().getDelegate());
		closeData.computeIfAbsent(barSeriesName, v -> new ArrayList<>()).add(bar.getClosePrice().getDelegate());	
		dates.computeIfAbsent(barSeriesName, v -> new ArrayList<>()).add(Date.from(bar.getEndTime().toInstant()));	
	}


	@Override
	public List<Number> getOpenData(String barSeriesName) {
		return openData.get(barSeriesName);
	}


	@Override
	public List<Number> getLowData(String barSeriesName) {
		return lowData.get(barSeriesName);
	}


	@Override
	public List<Number> getHighData(String barSeriesName) {
		return highData.get(barSeriesName);
	}


	@Override
	public List<Number> getCloseData(String barSeriesName) {
		return closeData.get(barSeriesName);
	}


	@Override
	public List<String> getBarSeriesNames() {
		return new ArrayList<>(barSeries.keySet());
	}


	@Override
	public List<Date> getDates(String barSeriesName) {
		// TODO Auto-generated method stub
		return null;
	}
}
