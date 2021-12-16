package org.sjwimmer.ta4jchart.chart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.*;
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.event.OverlayChangeListener;
import org.jfree.chart.panel.Overlay;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.ta4j.core.num.DoubleNum;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

public class TacChartMouseListener implements ChartMouseListener, Overlay {

    private static final Logger log = LogManager.getLogger(TacChartMouseListener.class);

    private final ChartPanel chartPanel;
    private final CombinedDomainXYPlot combinedDomainXYPlot;
    private final XYPlot mainPlot;

    private double x = Double.NaN;
    private double y = Double.NaN;

    public TacChartMouseListener(ChartPanel chartPanel) {
        this.chartPanel = chartPanel;
        this.combinedDomainXYPlot = (CombinedDomainXYPlot) chartPanel.getChart().getPlot();
        this.mainPlot = (XYPlot) combinedDomainXYPlot.getSubplots().get(0);
        chartPanel.addOverlay(this);
        for(Object p: this.combinedDomainXYPlot.getSubplots()){
            if(p instanceof XYPlot) {
                XYPlot subPlot = (XYPlot) p;
                subPlot.setDomainCrosshairVisible(true);
                subPlot.setRangeCrosshairVisible(true);
            }
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
            Rectangle2D panelArea = chartPanel.getScreenDataArea(x, y);
            java.util.List<?>subplots = combinedDomainXYPlot.getSubplots();
            Iterator<?> iterator = subplots.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                XYPlot subPlot = (XYPlot) iterator.next();
                subPlot.setDomainCrosshairVisible(true);
                subPlot.setDomainCrosshairValue(xx);
                if(subplotIndex == index) {
                    double yy = subPlot.getRangeAxis().java2DToValue(point.getY(), panelArea, subPlot.getRangeAxisEdge());
                    log.debug("y: {} yy: {}", y, yy);
                    subPlot.setRangeCrosshairVisible(true);
                    subPlot.setRangeCrosshairValue(yy);
                    this.x = x;
                    this.y = y;
                } else {
                    subPlot.setRangeCrosshairVisible(false);
                }
                index++;
            }
        }
    }

    @Override
    public void paintOverlay(Graphics2D g2, ChartPanel chartPanel) {
        if(this.x != Double.NaN && this.y != Double.NaN) {
            g2.draw(new Rectangle2D.Double(x-10, y-10, 20, 20));
        }
    }

    @Override
    public void addChangeListener(OverlayChangeListener listener) {

    }

    @Override
    public void removeChangeListener(OverlayChangeListener listener) {

    }
}
