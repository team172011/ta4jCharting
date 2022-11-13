package de.sjwimmer.ta4jchart.chartbuilder;

import de.sjwimmer.ta4jchart.chartbuilder.converter.*;
import de.sjwimmer.ta4jchart.chartbuilder.renderer.*;
import de.sjwimmer.ta4jchart.chartbuilder.tradingrecord.TradingRecordPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import de.sjwimmer.ta4jchart.chartbuilder.data.TacDataTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.*;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Date;

public class TacChartBuilder {

	private final static Logger log = LoggerFactory.getLogger(TacChartBuilder.class);
	private final TacChartTheme theme;
	private final BarSeries barSeries;
	private final BarSeriesConverter barSeriesConverter;
	private final IndicatorToTimeSeriesConverter indicatorToTimeSeriesConverter;
	private final IndicatorToBarDataConverter indicatorToBarDataConverter;
	private final JFreeChart chart;

	private final TacDataTableModel dataTableModel = new TacDataTableModel();

	private int overlayIds = 2; // 0 = ohlcv data, 1 = volume data
	private TradingRecordPanel tradingRecordPanel;

	public static TacChartBuilder of(BarSeries barSeries) {
		return of(barSeries, Theme.LIGHT);
	}

	public static TacChartBuilder of(BarSeries barSeries, Theme theme) {
		return new TacChartBuilder(barSeries, theme);
	}

	private TacChartBuilder(BarSeries barSeries, Theme theme) {
		this(barSeries, new BarSeriesConverterImpl(), new IndicatorToTimeSeriesConverterImpl(), new IndicatorToBarDataConverterImpl(), theme);
	}
	
	private TacChartBuilder(BarSeries barSeries, BarSeriesConverter barseriesPlotter, IndicatorToTimeSeriesConverter indicatorConverter, IndicatorToBarDataConverter indicatorToBarDataConverter, Theme theme) {
		if(theme == Theme.DARK) {
			this.theme = new DarkTacChartTheme();
		} else {
			this.theme = new LightTacChartTheme();
		}
		this.barSeriesConverter = barseriesPlotter;
		this.indicatorToTimeSeriesConverter = indicatorConverter;
		this.indicatorToBarDataConverter = indicatorToBarDataConverter;
		this.barSeries = barSeries;
		this.chart = createCandlestickChart(this.barSeries);
	}

	/**
	 * Builds the chart
	 * @return a JPanel holding all ta4j-charting elements
	 */
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
		setPlotTheme(plot);
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

	/**
	 * Adds an indicator to the chart. The indicator can be configured with help of the {@link IndicatorConfiguration}
	 * @param indicatorConfigurationBuilder the indicatorConfiguration with the {@link Indicator}ndicator
	 * @return the {@link TacChartBuilder builder}
	 */
	public TacChartBuilder withIndicator(IndicatorConfiguration.Builder<?> indicatorConfigurationBuilder) {
		final CombinedDomainXYPlot combinedDomainPlot = (CombinedDomainXYPlot) this.chart.getPlot();
		final IndicatorConfiguration<?> indicatorConfiguration = indicatorConfigurationBuilder.build();
		final Indicator<?> indicator = indicatorConfiguration.getIndicator();
		final PlotType plotType = indicatorConfiguration.getPlotType();
		final ChartType chartType = indicatorConfiguration.getChartType();
		final String name = indicatorConfiguration.getName();
		final boolean inDataTable = indicatorConfiguration.isAddToDataTable();

		if(plotType == PlotType.OVERLAY) {
			if(chartType == ChartType.LINE) {
				final int counter = overlayIds++;
				final TimeSeriesCollection timeSeriesCollection = this.indicatorToTimeSeriesConverter.convert(indicator, name);
				final XYLineAndShapeRenderer renderer = createLineRenderer(indicatorConfiguration);
				final XYPlot candlestickPlot = (XYPlot) combinedDomainPlot.getSubplots().get(0);
				setPlotTheme(candlestickPlot);
				candlestickPlot.setRenderer(counter, renderer);
				candlestickPlot.setDataset(counter, timeSeriesCollection);
				if (inDataTable) {
					this.dataTableModel.addEntries(timeSeriesCollection);
				}
			} else if(chartType == ChartType.BAR) {
				final int counter = overlayIds++;
				final TacBarDataset barDataset = indicatorToBarDataConverter.convert(indicator, name);
				final TacBarRenderer barRenderer = createBarRenderer(indicatorConfiguration);
				final XYPlot candlestickPlot = (XYPlot) combinedDomainPlot.getSubplots().get(0);
				setPlotTheme(candlestickPlot);
				candlestickPlot.setRenderer(counter, barRenderer);
				candlestickPlot.setDataset(counter, barDataset);
				if(inDataTable) {
					this.dataTableModel.addEntries(barDataset);
				}
			}
		} else if (plotType == PlotType.SUBPLOT) {
			if(chartType == ChartType.BAR) {
				final TacBarDataset barDataset = indicatorToBarDataConverter.convert(indicator, name);
				final NumberAxis valueAxis = new NumberAxis(name);
				final TacBarRenderer barRenderer = createBarRenderer(indicatorConfiguration);
				final XYPlot barPlot = new XYPlot(barDataset, null, valueAxis, barRenderer);
				setPlotTheme(barPlot);
				valueAxis.setLabel("");
				combinedDomainPlot.add(barPlot, 1);
				if (inDataTable) {
					this.dataTableModel.addEntries(barDataset);
				}
			} else if (chartType == ChartType.LINE) {
				final TimeSeriesCollection timeSeriesCollection = this.indicatorToTimeSeriesConverter.convert(indicator, name);
				final XYLineAndShapeRenderer renderer = createLineRenderer(indicatorConfiguration);
				final NumberAxis valueAxis = new NumberAxis(name);
				final XYPlot linePlot = new XYPlot(timeSeriesCollection, null, valueAxis, renderer);
				setPlotTheme(linePlot);
				valueAxis.setLabel("");
				valueAxis.setAutoRangeIncludesZero(false);
				if (inDataTable) {
					this.dataTableModel.addEntries(timeSeriesCollection);
				}
				combinedDomainPlot.add(linePlot, 1);
			}
		}
		return this;
	}

