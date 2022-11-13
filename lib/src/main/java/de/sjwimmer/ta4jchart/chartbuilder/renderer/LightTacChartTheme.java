package de.sjwimmer.ta4jchart.chartbuilder.renderer;

import static org.jfree.chart.StandardChartTheme.createJFreeTheme;

import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;

import com.formdev.flatlaf.FlatLightLaf;

public class LightTacChartTheme implements TacChartTheme {

    public LightTacChartTheme() {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void apply(JFreeChart chart) {
        ChartTheme jFreeTheme = createJFreeTheme();
        jFreeTheme.apply(chart);
    }
}
