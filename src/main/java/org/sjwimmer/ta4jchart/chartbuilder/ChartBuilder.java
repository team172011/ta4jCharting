package org.sjwimmer.ta4jchart.chartbuilder;

import javax.swing.JPanel;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

public interface ChartBuilder {

	JPanel createPlot();

	default void addIndicator(Indicator<Num> indicator){
		addIndicator(indicator, PlotType.OVERLAY, ChartType.LINE);
	}

	default void addIndicator(Indicator<Num> indicator, PlotType plotType){
		addIndicator(indicator, plotType, ChartType.LINE);
	}

	void addIndicator(Indicator<Num> indicator, PlotType plotType, ChartType chartType);

	void setTradingRecord(TradingRecord tradingRecord);
}
