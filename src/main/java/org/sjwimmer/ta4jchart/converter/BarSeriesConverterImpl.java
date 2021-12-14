package org.sjwimmer.ta4jchart.converter;
import java.util.*;

import org.jfree.data.xy.*;
import org.sjwimmer.ta4jchart.chart.dataset.TacBarDataset;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.num.Num;

public class BarSeriesConverterImpl implements BarSeriesConverter {

	@Override
	public DefaultHighLowDataset apply(BarSeries barSeries) {
		if(barSeries.getBarCount() < 1){
			throw new IllegalArgumentException("Bar series needs at least two bars");
		}

		final int nbBars = barSeries.getBarCount();

		final Date[] dates = new Date[nbBars];
		final double[] opens = new double[nbBars];
		final double[] highs = new double[nbBars];
		final double[] lows = new double[nbBars];
		final double[] closes = new double[nbBars];
		final double[] volumes = new double[nbBars];

		for (int i = 0; i < nbBars; i++) {
			final Bar currentBar = barSeries.getBar(i);
			final long milliseconds = getMilliseconds(currentBar);
			dates[i] = new Date(milliseconds);
			opens[i] = currentBar.getOpenPrice().doubleValue();
			highs[i] = currentBar.getHighPrice().doubleValue();
			lows[i] = currentBar.getLowPrice().doubleValue();
			closes[i] = currentBar.getClosePrice().doubleValue();

			volumes[i] = currentBar.getVolume().doubleValue();

		}

		DefaultHighLowDataset tacOHLCDataset = new DefaultHighLowDataset(barSeries.getName(), dates, highs, lows, opens, closes, volumes);
		return tacOHLCDataset;
	}

	@Override
	public String getName(BarSeries element) {
		return element.getName();
	}

}
