package org.sjwimmer.ta4jchart.plotter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public class IndicatorPlotterImpl implements IndicatorPlotter<Num> {

	Map<String, Indicator<Num>> indicators = new LinkedHashMap<>();
	Map<String, List<Number>> values = new LinkedHashMap<>();
	Map<String, List<Date>> dates = new LinkedHashMap<>();
	
	@Override
	public void addIndicator(final String indicatorName, Indicator<Num> indicator) {
		Objects.requireNonNull(indicatorName, "indicatorName cannot be null!");
		Objects.requireNonNull(indicator, "Indicator cannot be null!");
		
		final BarSeries barSeries = indicator.getBarSeries();
		this.indicators.put(indicatorName, indicator);
		for(int i = barSeries.getBeginIndex(); i <= barSeries.getEndIndex(); i++) {
			addValues(indicatorName, indicator.getValue(i), barSeries.getBar(i).getEndTime());
		}
	}


	@Override
	public List<Number> getValues(String indicatorName) {
		return this.values.get(indicatorName);
	}

	@Override
	public List<Date> getDates(String indicatorName) {
		return this.dates.get(indicatorName);
	}

	@Override
	public List<String> getIndicatorNames() {
		return new ArrayList<>(this.indicators.keySet());
	}


	private void addValues(String indicatorName, Num value, ZonedDateTime endTime) {
		this.values.computeIfAbsent(indicatorName, v -> new ArrayList<>()).add(value.getDelegate());
		this.dates.computeIfAbsent(indicatorName, v -> new ArrayList<>()).add(Date.from(endTime.toInstant()));
	}
}
