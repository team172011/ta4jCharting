package de.sjwimmer.ta4jchart.chartbuilder;

import de.sjwimmer.ta4jchart.chartbuilder.crosshair.TacChartMouseHandler;
import de.sjwimmer.ta4jchart.chartbuilder.data.DataPanel;
import de.sjwimmer.ta4jchart.chartbuilder.data.TacDataTableModel;
import de.sjwimmer.ta4jchart.chartbuilder.toolbar.TacShowBuySellSignals;
import de.sjwimmer.ta4jchart.chartbuilder.toolbar.TacShowDataButton;
import de.sjwimmer.ta4jchart.chartbuilder.toolbar.TacShowTradingRecordButton;
import de.sjwimmer.ta4jchart.chartbuilder.toolbar.TacStickyCrossHairButton;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.ta4j.core.BarSeries;
import org.ta4j.core.TradingRecord;

import javax.swing.*;
import java.awt.*;

public class TacChart extends JPanel {

    public TacChart(JFreeChart chart, BarSeries barSeries, TacDataTableModel tacDataTableModel, TradingRecord tradingRecord) {
        super(new BorderLayout());
        final ChartPanel chartPanel = new ChartPanel(chart);
        final JToolBar toolBar = new JToolBar("Action");

        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(chartPanel), BorderLayout.CENTER);

        toolBar.add(new TacStickyCrossHairButton(new TacChartMouseHandler(chartPanel)));
        toolBar.add(new TacShowDataButton(new DataPanel(tacDataTableModel), this));
        toolBar.add(new TacShowTradingRecordButton(tradingRecord, this));
        toolBar.add(new TacShowBuySellSignals(chart, barSeries, tradingRecord, this));
    }
}
