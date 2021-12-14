package org.sjwimmer.ta4jchart.converter;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.sjwimmer.ta4jchart.chart.dataset.TacBarDataset;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

import java.util.Date;

public class IndicatorToBarDataConverterImpl implements IndicatorToBarDataConverter {

	@Override
	public TacBarDataset apply(Indicator<Num> numIndicator) {
		final BarSeries barSeries = numIndicator.getBarSeries();
		if(barSeries.getBarCount() < 2) {
			throw new IllegalArgumentException("Bar series must have at least two entries!");
		}
		final int nbBars = barSeries.getBarCount();
		final XYSeries volumeSeries = new XYSeries(getName(numIndicator));
		long minTimeDiff = Long.MAX_VALUE;

		for (int i = 0; i < nbBars; i++) {
			final Bar currentBar = barSeries.getBar(i);
			final long milliseconds = getMilliseconds(currentBar);
			final double value = numIndicator.getValue(i).doubleValue();
			volumeSeries.add(milliseconds, value);

			if(i > 0) {
				Bar prevBar = barSeries.getBar(i -1);
				long prevBarMilliseconds = getMilliseconds(prevBar);
				long timeDiff = milliseconds-prevBarMilliseconds;
				if (minTimeDiff > timeDiff) {
					minTimeDiff = timeDiff;
				}
			}
		}

		return new TacBarDataset(volumeSeries, minTimeDiff);
	}
}
