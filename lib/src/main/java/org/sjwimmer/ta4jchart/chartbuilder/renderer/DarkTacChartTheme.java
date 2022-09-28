package org.sjwimmer.ta4jchart.chartbuilder.renderer;

import static org.jfree.chart.StandardChartTheme.createDarknessTheme;

import javax.swing.UIManager;

import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;

import com.formdev.flatlaf.FlatDarculaLaf;

public class DarkTacChartTheme implements TacChartTheme {

    public DarkTacChartTheme() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void apply(JFreeChart chart) {
        ChartTheme darknessTheme = createDarknessTheme();
        darknessTheme.apply(chart);
        chart.setBorderVisible(false);
        chart.setBackgroundPaint(UIManager.getColor("Panel.background"));
        chart.getLegend().setBackgroundPaint(UIManager.getColor("Panel.background"));
        applyChart(chart);
    }
}
