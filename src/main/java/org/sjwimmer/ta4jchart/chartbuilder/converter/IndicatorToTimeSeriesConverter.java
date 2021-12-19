package org.sjwimmer.ta4jchart.chartbuilder.converter;

import org.jfree.data.time.TimeSeriesCollection;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public interface IndicatorToTimeSeriesConverter extends Converter<Indicator<Num>, TimeSeriesCollection> {
}
