package de.sjwimmer.ta4jchart.chartbuilder;

import de.sjwimmer.ta4jchart.chartbuilder.data.DataPanel;
import de.sjwimmer.ta4jchart.chartbuilder.toolbar.TacShowDataButton;
import de.sjwimmer.ta4jchart.chartbuilder.toolbar.TacShowTradingRecordButton;
import de.sjwimmer.ta4jchart.chartbuilder.toolbar.TacStickyCrossHairButton;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import de.sjwimmer.ta4jchart.chartbuilder.crosshair.TacChartMouseHandler;
import de.sjwimmer.ta4jchart.chartbuilder.data.TacDataTableModel;

import javax.swing.*;
import java.awt.*;

public class TacChart extends JPanel {

    public TacChart(JFreeChart chart, TacDataTableModel tacDataTableModel, JPanel tradingRecordPanel) {
        super(new BorderLayout());
        ChartPanel chartPanel = new ChartPanel(chart);

        JToolBar toolBar = new JToolBar("Action");
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(chartPanel), BorderLayout.CENTER);

        toolBar.add(new TacStickyCrossHairButton(new TacChartMouseHandler(chartPanel)));
        toolBar.add(new TacShowDataButton(new DataPanel(tacDataTableModel), this));
        toolBar.add(new TacShowTradingRecordButton(tradingRecordPanel, this));
    }
}
