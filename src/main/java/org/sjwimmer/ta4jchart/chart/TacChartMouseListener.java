package org.sjwimmer.ta4jchart.chart;

import org.jfree.chart.*;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

public class TacChartMouseListener implements ChartMouseListener {

    private final ChartPanel chartPanel;
    private final CombinedDomainXYPlot combinedDomainXYPlot;
    private final XYPlot mainPlot;

    public TacChartMouseListener(ChartPanel chartPanel) {
        this.chartPanel = chartPanel;

       this.combinedDomainXYPlot = (CombinedDomainXYPlot) chartPanel.getChart().getPlot();
       this.mainPlot = (XYPlot) combinedDomainXYPlot.getSubplots().get(0);
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
        int x = event.getTrigger().getY();
        int y = event.getTrigger().getY();
        final Point p = new Point(x, y);
        System.out.println("raw X: " + x);
        final ChartRenderingInfo chartRenderingInfo = chartPanel.getChartRenderingInfo();

        final Point point = chartPanel.translateJava2DToScreen(p);
        final PlotRenderingInfo plotInfo = chartRenderingInfo.getPlotInfo();

        int subplotIndex = plotInfo.getSubplotIndex(point);

        if(subplotIndex >= 0) {
            Rectangle2D dataArea = plotInfo.getDataArea();
            double xx = combinedDomainXYPlot.getDomainAxis().java2DToValue(point.getX(), dataArea, combinedDomainXYPlot.getDomainAxisEdge());
            Rectangle2D panelArea = chartPanel.getScreenDataArea(x, y);
            java.util.List subplots = combinedDomainXYPlot.getSubplots();
            Iterator iterator = subplots.iterator();
            int index = 0;
            System.out.println("X: " + xx);

            while (iterator.hasNext()) {
                XYPlot subPlot = (XYPlot) iterator.next();
                subPlot.setDomainCrosshairVisible(true);
                subPlot.setDomainCrosshairValue(xx);
                if(subplotIndex == index) {
                    double yy = subPlot.getRangeAxis().java2DToValue(point.getY(), panelArea, subPlot.getRangeAxisEdge());
                    subPlot.setRangeCrosshairVisible(true);
                    subPlot.setRangeCrosshairValue(yy);
                    System.out.println("Y: " + yy);
                } else {
                    subPlot.setRangeCrosshairVisible(false);
                }
                index++;
            }
        }
    }
}
