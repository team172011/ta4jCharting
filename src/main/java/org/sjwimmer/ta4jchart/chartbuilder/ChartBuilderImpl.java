package org.sjwimmer.ta4jchart.chartbuilder;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.knowm.xchart.OHLCChart;
import org.knowm.xchart.OHLCChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.sjwimmer.ta4jchart.data.DataTableModel;
import org.sjwimmer.ta4jchart.plotter.BarSeriesPlotter;
import org.sjwimmer.ta4jchart.plotter.BarSeriesPlotterImpl;
import org.sjwimmer.ta4jchart.plotter.IndicatorPlotter;
import org.sjwimmer.ta4jchart.plotter.IndicatorPlotterImpl;
import org.sjwimmer.ta4jchart.plotter.StrategyPlotter;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.Strategy;
import org.ta4j.core.num.Num;

public class ChartBuilderImpl implements ChartBuilder {
	
	private final BarSeriesPlotter barseriesPlotter;
	private final IndicatorPlotter<Num> indicatorPlotter;
	private final StrategyPlotter strategyPlotter;
	
		
	public ChartBuilderImpl() {
		this.barseriesPlotter = new BarSeriesPlotterImpl();
		this.indicatorPlotter = new IndicatorPlotterImpl();
		this.strategyPlotter = null;
	}
	
	public ChartBuilderImpl(BarSeriesPlotter barseriesPlotter, IndicatorPlotter<Num> indicatorPlotter, StrategyPlotter strategyPlotter) {
		this.barseriesPlotter = barseriesPlotter;
		this.indicatorPlotter = indicatorPlotter;
		this.strategyPlotter = strategyPlotter;
	}

	@Override
	public JPanel createPlot() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		DataTableModel dataTableModel = new DataTableModel();
		OHLCChart chart = new OHLCChartBuilder().width(600).height(400).title("MyTitle").xAxisTitle("Time").yAxisTitle("Value").build();
		
		chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
		

		for(String seriesName: this.barseriesPlotter.getBarSeriesNames()) {
			chart.addSeries(seriesName, 
					this.barseriesPlotter.getDates(seriesName),
					this.barseriesPlotter.getOpenData(seriesName), 
					this.barseriesPlotter.getHighData(seriesName),
					this.barseriesPlotter.getLowData(seriesName), 
					this.barseriesPlotter.getCloseData(seriesName))
			.setUpColor(Color.RED)
			.setDownColor(Color.GREEN);
			dataTableModel.addEntry(seriesName, this.barseriesPlotter.getCloseData(seriesName));
		}
		
		for(String indicatorName: this.indicatorPlotter.getIndicatorNames()) {
			chart.addSeries(indicatorName, 
					null, 
					this.indicatorPlotter.getValues(indicatorName));
			dataTableModel.addEntry(indicatorName, this.indicatorPlotter.getValues(indicatorName));
		}
		
		JTable dataTable = new JTable(dataTableModel);
		JPanel chartPanel = new XChartPanel<OHLCChart>(chart);
		mainPanel.add(chartPanel, BorderLayout.CENTER);
		mainPanel.add(new JScrollPane(dataTable), BorderLayout.EAST);
		return mainPanel;
	}

	@Override
	public void addBarSeries(BarSeries barSeries) {
		this.barseriesPlotter.addBarSeries(barSeries);
		
	}

	@Override
	public void addStrategy(Strategy strategy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addIndicator(String name, Indicator<Num> indicator) {
		this.indicatorPlotter.addIndicator(name, indicator);
	}


}
