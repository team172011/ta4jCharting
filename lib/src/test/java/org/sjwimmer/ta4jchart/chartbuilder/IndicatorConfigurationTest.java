package org.sjwimmer.ta4jchart.chartbuilder;

import org.junit.jupiter.api.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarBuilder;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

public class IndicatorConfigurationTest {


    @Test
    public void testDefaultBuilder() {
        final BarSeries barSeries = new BaseBarSeries("Test", BarSeriesHelper.createBars());
        final EMAIndicator emaIndicator = new EMAIndicator(new ClosePriceIndicator(barSeries), 2);
        IndicatorConfiguration<Num> indicatorConfiguration = IndicatorConfiguration.Builder.of(emaIndicator).build();

        assertNotNull(indicatorConfiguration);
        assertNotNull(indicatorConfiguration.getIndicator());
        assertNotNull(indicatorConfiguration.getColor());
        assertEquals(emaIndicator.toString(), indicatorConfiguration.getName());
        assertNotNull(indicatorConfiguration.getShape());
        assertTrue(indicatorConfiguration.isAddToDataTable());
        assertEquals(PlotType.OVERLAY, indicatorConfiguration.getPlotType());
        assertEquals(ChartType.LINE, indicatorConfiguration.getChartType());

    }

    @Test
    public void testParameterForBuilder() {
        final BarSeries barSeries = new BaseBarSeries("Test", BarSeriesHelper.createBars());
        final EMAIndicator emaIndicator = new EMAIndicator(new ClosePriceIndicator(barSeries), 2);
        final String name = "test";
        final Color color = Color.ORANGE;
        final Shape shape = new Rectangle2D.Double(2,2,2,2);
        final PlotType plotType = PlotType.SUBPLOT;
        final ChartType chartType = ChartType.BAR;

        IndicatorConfiguration<Num> indicatorConfiguration = IndicatorConfiguration.Builder.of(emaIndicator)
                .name(name)
                .plotType(plotType)
                .chartType(chartType)
                .shape(shape)
                .color(color)
                .notInTable()
                .build();

        assertNotNull(indicatorConfiguration);
        assertEquals(emaIndicator, indicatorConfiguration.getIndicator());
        assertEquals(color, indicatorConfiguration.getColor());
        assertEquals(name, indicatorConfiguration.getName());
        assertEquals(shape, indicatorConfiguration.getShape());
        assertFalse(indicatorConfiguration.isAddToDataTable());
        assertEquals(plotType, indicatorConfiguration.getPlotType());
        assertEquals(chartType, indicatorConfiguration.getChartType());

    }
}
