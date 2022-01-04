package org.sjwimmer.ta4jchart.chartbuilder.renderer;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;

import javax.swing.*;

import java.awt.*;

import static org.jfree.chart.StandardChartTheme.createDarknessTheme;

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
