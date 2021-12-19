package org.sjwimmer.ta4jchart.chartbuilder.renderer;

import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;

import java.awt.*;

public class TacBarRenderer extends XYBarRenderer {

    private final Color barColor;

    public TacBarRenderer(Color barColor) {
        this.barColor = barColor;
    }

    @Override
    public Paint getSeriesPaint(int seriesIndex) {
        return this.barColor;
    }

    @Override
    public XYBarPainter getBarPainter() {
        return new StandardXYBarPainter();
    }
}
