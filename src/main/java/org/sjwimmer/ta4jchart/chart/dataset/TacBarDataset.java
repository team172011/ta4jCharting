package org.sjwimmer.ta4jchart.chart.dataset;

import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class TacBarDataset extends XYSeriesCollection implements IntervalXYDataset {

    private final double barSize;
    private double padding = 0.1;

    public TacBarDataset(XYSeries volumeSeries, long barSize) {
        super(volumeSeries);
        this.barSize = (barSize - barSize * padding) / 2d;
    }

    @Override
    public double getEndXValue(int series, int item) {
        double endXValue = super.getEndXValue(series, item);
        return endXValue + barSize;
    }

    @Override
    public double getStartXValue(int series, int item) {
        double startXValue = super.getStartXValue(series, item);

        return startXValue - barSize;
    }

    public double getPadding() {
        return padding;
    }

    public void setPadding(double padding) {
        this.padding = padding;
    }
}
