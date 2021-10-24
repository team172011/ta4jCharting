package org.sjwimmer.ta4jchart.converter;

import org.jfree.data.xy.OHLCDataset;
import org.junit.jupiter.api.Test;
import org.sjwimmer.ta4jchart.BarSeriesHelper;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BarSeriesConverterImplTest {


    @Test
    public void testCreateConverter(){
        final var barSeriesConverter = new BarSeriesConverterImpl();
        final double[] prices = new double[]{20, 21, 23, 24, 25, 26};
        final BaseBarSeries barSeries = new BaseBarSeriesBuilder().withBars(BarSeriesHelper.createBars(prices)).withName("test").build();

        OHLCDataset dataset = barSeriesConverter.apply(barSeries);
        String name = barSeriesConverter.getName(barSeries);

        assertEquals(1, dataset.getSeriesCount());
        assertEquals(prices.length, dataset.getItemCount(0));
        assertEquals(barSeries.getName(),name);
    }
}
