package org.sjwimmer.ta4jchart.plotter;

import java.util.Date;
import java.util.List;

import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public interface IndicatorPlotter<N extends Num> extends Plotter{
	
	void addIndicator(String name, Indicator<N> indicator);
	
	List<Number> getValues(String indicatorName);
	
	List<Date> getDates(String indicatorName);
	
	List<String> getIndicatorNames();
}
