package org.sjwimmer.ta4jchart.chartbuilder;

import javax.swing.JPanel;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.Strategy;
import org.ta4j.core.num.Num;

public interface ChartBuilder {

	JPanel createPlot();

	void addBarSeries(BarSeries barSeries);

	void addIndicator(String name, Indicator<Num> indicator);

	void addStrategy(Strategy strategy);

	

}
