package org.sjwimmer.ta4jchart.chartbuilder.converter;

import org.jfree.data.xy.XYSeries;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public class IndicatorToBarDataConverterImpl implements IndicatorToBarDataConverter {

	@Override
	public TacBarDataset convert(Indicator<?> numIndicator, String name) {
		final BarSeries barSeries = numIndicator.getBarSeries();
		if(barSeries.getBarCount() < 2) {
			throw new IllegalArgumentException("Bar series must have at least two entries!");
		}
		final int nbBars = barSeries.getBarCount();
		final XYSeries volumeSeries = new XYSeries(name);
		long minTimeDiff = Long.MAX_VALUE;

		for (int i = 0; i < nbBars; i++) {
			final Bar currentBar = barSeries.getBar(i);
			final long milliseconds = getMilliseconds(currentBar);
			final double value = extractDoubleValue(numIndicator, i);
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
