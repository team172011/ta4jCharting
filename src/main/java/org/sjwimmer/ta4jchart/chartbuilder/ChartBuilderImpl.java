package org.sjwimmer.ta4jchart.chartbuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.OHLCDataset;
import org.sjwimmer.ta4jchart.data.DataTableModel;
import org.sjwimmer.ta4jchart.converter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.*;
import org.ta4j.core.num.Num;

public class ChartBuilderImpl implements ChartBuilder {

	private final static Logger log = LoggerFactory.getLogger(ChartBuilderImpl.class);
	private final BarSeries barSeries;
	private final BarSeriesConverter barSeriesConverter;
	private final IndicatorConverter indicatorConverter;
	private final JFreeChart chart;
	private final DataTableModel dataTableModel = new DataTableModel();
	private final ChartBuilderConfig chartBuilderConfig;

	private int overlayCounter = 1; // 0 = ohlcv data
		
	public ChartBuilderImpl(BarSeries barSeries) {
		this(barSeries, new BarSeriesConverterImpl(), new IndicatorConverterImpl(), new BaseChartBuilderConfig());
	}
	
	public ChartBuilderImpl(BarSeries barSeries, BarSeriesConverter barseriesPlotter, IndicatorConverter indicatorConverter, ChartBuilderConfig chartBuilderConfig) {
		this.barSeriesConverter = barseriesPlotter;
		this.indicatorConverter = indicatorConverter;
		this.chartBuilderConfig = chartBuilderConfig;
		this.barSeries = barSeries;
		chart = createCandlestickChart(this.barSeries, dataTableModel);
	}

	@Override
	public JPanel createPlot() {
		final JPanel mainPanel = new JPanel(new BorderLayout());
		final JTable dataTable = new JTable(dataTableModel);
		final ChartPanel chartPanel = new ChartPanel(chart);
		mainPanel.add(chartPanel, BorderLayout.CENTER);

		if (chartBuilderConfig.isPlotDataTable()) {
			mainPanel.add(new JScrollPane(dataTable), BorderLayout.EAST);
		}

		return mainPanel;
	}

	private JFreeChart createCandlestickChart(final BarSeries series, final DataTableModel dataTableModel) {
		final String seriesName = this.barSeriesConverter.getName(series);
		final ValueAxis timeAxis = new DateAxis("Time");
		final NumberAxis valueAxis = new NumberAxis("Price");
		final CandlestickRenderer renderer = new CandlestickRenderer();
		final OHLCDataset barSeriesData = this.barSeriesConverter.apply(series);
		final XYPlot plot = new XYPlot(barSeriesData, timeAxis, valueAxis, renderer);
		JFreeChart chart = new JFreeChart(seriesName, JFreeChart.DEFAULT_TITLE_FONT,
				plot, true);
		new StandardChartTheme("JFree").apply(chart);

		dataTableModel.addEntries(barSeriesData);

		return chart;
	}

	@Override
	public void addIndicator(Indicator<Num> indicator) {
		final int counter = overlayCounter++;
		final TimeSeriesCollection timeSeriesCollection = this.indicatorConverter.apply(indicator);
		((XYPlot) this.chart.getPlot()).setRenderer(counter, new XYLineAndShapeRenderer());
		((XYPlot) this.chart.getPlot()).setDataset(counter, timeSeriesCollection);
		this.dataTableModel.addEntries(timeSeriesCollection);
	}

	@Override
	public void setTradingRecord(TradingRecord tradingRecord) {
		List<String> tradeData = new ArrayList<>();
		for(int i = this.barSeries.getBeginIndex(); i < this.barSeries.getBarCount(); i++){
			tradeData.add("-");
		}

		if(tradingRecord.getLastExit() != null){
			final XYPlot mainPlot = chart.getXYPlot();
			final java.util.List<Trade> trades = tradingRecord.getTrades();
			final Order.OrderType orderType = tradingRecord.getLastExit().getType().complementType();
			final List<Marker> markers = new ArrayList<>();
			final RectangleAnchor entryAnchor = RectangleAnchor.TOP_LEFT;
			final RectangleAnchor exitAnchor = RectangleAnchor.BOTTOM_RIGHT;

			final Color entryColor = orderType==Order.OrderType.SELL ? Color.RED : Color.GREEN;
			final Color exitColor = orderType==Order.OrderType.SELL ? Color.GREEN: Color.RED;
			for(Trade trade: trades){
				int entryIndex = trade.getEntry().getIndex();
				int exitIndex = trade.getExit().getIndex();
				double entry = new Minute(Date.from(
						this.barSeries.getBar(entryIndex).getEndTime().toInstant())).getFirstMillisecond();
				double exit = new Minute(Date.from(
						this.barSeries.getBar(exitIndex).getEndTime().toInstant())).getFirstMillisecond();
				tradeData.set(entryIndex, "Enter");
				tradeData.set(exitIndex, "Exit");
				ValueMarker in = new ValueMarker(entry);
				in.setLabel(orderType.toString());
				in.setLabelPaint(Color.WHITE);
				in.setLabelAnchor(entryAnchor);
				in.setPaint(entryColor);
				mainPlot.addDomainMarker(in);

				ValueMarker out = new ValueMarker(exit);
				out.setLabel(orderType.complementType().toString());
				out.setLabelPaint(Color.WHITE);
				out.setLabelAnchor(exitAnchor);
				out.setPaint(exitColor);
				mainPlot.addDomainMarker(out);

				IntervalMarker imarker = new IntervalMarker(entry, exit, entryColor);
				imarker.setAlpha(0.1f);
				mainPlot.addDomainMarker(imarker);
				markers.add(imarker);
				markers.add(in);
				markers.add(out);
			}
			this.dataTableModel.addEntries("Trades", tradeData);
		} else{
			log.error("No closed trade in trading record!");
		}
	}
}
