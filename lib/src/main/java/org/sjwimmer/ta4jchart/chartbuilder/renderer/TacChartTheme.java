package org.sjwimmer.ta4jchart.chartbuilder.renderer;

import java.awt.Color;

import javax.swing.UIManager;

import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;

public interface TacChartTheme extends ChartTheme {

    default void applyChart(JFreeChart chart){
        Color backgroundColor = UIManager.getColor("Panel.background");
        final CombinedDomainXYPlot plot = (CombinedDomainXYPlot) chart.getPlot();
        final XYPlot xyPlot = (XYPlot) plot.getSubplots().get(0);
        xyPlot.setBackgroundPaint(backgroundColor);
        xyPlot.setOutlinePaint(Color.GRAY);
        plot.getDomainAxis().setTickLabelPaint(UIManager.getColor("Label.foreground"));
        plot.getDomainAxis().setLabelPaint(UIManager.getColor("Label.foreground"));
    }
}
