package org.sjwimmer.ta4jchart.converter;

import org.sjwimmer.ta4jchart.chart.dataset.TacBarDataset;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public interface IndicatorToBarDataConverter extends Converter<Indicator<Num>, TacBarDataset>{
}
