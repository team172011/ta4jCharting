package org.sjwimmer.ta4jchart.converter;
import java.util.*;

import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.*;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;

public class BarSeriesConverterImpl implements BarSeriesConverter {

	@Override
	public OHLCDataset apply(BarSeries barSeries) {
		final int nbBars = barSeries.getBarCount();

		Date[] dates = new Date[nbBars];
		double[] opens = new double[nbBars];
		double[] highs = new double[nbBars];
		double[] lows = new double[nbBars];
		double[] closes = new double[nbBars];
		double[] volumes = new double[nbBars];

		for (int i = 0; i < nbBars; i++) {
			Bar Bar = barSeries.getBar(i);
			dates[i] = new Date(Bar.getEndTime().toEpochSecond() * 1000);
			opens[i] = Bar.getOpenPrice().doubleValue();
			highs[i] = Bar.getHighPrice().doubleValue();
			lows[i] = Bar.getLowPrice().doubleValue();
			closes[i] = Bar.getClosePrice().doubleValue();
			volumes[i] = Bar.getVolume().doubleValue();
		}

		return new DefaultHighLowDataset(barSeries.getName(), dates, highs, lows, opens, closes, volumes);
	}

	@Override
	public String getName(BarSeries element) {
		return element.getName();
	}
}