	private void setPlotTheme(XYPlot plot) {
		final Color labelColor = UIManager.getColor("Label.foreground");
		plot.setBackgroundPaint(UIManager.getColor("Panel.background"));
		if(plot.getRangeAxis() != null) {
			plot.getRangeAxis().setTickLabelPaint(labelColor);
			plot.getRangeAxis().setLabelPaint(labelColor);
		}
		if(plot.getDomainAxis() != null) {
			plot.getDomainAxis().setTickLabelPaint(labelColor);
			plot.getDomainAxis().setLabelPaint(labelColor);
		}
	}

	private TacBarRenderer createBarRenderer(IndicatorConfiguration<?> indicatorConfiguration) {
		return new TacBarRenderer(indicatorConfiguration.getColor());
	}

	private XYLineAndShapeRenderer createLineRenderer(IndicatorConfiguration<?> indicatorConfiguration) {
		XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer();
		lineRenderer.setSeriesShape(0, indicatorConfiguration.getShape());
		lineRenderer.setSeriesPaint(0, indicatorConfiguration.getColor());
		return lineRenderer;
	}


	public TacChartBuilder withTradingRecord(TradingRecord tradingRecord) {
		this.tradingRecordPanel = new TradingRecordPanel(tradingRecord);
		if(tradingRecord.getLastExit() != null){
			final XYPlot mainPlot = ((XYPlot)((CombinedDomainXYPlot) chart.getPlot()).getSubplots().get(0));
			final java.util.List<Position> trades = tradingRecord.getPositions();
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
				out.setLabel(""); // profit
				out.setLabelPaint(Color.BLACK);
				out.setLabelFont(labelFont);
				out.setLabelBackgroundColor(new Color(profitColor.getRed(), profitColor.getGreen(), profitColor.getBlue(), 200));
				out.setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
				out.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
				out.setPaint(markerColor);
				mainPlot.addDomainMarker(out);

				IntervalMarker imarker = new IntervalMarker(entry, exit, profitColor);
				imarker.setAlpha(0.1f);


				IntervalMarker imarkerText = new IntervalMarker(entry, exit, new Color(0,0,0,0));
				imarkerText.setLabelFont(labelFont);
				imarkerText.setLabel(profit+"");
				imarkerText.setLabelPaint(markerColor);
				imarkerText.setLabelBackgroundColor(new Color(profitColor.getRed(), profitColor.getGreen(), profitColor.getBlue(), 200));
				imarkerText.setLabelAnchor(RectangleAnchor.BOTTOM);
				imarkerText.setLabelTextAnchor(TextAnchor.BOTTOM_CENTER);

				mainPlot.addDomainMarker(imarkerText);

				mainPlot.addDomainMarker(imarker, Layer.BACKGROUND);
			}
		} else{
			log.error("No closed trade in trading record!");
		}

		return this;
	}

	private void removeDomainMarkers(XYPlot plot) {
		Collection<?> domainMarkers = plot.getDomainMarkers(Layer.FOREGROUND);
		Collection<?> domainMarkers2 = plot.getDomainMarkers(Layer.BACKGROUND);
		removeMarkers(plot, domainMarkers);
		removeMarkers(plot, domainMarkers2);
	}

	private void removeMarkers(XYPlot plot, Collection<?> markers) {
		if(markers != null){
			for (Object markerObject : markers) {
				if (markerObject instanceof Marker) {
					plot.removeDomainMarker((Marker) markerObject);
				}
			}
		}
	}

	public void buildAndShow(String title) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			final JFrame frame = new JFrame(title);
			frame.setLayout(new BorderLayout());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(this.build());
			frame.pack();
			frame.setVisible(true);
		});
	}

	/**
	 * Builds and shows the chart panel in a JFrame with BorderLayout
	 */
	public void buildAndShow(){
		buildAndShow("Ta4j charting");
	}
}
