package org.sjwimmer.ta4jchart.chartbuilder.renderer;

import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;

import java.awt.*;

public class TacChartTheme extends StandardChartTheme {

    public TacChartTheme() {
        super("Ta4j-theme");

        setPlotBackgroundPaint(Color.white);
        setLegendBackgroundPaint(Color.white);
        setXYBarPainter(new StandardXYBarPainter());
    }
}
