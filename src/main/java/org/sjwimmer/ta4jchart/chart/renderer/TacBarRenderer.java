package org.sjwimmer.ta4jchart.chart.renderer;

import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.*;
import org.jfree.chart.ui.GradientPaintTransformType;
import org.jfree.chart.ui.GradientPaintTransformer;
import org.jfree.chart.ui.StandardGradientPaintTransformer;

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
