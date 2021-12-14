package org.sjwimmer.ta4jchart.converter;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

import java.util.Date;

public class IndicatorToTimeSeriesConverterImpl implements IndicatorToTimeSeriesConverter {

	@Override
	public TimeSeriesCollection apply(Indicator<Num> indicator) {
		final TimeSeriesCollection collection = new TimeSeriesCollection();
		final TimeSeries timeSeries = new TimeSeries(this.getName(indicator));
		final BarSeries barSeries = indicator.getBarSeries();
		for(int i = barSeries.getBeginIndex(); i <= barSeries.getEndIndex(); i++) {
			final Bar bar = barSeries.getBar(i);
			timeSeries.add(new Second(new Date(bar.getEndTime().toEpochSecond() * 1000)), indicator.getValue(i).doubleValue());
		}
		collection.addSeries(timeSeries);
		return collection;
	}
}
