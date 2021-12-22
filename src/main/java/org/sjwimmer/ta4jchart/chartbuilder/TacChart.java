package org.sjwimmer.ta4jchart.chartbuilder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.sjwimmer.ta4jchart.chartbuilder.crosshair.TacChartMouseHandler;
import org.sjwimmer.ta4jchart.chartbuilder.data.DataPanel;
import org.sjwimmer.ta4jchart.chartbuilder.data.TacDataTableModel;
import org.sjwimmer.ta4jchart.chartbuilder.toolbar.TacShowDataButton;
import org.sjwimmer.ta4jchart.chartbuilder.toolbar.TacShowTradingRecordButton;
import org.sjwimmer.ta4jchart.chartbuilder.toolbar.TacStickyCrossHairButton;
import org.sjwimmer.ta4jchart.chartbuilder.tradingrecord.TradingRecordPanel;

import javax.swing.*;
import java.awt.*;

public class TacChart extends JPanel {

    private final JToolBar toolBar = new JToolBar("Action");
    private final ChartPanel chartPanel;


    public TacChart(JFreeChart chart, TacDataTableModel tacDataTableModel, JPanel tradingRecordPanel) {
        super(new BorderLayout());
        this.chartPanel = new ChartPanel(chart);
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(chartPanel), BorderLayout.CENTER);

        toolBar.add(new TacStickyCrossHairButton(new TacChartMouseHandler(chartPanel)));
        toolBar.add(new TacShowDataButton(new DataPanel(tacDataTableModel), this));
        toolBar.add(new TacShowTradingRecordButton(tradingRecordPanel, this));
    }
}
