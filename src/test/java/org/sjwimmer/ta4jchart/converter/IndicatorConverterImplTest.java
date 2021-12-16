package org.sjwimmer.ta4jchart.converter;

import org.jfree.data.time.TimeSeriesCollection;
import org.junit.jupiter.api.Test;
import org.sjwimmer.ta4jchart.BarSeriesHelper;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IndicatorConverterImplTest {

    @Test
    public void testCreateConverter(){
        var prices = new double[]{20, 21, 23, 24, 25, 26};
        var indicatorConver = new IndicatorToTimeSeriesConverterImpl();
        var barSeries = new BaseBarSeriesBuilder().withName("test").withBars(BarSeriesHelper.createBars(prices)).build();
        var closePrice = new ClosePriceIndicator(barSeries);

        final TimeSeriesCollection seriesCollection = indicatorConver.apply(closePrice);
        final String name = indicatorConver.getName(closePrice);

        assertNotNull(name);
        assertEquals(1, seriesCollection.getSeriesCount());
        assertEquals(prices.length, seriesCollection.getSeries(0).getItemCount());
        assertEquals(prices[0], seriesCollection.getSeries(0).getValue(0));
    }
}
