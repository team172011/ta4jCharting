package de.sjwimmer.ta4jchart.chartbuilder.converter;

import de.sjwimmer.ta4jchart.chartbuilder.BarSeriesHelper;
import org.jfree.data.time.TimeSeriesCollection;
import org.junit.jupiter.api.Test;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndicatorConverterImplTest {

    @Test
    public void testCreateConverter(){
        double[] prices = new double[]{20, 21, 23, 24, 25, 26};
        IndicatorToTimeSeriesConverter indicatorConver = new IndicatorToTimeSeriesConverterImpl();
        BaseBarSeries barSeries = new BaseBarSeriesBuilder().withName("test").withBars(BarSeriesHelper.createBars(prices)).build();
        ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);
        final String name = "Close Price";

        final TimeSeriesCollection seriesCollection = indicatorConver.convert(closePrice, name);


        assertEquals(name, seriesCollection.getSeries(0).getKey());
        assertEquals(1, seriesCollection.getSeriesCount());
        assertEquals(prices.length, seriesCollection.getSeries(0).getItemCount());
        assertEquals(prices[0], seriesCollection.getSeries(0).getValue(0));
    }
}
