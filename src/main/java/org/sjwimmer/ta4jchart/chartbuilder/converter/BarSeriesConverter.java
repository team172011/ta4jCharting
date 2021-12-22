package org.sjwimmer.ta4jchart.chartbuilder.converter;

import org.jfree.data.xy.DefaultHighLowDataset;
import org.ta4j.core.BarSeries;

public interface BarSeriesConverter extends Converter<BarSeries, DefaultHighLowDataset> {

    default DefaultHighLowDataset convert(BarSeries barSeries) {
        return convert(barSeries, barSeries.getName());
    }
}
