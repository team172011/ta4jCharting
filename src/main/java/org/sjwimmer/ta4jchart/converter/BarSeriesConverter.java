package org.sjwimmer.ta4jchart.converter;

import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.ta4j.core.BarSeries;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BarSeriesConverter extends Converter<BarSeries, OHLCDataset> {
}
