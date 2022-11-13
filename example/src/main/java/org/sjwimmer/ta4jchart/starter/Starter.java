package org.sjwimmer.ta4jchart.starter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

import java.awt.Color;

import de.sjwimmer.ta4jchart.chartbuilder.ChartType;
import de.sjwimmer.ta4jchart.chartbuilder.PlotType;
import de.sjwimmer.ta4jchart.chartbuilder.TacChartBuilder;
import de.sjwimmer.ta4jchart.chartbuilder.renderer.Theme;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.Returns;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.ParabolicSarIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.VolumeIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import static de.sjwimmer.ta4jchart.chartbuilder.IndicatorConfiguration.Builder.of;

public class Starter {

	public static void main(String[] args) {
		// 1 Create a barSeries, indicators and run your strategy with ta4j
		final BarSeries barSeries = loadAppleIncSeries();
		final VolumeIndicator volume = new VolumeIndicator(barSeries);
		final ParabolicSarIndicator parabolicSar = new ParabolicSarIndicator(barSeries);
		final ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);
		final EMAIndicator longEma = new EMAIndicator(closePrice, 12);
		final EMAIndicator shortEma = new EMAIndicator(closePrice, 4);
		final CrossedDownIndicatorRule exit = new CrossedDownIndicatorRule(shortEma, longEma);
		final CrossedUpIndicatorRule entry = new CrossedUpIndicatorRule(shortEma, longEma);

		final Strategy strategy = new BaseStrategy(entry, exit);
		final TradingRecord tradingRecord = new BarSeriesManager(barSeries).run(strategy);

		final Returns returns = new Returns(barSeries, tradingRecord, Returns.ReturnType.ARITHMETIC);

		// 2 Use the ChartBuilder to create a plot with barSeries, indicators and trading record
		TacChartBuilder.of(barSeries, Theme.LIGHT)
				.withIndicator(
						of(shortEma)
								.name("Short Ema")
								.color(Color.BLUE)) // default: ChartType.LINE, PlotType.OVERLAY
				.withIndicator(
						of(volume)
								.name("Volume")
								.plotType(PlotType.SUBPLOT)
								.chartType(ChartType.BAR)
								.color(Color.BLUE))
				.withIndicator(
						of(parabolicSar)  // default name = toString()
								.plotType(PlotType.OVERLAY)
								.chartType(ChartType.LINE)
								.color(Color.MAGENTA))
				.withIndicator(
						of(longEma)
								.name("Long Ema")
								.plotType(PlotType.SUBPLOT)
								.chartType(ChartType.LINE)) // random color
				.withIndicator(
						of(returns)
								.name("Returns")
								.plotType(PlotType.SUBPLOT)
								.color(Color.BLACK) // default: ChartType.LINE
								.notInTable()) // do not show entries in data table
				.withTradingRecord(tradingRecord)
				.buildAndShow(); // Creates and displays the JPanel in a JFrame
	}

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * @return the bar series from Apple Inc. bars.
     */
    public static BarSeries loadAppleIncSeries() {
        return loadCsvSeries("appleinc_bars_from_20130101_usd.csv");
    }

    public static BarSeries loadCsvSeries(String filename) {
		try {
			final InputStream inputStream = Starter.class.getClassLoader().getResourceAsStream(filename);
			assert inputStream != null;
			final BarSeries series = new BaseBarSeries("AAPL");
			new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
					.lines()
					.forEach(st -> {
						final String[] line = st.split(",");
						final ZonedDateTime date = LocalDate.parse(line[0], DATE_FORMAT).atStartOfDay(ZoneId.systemDefault());
						double open = Double.parseDouble(line[1]);
						double high = Double.parseDouble(line[2]);
						double low = Double.parseDouble(line[3]);
						double close = Double.parseDouble(line[4]);
						double volume = Double.parseDouble(line[5]);
						series.addBar(date, open, high, low, close, volume);
					});
	        	return series;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

	public static DefaultMetalTheme DarkMetalTheme = new DefaultMetalTheme(){
		//Wichtig
		public ColorUIResource getWindowTitleInactiveBackground() {
			return new ColorUIResource(java.awt.Color.GRAY);
		}

		//Wichtig:
		public ColorUIResource getWindowTitleBackground() {
			return new ColorUIResource(java.awt.Color.YELLOW);
		}

		public ColorUIResource getPrimaryControlHighlight() {
			return new ColorUIResource(java.awt.Color.BLACK);
		}

		public ColorUIResource getPrimaryControlDarkShadow() {
			return new ColorUIResource(java.awt.Color.LIGHT_GRAY);
		}

		public ColorUIResource getPrimaryControl() {
			return new ColorUIResource(java.awt.Color.GRAY);
		}

		public ColorUIResource getControlHighlight() {
			return new ColorUIResource(java.awt.Color.BLACK);
		}

		public ColorUIResource getControlDarkShadow() {
			return new ColorUIResource(java.awt.Color.BLACK);
		}

		public ColorUIResource getControl() {
			return new ColorUIResource(java.awt.Color.DARK_GRAY);
		}
	};
}
