package org.sjwimmer.ta4jchart.chart.renderer;

import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;

import java.awt.*;

public class TacCandlestickRenderer extends CandlestickRenderer {

    @Override
    public Paint getItemPaint(int row, int column) {
        OHLCDataset dataset = (OHLCDataset) getPlot().getDataset();
        double openValue = dataset.getOpenValue(row, column);
        double close = dataset.getCloseValue(row, column);
        if(openValue > close) {
            return getDownPaint();
        } else {
            return getUpPaint();
        }
    }
}
