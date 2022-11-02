package de.sjwimmer.ta4jchart.chartbuilder.converter;

import de.sjwimmer.ta4jchart.chartbuilder.BarSeriesHelper;
import org.jfree.data.xy.OHLCDataset;
import org.junit.jupiter.api.Test;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BarSeriesConverterImplTest {


    @Test
    public void testCreateConverter(){
        final var barSeriesConverter = new BarSeriesConverterImpl();
        final double[] prices = new double[]{20, 21, 23, 24, 25, 26};
        final String name = "test";
        final BaseBarSeries barSeries = new BaseBarSeriesBuilder().withBars(BarSeriesHelper.createBars(prices)).withName(name).build();

        final OHLCDataset dataset = barSeriesConverter.convert(barSeries);

        assertEquals(1, dataset.getSeriesCount());
        assertEquals(prices.length, dataset.getItemCount(0));
        assertEquals(name, dataset.getSeriesKey(0));
    }

    @Test
    public void testCreateConverterWithName(){
        final var barSeriesConverter = new BarSeriesConverterImpl();
        final double[] prices = new double[]{20, 21, 23, 24, 25, 26};
        final String name = "test";
        final String otherName = "otherName";
        final BaseBarSeries barSeries = new BaseBarSeriesBuilder().withBars(BarSeriesHelper.createBars(prices)).withName(name).build();

        final OHLCDataset dataset = barSeriesConverter.convert(barSeries, otherName);

        assertEquals(1, dataset.getSeriesCount());
        assertEquals(prices.length, dataset.getItemCount(0));
        assertEquals(otherName, dataset.getSeriesKey(0));
    }
}
