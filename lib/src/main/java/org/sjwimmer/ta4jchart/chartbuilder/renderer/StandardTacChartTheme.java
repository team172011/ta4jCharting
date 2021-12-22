package org.sjwimmer.ta4jchart.chartbuilder.renderer;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;

import java.awt.*;

public class StandardTacChartTheme extends StandardChartTheme implements TacChartTheme {



    public StandardTacChartTheme() {
        super("Ta4j-theme");
        setPlotOutlinePaint(Color.GRAY);
        setPlotBackgroundPaint(Color.white);
        setLegendBackgroundPaint(Color.white);
        setXYBarPainter(new StandardXYBarPainter());
    }

    @Override
    public void apply(JFreeChart chart) {
        super.apply(chart);
        final CombinedDomainXYPlot plot = (CombinedDomainXYPlot) chart.getPlot();
        final XYPlot xyPlot = (XYPlot) plot.getSubplots().get(0);
        xyPlot.setDomainGridlinePaint(Color.GRAY);
        xyPlot.setRangeGridlinePaint(Color.GRAY);
        xyPlot.setRangeMinorGridlinePaint(Color.LIGHT_GRAY);
        xyPlot.setDomainMinorGridlinePaint(Color.LIGHT_GRAY);

        xyPlot.setDomainGridlinesVisible(true);
        xyPlot.setRangeGridlinesVisible(true);
    }
}
