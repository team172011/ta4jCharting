package org.sjwimmer.ta4jchart.converter;

import java.util.Date;
import java.util.List;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public interface IndicatorConverter extends Converter<Indicator<Num>, TimeSeriesCollection> {
}
