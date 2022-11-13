package de.sjwimmer.ta4jchart.chartbuilder.crosshair;

import de.sjwimmer.ta4jchart.chartbuilder.GlobalConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.event.OverlayChangeListener;
import org.jfree.chart.panel.Overlay;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;

public class TacChartMouseHandler implements ChartMouseListener, Overlay {

    private static final Logger log = LogManager.getLogger(TacChartMouseHandler.class);

    private final ChartPanel chartPanel;
    private final CombinedDomainXYPlot combinedDomainXYPlot;

    private double x = Double.NaN;
    private double y = Double.NaN;

    private double xx = Double.NaN;
    private double yy = Double.NaN;
    private boolean sticky = true;

    long[] ohlcXValues;

    public TacChartMouseHandler(ChartPanel chartPanel) {
        this.chartPanel = chartPanel;
        this.chartPanel.addChartMouseListener(this);
        this.combinedDomainXYPlot = (CombinedDomainXYPlot) chartPanel.getChart().getPlot();
        final XYPlot mainPlot = (XYPlot) combinedDomainXYPlot.getSubplots().get(0);
        chartPanel.addOverlay(this);

        for(Object p: this.combinedDomainXYPlot.getSubplots()){
            if(p instanceof XYPlot) {
                XYPlot subPlot = (XYPlot) p;
                subPlot.setDomainCrosshairVisible(true);
                subPlot.setRangeCrosshairVisible(true);
            }
        }

        final XYDataset dataset = mainPlot.getDataset(0);
        final int entriesCount = dataset.getItemCount(0);
        ohlcXValues = new long[entriesCount];
        for(int i = 0; i< entriesCount; i++) {
            ohlcXValues[i] = (long) dataset.getX(0, i);
        }
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent event) {

    }

    @Override
    public void chartMouseMoved(ChartMouseEvent event) {
        int x = event.getTrigger().getX();
        int y = event.getTrigger().getY();
        final Point p = new Point(x, y);
        final ChartRenderingInfo chartRenderingInfo = chartPanel.getChartRenderingInfo();

        final Point point = chartPanel.translateJava2DToScreen(p);
        final PlotRenderingInfo plotInfo = chartRenderingInfo.getPlotInfo();

        int subplotIndex = plotInfo.getSubplotIndex(point);

        if(subplotIndex >= 0) {
            Rectangle2D dataArea = plotInfo.getDataArea();
            double xx = combinedDomainXYPlot.getDomainAxis().java2DToValue(point.getX(), dataArea, combinedDomainXYPlot.getDomainAxisEdge());
            log.debug("y: {} yy: {}", x, xx);
            final Rectangle2D panelArea = chartPanel.getScreenDataArea(x, y);
            final java.util.List<?>subplots = combinedDomainXYPlot.getSubplots();
            final Iterator<?> iterator = subplots.iterator();
            int plotIndex = 0;
            while (iterator.hasNext()) {
                XYPlot subPlot = (XYPlot) iterator.next();
                subPlot.setDomainCrosshairVisible(true);
                subPlot.setDomainCrosshairValue(findClosestXValue(xx));
                if(subplotIndex == plotIndex) {
                    double yy = subPlot.getRangeAxis().java2DToValue(point.getY(), panelArea, subPlot.getRangeAxisEdge());
                    log.debug("y: {} yy: {}", y, yy);
                    subPlot.setRangeCrosshairVisible(true);
                    subPlot.setRangeCrosshairValue(yy);
                    this.x = x;
                    this.y = y;
                    this.xx = xx;
                    this.yy = yy;
                } else {
                    subPlot.setRangeCrosshairVisible(false);
                }
                plotIndex++;
            }
        }
    }

    private double findClosestXValue(double xx) {
        if(this.sticky && this.ohlcXValues != null) {
            for (double x : this.ohlcXValues) {
                if (x >= xx) {
                    return x;
                }
            }
        }
        return xx;
    }

    @Override
    public void paintOverlay(Graphics2D g2, ChartPanel chartPanel) {
        if(!Double.isNaN(x) && !Double.isNaN(y)) {
            g2.drawString("Date: " + createDateString(xx), (int)x+10, (int)y+60);
            g2.drawString("Value: " + yy, (int)x+10, (int)y+80);
        }
    }

    private String createDateString(double xx) {
        Date date = Date.from(Instant.ofEpochMilli((long) findClosestXValue(xx)));
        try {
            return GlobalConstants.DATE_FORMATTER.valueToString(date);
        } catch (ParseException e) {
            log.error("", e);
        }
        return date.toString();
    }

    @Override
    public void addChangeListener(OverlayChangeListener listener) {

    }

    @Override
    public void removeChangeListener(OverlayChangeListener listener) {

    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }
}
