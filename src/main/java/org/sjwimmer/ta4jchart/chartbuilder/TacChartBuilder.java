package org.sjwimmer.ta4jchart.chartbuilder;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.sjwimmer.ta4jchart.chartbuilder.converter.*;
import org.sjwimmer.ta4jchart.chartbuilder.data.TacDataTableModel;
import org.sjwimmer.ta4jchart.chartbuilder.renderer.TacBarRenderer;
import org.sjwimmer.ta4jchart.chartbuilder.renderer.TacCandlestickRenderer;
import org.sjwimmer.ta4jchart.chartbuilder.renderer.StandardTacChartTheme;
import org.sjwimmer.ta4jchart.chartbuilder.tradingrecord.TradingRecordPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.*;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class TacChartBuilder {

	private final static Logger log = LoggerFactory.getLogger(TacChartBuilder.class);
	private final StandardTacChartTheme theme = new StandardTacChartTheme();
	private final BarSeries barSeries;
	private final BarSeriesConverter barSeriesConverter;
	private final IndicatorToTimeSeriesConverter indicatorToTimeSeriesConverter;
	private final IndicatorToBarDataConverter indicatorToBarDataConverter;
	private final JFreeChart chart;

	private final TacDataTableModel dataTableModel = new TacDataTableModel();
	private final TradingRecordPanel tradingRecordPanel = new TradingRecordPanel();


	private int overlayIds = 2; // 0 = ohlcv data, 1 = volume data

	public static TacChartBuilder of(BarSeries barSeries) {
		return new TacChartBuilder(barSeries);
	}
	private TacChartBuilder(BarSeries barSeries) {
		this(barSeries, new BarSeriesConverterImpl(), new IndicatorToTimeSeriesConverterImpl(), new IndicatorToBarDataConverterImpl());
	}
	
	public TacChartBuilder(BarSeries barSeries, BarSeriesConverter barseriesPlotter, IndicatorToTimeSeriesConverter indicatorConverter, IndicatorToBarDataConverter indicatorToBarDataConverter) {
		this.barSeriesConverter = barseriesPlotter;
		this.indicatorToTimeSeriesConverter = indicatorConverter;
		this.indicatorToBarDataConverter = indicatorToBarDataConverter;
		this.barSeries = barSeries;
		this.chart = createCandlestickChart(this.barSeries);
	}

	public TacChart build() {
		return new TacChart(chart, dataTableModel, tradingRecordPanel);
	}

	private JFreeChart createCandlestickChart(final BarSeries series) {
		final String seriesName = series.getName();
		final ValueAxis timeAxis = new DateAxis("Time");
		final NumberAxis valueAxis = new NumberAxis("Price/Value");
		final TacCandlestickRenderer candlestickRenderer = new TacCandlestickRenderer();
		final DefaultHighLowDataset barSeriesData = this.barSeriesConverter.convert(series);
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

	public TacChartBuilder withIndicator(IndicatorConfiguration.Builder<?> indicatorConfigurationBuilder) {
		final IndicatorConfiguration<?> indicatorConfiguration = indicatorConfigurationBuilder.build();
		final Indicator<?> indicator = indicatorConfiguration.getIndicator();
		final PlotType plotType = indicatorConfiguration.getPlotType();
		final ChartType chartType = indicatorConfiguration.getChartType();
		final Color color = indicatorConfiguration.getColor();
		final String name = indicatorConfiguration.getName();

		if(plotType == PlotType.OVERLAY) {
			if(chartType == ChartType.LINE) {
				final int counter = overlayIds++;
				final TimeSeriesCollection timeSeriesCollection = this.indicatorToTimeSeriesConverter.convert(indicator, name);
				final XYLineAndShapeRenderer renderer = createLineRenderer(counter, indicatorConfiguration);
				final CombinedDomainXYPlot combinedDomainPlot = (CombinedDomainXYPlot) this.chart.getPlot();
				final XYPlot candlestickPlot = (XYPlot) combinedDomainPlot.getSubplots().get(0);

				candlestickPlot.setRenderer(counter, renderer);
				candlestickPlot.setDataset(counter, timeSeriesCollection);
				this.dataTableModel.addEntries(timeSeriesCollection);
			} else if(chartType == ChartType.BAR) {
				final int counter = overlayIds++;
				final TacBarDataset barDataset = indicatorToBarDataConverter.convert(indicator, name);
				final TacBarRenderer barRenderer = createBarRenderer(indicatorConfiguration);
				final CombinedDomainXYPlot combinedDomainPlot = (CombinedDomainXYPlot) this.chart.getPlot();
				final XYPlot candlestickPlot = (XYPlot) combinedDomainPlot.getSubplots().get(0);

				candlestickPlot.setRenderer(counter, barRenderer);
				candlestickPlot.setDataset(counter, barDataset);
				this.dataTableModel.addEntries(barDataset);
			}
		} else if (plotType == PlotType.SUBPLOT) {
			if(chartType == ChartType.BAR) {
				final TacBarDataset barDataset = indicatorToBarDataConverter.convert(indicator, name);
				final NumberAxis valueAxis = new NumberAxis(name);
				final TacBarRenderer barRenderer = createBarRenderer(indicatorConfiguration);
				final XYPlot barPlot = new XYPlot(barDataset, null, valueAxis, barRenderer);
				final CombinedDomainXYPlot combinedDomainPlot = (CombinedDomainXYPlot) this.chart.getPlot();
				valueAxis.setLabel("");
				this.dataTableModel.addEntries(barDataset);

				combinedDomainPlot.add(barPlot, 1);
			} else if (chartType == ChartType.LINE) {
				final TimeSeriesCollection timeSeriesCollection = this.indicatorToTimeSeriesConverter.convert(indicator, name);
				final XYLineAndShapeRenderer renderer = createLineRenderer(0, indicatorConfiguration);
				final NumberAxis valueAxis = new NumberAxis(name);
				final XYPlot linePlot = new XYPlot(timeSeriesCollection, null, valueAxis, renderer);
				final CombinedDomainXYPlot combinedDomainPlot = (CombinedDomainXYPlot) this.chart.getPlot();

				valueAxis.setLabel("");
				valueAxis.setAutoRangeIncludesZero(false);
				this.dataTableModel.addEntries(timeSeriesCollection);
				combinedDomainPlot.add(linePlot, 1);
			}
		}
		return this;
	}

	private TacBarRenderer createBarRenderer(IndicatorConfiguration indicatorConfiguration) {
		final TacBarRenderer tacBarRenderer = new TacBarRenderer(indicatorConfiguration.getColor());

		return tacBarRenderer;
	}

	private XYLineAndShapeRenderer createLineRenderer(int counter, IndicatorConfiguration indicatorConfiguration) {
		XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer();
		lineRenderer.setSeriesShape(0, indicatorConfiguration.getShape());
		lineRenderer.setSeriesPaint(0, indicatorConfiguration.getColor());
		System.out.println(indicatorConfiguration.getColor());
		return lineRenderer;
	}


	public TacChartBuilder withTradingRecord(TradingRecord tradingRecord) {
		tradingRecordPanel.setTradingRecord(tradingRecord);
		if(tradingRecord.getLastExit() != null){
			final XYPlot mainPlot = ((XYPlot)((CombinedDomainXYPlot) chart.getPlot()).getSubplots().get(0));
			final java.util.List<Position> trades = tradingRecord.getPositions();
			final Trade.TradeType orderType = tradingRecord.getLastExit().getType().complementType();
			final Color markerColor = Color.black;
			final Font labelFont = new Font("Arial", Font.BOLD, 12);
			removeDomainMarkers(mainPlot);
			for(Position trade: trades){
				final int entryIndex = trade.getEntry().getIndex();
				final int exitIndex = trade.getExit().getIndex();
				final Bar entryBar = this.barSeries.getBar(entryIndex);
				final Bar exitBar = this.barSeries.getBar(exitIndex);
				final double profit = trade.getProfit().doubleValue();
				final Color profitColor = profit > 0 ? Color.GREEN: Color.RED;

				double entry = new Minute(Date.from(entryBar
						.getEndTime().toInstant())).getFirstMillisecond();
				double exit = new Minute(Date.from(
						exitBar.getEndTime().toInstant())).getFirstMillisecond();

				ValueMarker in = new ValueMarker(entry);
				in.setLabel(""); // orderType.toString()
				in.setLabelFont(labelFont);
				in.setLabelPaint(Color.BLACK);
				in.setLabelAnchor(RectangleAnchor.TOP_LEFT);
				in.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
				in.setPaint(markerColor);
				mainPlot.addDomainMarker(in);

				ValueMarker out = new ValueMarker(exit);
				out.setLabel("" + profit);
				out.setLabelPaint(Color.BLACK);
				out.setLabelFont(labelFont);
				out.setLabelBackgroundColor(new Color(profitColor.getRed(), profitColor.getGreen(), profitColor.getBlue(), 200));
				out.setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
				out.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
				out.setPaint(markerColor);
				mainPlot.addDomainMarker(out);

				IntervalMarker imarker = new IntervalMarker(entry, exit, profitColor);
				imarker.setAlpha(0.1f);

				/*
				IntervalMarker imarkerText = new IntervalMarker(entry, exit, new Color(0,0,0,0));
				imarkerText.setLabelFont(labelFont);
				imarkerText.setLabel(String.format("%s: %.2f %s: %.2f",
						trade.getEntry().getType().toString(),
						entryBar.getClosePrice().doubleValue(),
						trade.getExit().getType().toString(),
						exitBar.getClosePrice().doubleValue()));
				imarkerText.setLabelPaint(Color.BLACK);
				imarkerText.setLabelBackgroundColor(new Color(0,0,0,0));
				imarkerText.setLabelAnchor(RectangleAnchor.TOP);
				imarkerText.setLabelTextAnchor(TextAnchor.TOP_CENTER);

				mainPlot.addDomainMarker(imarkerText);
				*/
				mainPlot.addDomainMarker(imarker, Layer.BACKGROUND);
			}
		} else{
			log.error("No closed trade in trading record!");
		}

		return this;
	}

	private void removeDomainMarkers(XYPlot plot) {
		Collection domainMarkers = plot.getDomainMarkers(Layer.FOREGROUND);
		Collection domainMarkers2 = plot.getDomainMarkers(Layer.BACKGROUND);
		removeMarkers(plot, domainMarkers);
		removeMarkers(plot, domainMarkers2);
	}

	private void removeMarkers(XYPlot plot, Collection markers) {
		if(markers != null){
			Iterator iterator = markers.iterator();
			while (iterator.hasNext()) {
				Object markerObject = iterator.next();
				if (markerObject instanceof Marker) {
					plot.removeDomainMarker((Marker) markerObject);
				}
			}
		}
	}

	/**
	 * Builds and shows the chart panel in a JFrame with BorderLayout
	 */
	public void buildAndShow() {
		javax.swing.SwingUtilities.invokeLater(() -> {
			final JFrame frame = new JFrame("Ta4j charting");
			frame.setLayout(new BorderLayout());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// 4 add the plot to a JFrame
			frame.add(this.build());
			frame.pack();
			frame.setVisible(true);
		});
	}
}
