package org.sjwimmer.ta4jchart.chartbuilder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.sjwimmer.ta4jchart.chart.TacChartMouseHandler;
import org.sjwimmer.ta4jchart.chart.dataset.TacBarDataset;
import org.sjwimmer.ta4jchart.chart.elements.TacDataTable;
import org.sjwimmer.ta4jchart.chart.elements.TacStickyCrossHairButton;
import org.sjwimmer.ta4jchart.converter.*;
import org.sjwimmer.ta4jchart.chart.elements.data.DataTableModel;
import org.sjwimmer.ta4jchart.chart.renderer.TacBarRenderer;
import org.sjwimmer.ta4jchart.chart.renderer.TacCandlestickRenderer;
import org.sjwimmer.ta4jchart.chart.renderer.TacChartTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.*;
import org.ta4j.core.num.Num;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartBuilderImpl implements ChartBuilder {

	private final static Logger log = LoggerFactory.getLogger(ChartBuilderImpl.class);
	private final TacChartTheme theme = new TacChartTheme();
	private final BarSeries barSeries;
	private final BarSeriesConverter barSeriesConverter;
	private final IndicatorToTimeSeriesConverter indicatorConverter;
	private final IndicatorToBarDataConverter indicatorToBarDataConverter;
	private final JFreeChart chart;
	private final DataTableModel dataTableModel = new DataTableModel();
	private final ChartBuilderConfig chartBuilderConfig;

	private int overlayIds = 2; // 0 = ohlcv data, 1 = volume data
		
	public ChartBuilderImpl(BarSeries barSeries) {
		this(barSeries, new BarSeriesConverterImpl(), new IndicatorToTimeSeriesConverterImpl(), new IndicatorToBarDataConverterImpl(), new BaseChartBuilderConfig());
	}
	
	public ChartBuilderImpl(BarSeries barSeries, BarSeriesConverter barseriesPlotter, IndicatorToTimeSeriesConverter indicatorConverter, IndicatorToBarDataConverter indicatorToBarDataConverter, ChartBuilderConfig chartBuilderConfig) {
		this.barSeriesConverter = barseriesPlotter;
		this.indicatorConverter = indicatorConverter;
		this.indicatorToBarDataConverter = indicatorToBarDataConverter;
		this.chartBuilderConfig = chartBuilderConfig;
		this.barSeries = barSeries;
		chart = createCandlestickChart(this.barSeries, dataTableModel);
	}

	@Override
	public JPanel createPlot() {
		final JPanel mainPanel = new JPanel(new BorderLayout());
		final TacDataTable dataTable = new TacDataTable(dataTableModel);

		final ChartPanel chartPanel = new ChartPanel(chart);
		final JToolBar toolBar = new JToolBar("Action");
		final TacChartMouseHandler mouseHandler = new TacChartMouseHandler(chartPanel);

		toolBar.add(new TacStickyCrossHairButton(mouseHandler));
		mainPanel.add(toolBar, BorderLayout.NORTH);
		mainPanel.add(chartPanel, BorderLayout.CENTER);
		chartPanel.addChartMouseListener(mouseHandler);
		if (chartBuilderConfig.isPlotDataTable()) {
			mainPanel.add(new JScrollPane(dataTable), BorderLayout.EAST);
		}

		return mainPanel;
	}

	private JFreeChart createCandlestickChart(final BarSeries series, final DataTableModel dataTableModel) {
		final String seriesName = this.barSeriesConverter.getName(series);
		final ValueAxis timeAxis = new DateAxis("Time");
		final NumberAxis valueAxis = new NumberAxis("Price/Value");
		final TacCandlestickRenderer candlestickRenderer = new TacCandlestickRenderer();
		final DefaultHighLowDataset barSeriesData = this.barSeriesConverter.apply(series);
		final XYPlot plot = new XYPlot(barSeriesData, null, valueAxis, candlestickRenderer);

		final CombinedDomainXYPlot combinedDomainPlot = new CombinedDomainXYPlot(timeAxis);

		combinedDomainPlot.add(plot,10);
		valueAxis.setAutoRangeIncludesZero(false);
		candlestickRenderer.setAutoWidthMethod(1);
		candlestickRenderer.setDrawVolume(false);
		candlestickRenderer.setDefaultItemLabelsVisible(false);

		final JFreeChart chart = new JFreeChart(seriesName, JFreeChart.DEFAULT_TITLE_FONT,
				combinedDomainPlot, true);

		theme.apply(chart);
		dataTableModel.addEntries(barSeriesData);
		return chart;
	}

	@Override
	public void addIndicator(Indicator<Num> indicator) {
		this.addIndicator(indicator, PlotType.OVERLAY, ChartType.LINE);
	}

	@Override
	public void addIndicator(Indicator<Num> indicator, PlotType plotType, ChartType chartType) {
		if(plotType == PlotType.OVERLAY) {
			if(chartType == ChartType.LINE) {
				final int counter = overlayIds++;
				final TimeSeriesCollection timeSeriesCollection = this.indicatorConverter.apply(indicator);
				final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
				final CombinedDomainXYPlot combinedDomainPlot = (CombinedDomainXYPlot) this.chart.getPlot();
				final XYPlot candlestickPlot = (XYPlot) combinedDomainPlot.getSubplots().get(0);

				renderer.setSeriesShape(0, new Rectangle2D.Double(-3.0, -3.0, 3.0, 3.0));
				candlestickPlot.setRenderer(counter, renderer);
				candlestickPlot.setDataset(counter, timeSeriesCollection);
				this.dataTableModel.addEntries(timeSeriesCollection);
			} else if(chartType == ChartType.BAR) {
				final int counter = overlayIds++;
				final TacBarDataset barDataset = indicatorToBarDataConverter.apply(indicator);
				final TacBarRenderer barRenderer = new TacBarRenderer(Color.blue);
				final CombinedDomainXYPlot combinedDomainPlot = (CombinedDomainXYPlot) this.chart.getPlot();
				final XYPlot candlestickPlot = (XYPlot) combinedDomainPlot.getSubplots().get(0);

				candlestickPlot.setRenderer(counter, barRenderer);
				candlestickPlot.setDataset(counter, barDataset);
			}
		} else if (plotType == PlotType.SUBPLOT) {
			if(chartType == ChartType.BAR) {
				final TacBarDataset barDataset = indicatorToBarDataConverter.apply(indicator);
				final NumberAxis valueAxis = new NumberAxis(indicatorToBarDataConverter.getName(indicator));
				final TacBarRenderer barRenderer = new TacBarRenderer(Color.blue);
				final XYPlot barPlot = new XYPlot(barDataset, null, valueAxis, barRenderer);
				final CombinedDomainXYPlot combinedDomainPlot = (CombinedDomainXYPlot) this.chart.getPlot();
				valueAxis.setLabel("");
				combinedDomainPlot.add(barPlot, 1);
			} else if (chartType == ChartType.LINE) {
				final TimeSeriesCollection timeSeriesCollection = this.indicatorConverter.apply(indicator);
				final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
				final NumberAxis valueAxis = new NumberAxis(indicatorToBarDataConverter.getName(indicator).replaceAll("\\s+", "\n"));
				final XYPlot linePlot = new XYPlot(timeSeriesCollection, null, valueAxis, renderer);
				final CombinedDomainXYPlot combinedDomainPlot = (CombinedDomainXYPlot) this.chart.getPlot();

				valueAxis.setLabel("");
				valueAxis.setAutoRangeIncludesZero(false);
				renderer.setSeriesShape(0, new Rectangle2D.Double(-3.0, -3.0, 3.0, 3.0));
				this.dataTableModel.addEntries(timeSeriesCollection);
				combinedDomainPlot.add(linePlot, 1);
				this.dataTableModel.addEntries(timeSeriesCollection);
			}
		}
		theme.apply(chart);
	}

	@Override
	public void setTradingRecord(TradingRecord tradingRecord) {
		List<Object> tradeData = new ArrayList<>();
		for(int i = this.barSeries.getBeginIndex(); i < this.barSeries.getBarCount(); i++){
			tradeData.add("-");
		}

		if(tradingRecord.getLastExit() != null){
			final XYPlot mainPlot = ((XYPlot)((CombinedDomainXYPlot) chart.getPlot()).getSubplots().get(0));
			final java.util.List<Position> trades = tradingRecord.getPositions();
			final Trade.TradeType orderType = tradingRecord.getLastExit().getType().complementType();
			final List<Marker> markers = new ArrayList<>();
			final RectangleAnchor entryAnchor = RectangleAnchor.TOP_LEFT;
			final RectangleAnchor exitAnchor = RectangleAnchor.BOTTOM_RIGHT;

			final Color entryColor = orderType == Trade.TradeType.SELL ? Color.RED : Color.GREEN;
			final Color exitColor = orderType == Trade.TradeType.SELL ? Color.GREEN: Color.RED;
			for(Position trade: trades){
				int entryIndex = trade.getEntry().getIndex();
				int exitIndex = trade.getExit().getIndex();
				double entry = new Minute(Date.from(
						this.barSeries.getBar(entryIndex).getEndTime().toInstant())).getFirstMillisecond();
				double exit = new Minute(Date.from(
						this.barSeries.getBar(exitIndex).getEndTime().toInstant())).getFirstMillisecond();
				tradeData.set(entryIndex, trade.getEntry());
				tradeData.set(exitIndex, trade.getExit());
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
