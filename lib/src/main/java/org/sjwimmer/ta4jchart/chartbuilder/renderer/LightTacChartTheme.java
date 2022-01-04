package org.sjwimmer.ta4jchart.chartbuilder.renderer;

import com.formdev.flatlaf.FlatLightLaf;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;

import java.awt.*;

import static org.jfree.chart.StandardChartTheme.createJFreeTheme;

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
