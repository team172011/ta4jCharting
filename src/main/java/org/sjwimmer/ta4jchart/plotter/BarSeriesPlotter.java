package org.sjwimmer.ta4jchart.plotter;

import java.util.Date;
import java.util.List;

import org.ta4j.core.BarSeries;

public interface BarSeriesPlotter extends Plotter{
	
	void addBarSeries(BarSeries barSeries);
	List<String> getBarSeriesNames();
	
	List<Date> getDates(String barSeriesName);
	List<Number> getOpenData(String barSeriesName);
	List<Number> getLowData(String barSeriesName);
	List<Number> getHighData(String barSeriesName);
	List<Number> getCloseData(String barSeriesName);
}
