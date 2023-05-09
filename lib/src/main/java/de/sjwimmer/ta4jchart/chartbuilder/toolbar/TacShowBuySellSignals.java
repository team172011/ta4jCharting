package de.sjwimmer.ta4jchart.chartbuilder.toolbar;

import de.sjwimmer.ta4jchart.chartbuilder.TacChart;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.*;
import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.time.Minute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class TacShowBuySellSignals extends JToggleButton implements ActionListener {

    private static final Logger log = LoggerFactory.getLogger(TacShowBuySellSignals.class);

    private final TacChart mainPanel;
    private final JFreeChart chart;
    private final BarSeries barSeries;
    private final TradingRecord tradingRecord;

    final static Color MARKER_COLOR = Color.black;
    final static Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);

    public TacShowBuySellSignals(JFreeChart chart, BarSeries barSeries, TradingRecord tradingRecord, TacChart mainPanel) {
        super("Entry/exit Signals");
        setToolTipText("Shows/hides entry and exit signals on the chart");
        addActionListener(this);
        this.mainPanel = mainPanel;
        this.chart = chart;
        this.barSeries = barSeries;
        this.tradingRecord = tradingRecord;
        if (tradingRecord == null) {
            this.setEnabled(false);
        } else {
            addBuySellSignals(tradingRecord, barSeries, chart);
            setSelected(true);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isSelected()) {
            addBuySellSignals(tradingRecord, barSeries, chart);
        } else {
            removeBuySellSignals(((XYPlot)((CombinedDomainXYPlot) chart.getPlot()).getSubplots().get(0)));
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void addBuySellSignals(TradingRecord tradingRecord, BarSeries barSeries, JFreeChart chart) {
        if(tradingRecord.getLastExit() != null) {
            final XYPlot mainPlot = ((XYPlot)((CombinedDomainXYPlot) chart.getPlot()).getSubplots().get(0));
            removeBuySellSignals(mainPlot);
            for(Position trade: tradingRecord.getPositions()){
                final int entryIndex = trade.getEntry().getIndex();
                final int exitIndex = trade.getExit().getIndex();
                final Bar entryBar = barSeries.getBar(entryIndex);
                final Bar exitBar = barSeries.getBar(exitIndex);
                final double profit = trade.getProfit().doubleValue();
                final Color profitColor = profit > 0 ? Color.GREEN: Color.RED;
                final double entry = new Minute(Date.from(entryBar
                        .getEndTime().toInstant())).getFirstMillisecond();
                final double exit = new Minute(Date.from(
                        exitBar.getEndTime().toInstant())).getFirstMillisecond();

                mainPlot.addDomainMarker(createInValueMarker(entry));
                mainPlot.addDomainMarker(createOutValueMarker(exit, profitColor));
                mainPlot.addDomainMarker(createIntervalTextMarker(entry, exit, profitColor, profit));
                mainPlot.addDomainMarker(createIntervalMarker(entry, exit, profitColor), Layer.BACKGROUND);
            }
        } else{
            log.error("No closed trade in trading record!");
        }

    }

    public void removeBuySellSignals(XYPlot plot) {
        removeDomainMarkers(plot);
    }

    private void removeDomainMarkers(XYPlot plot) {
        Collection<?> domainMarkers = plot.getDomainMarkers(Layer.FOREGROUND);
        Collection<?> domainMarkers2 = plot.getDomainMarkers(Layer.BACKGROUND);
        removeMarkers(plot, domainMarkers, Layer.FOREGROUND);
        removeMarkers(plot, domainMarkers2, Layer.BACKGROUND);
    }


    private void removeMarkers(XYPlot plot, Collection<?> markers, Layer layer) {
        if(markers != null){
            for (Object markerObject : new ArrayList<>(markers)) {
                if (markerObject instanceof Marker) {
                    plot.removeDomainMarker((Marker) markerObject, layer);
                }
            }
        }
    }

    private Marker createInValueMarker(double value) {
        ValueMarker in = new ValueMarker(value);
        in.setLabel("");
        in.setLabelFont(LABEL_FONT);
        in.setLabelPaint(Color.BLACK);
        in.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        in.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
        in.setPaint(MARKER_COLOR);
        return in;
    }

    private Marker createOutValueMarker(double value, Color color) {
        ValueMarker out = new ValueMarker(value);
        out.setLabel("");
        out.setLabelFont(LABEL_FONT);
        out.setLabelPaint(MARKER_COLOR);
        out.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        out.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
        out.setPaint(color);
        return out;
    }

    private Marker createIntervalTextMarker(double entry, double exit, Color profitColor, double profit) {
        IntervalMarker imarkerText = new IntervalMarker(entry, exit, new Color(0,0,0,0));
        imarkerText.setLabelFont(LABEL_FONT);
        imarkerText.setLabel(String.valueOf(profit));
        imarkerText.setLabelPaint(MARKER_COLOR);
        imarkerText.setLabelBackgroundColor(new Color(profitColor.getRed(), profitColor.getGreen(), profitColor.getBlue(), 200));
        imarkerText.setLabelAnchor(RectangleAnchor.BOTTOM);
        imarkerText.setLabelTextAnchor(TextAnchor.BOTTOM_CENTER);
        return imarkerText;
    }

    private Marker createIntervalMarker(double start, double end, Color color) {
        IntervalMarker imarker = new IntervalMarker(start, end, color);
        imarker.setAlpha(0.2f);
        return imarker;
    }
}
