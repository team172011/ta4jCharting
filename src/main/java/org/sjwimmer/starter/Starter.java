package org.sjwimmer.starter;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFrame;

import org.sjwimmer.ta4jchart.chartbuilder.ChartBuilder;
import org.sjwimmer.ta4jchart.chartbuilder.ChartBuilderImpl;
import org.sjwimmer.ta4jchart.chartbuilder.ChartType;
import org.sjwimmer.ta4jchart.chartbuilder.PlotType;
import org.ta4j.core.*;
import org.ta4j.core.analysis.Returns;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.ParabolicSarIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.VolumeIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;


public class Starter {

	public static void main(String[] args) {
		
		// 1 Create a barSeries, indicators and run your strategy
		final BarSeries barSeries = loadAppleIncSeries();
		final VolumeIndicator volume = new VolumeIndicator(barSeries);
		final ParabolicSarIndicator parabolicSar = new ParabolicSarIndicator(barSeries);
		final ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);
		final EMAIndicator longEma = new EMAIndicator(closePrice, 20);
		final EMAIndicator shortEma = new EMAIndicator(closePrice, 6);
		final CrossedDownIndicatorRule exit = new CrossedDownIndicatorRule(shortEma, longEma);
		final CrossedUpIndicatorRule entry = new CrossedUpIndicatorRule(shortEma, longEma);

		final Strategy strategy = new BaseStrategy(entry, exit);
		final TradingRecord tradingRecord = new BarSeriesManager(barSeries).run(strategy);

		final Returns returns = new Returns(barSeries, tradingRecord, Returns.ReturnType.ARITHMETIC);

		// 2 Add your ta4j objects to a ChartBuilder instance
		final ChartBuilder chartBuilder = new ChartBuilderImpl(barSeries);

		chartBuilder.addIndicator(volume, PlotType.SUBPLOT, ChartType.BAR);
		chartBuilder.addIndicator(parabolicSar, PlotType.OVERLAY, ChartType.LINE);
		chartBuilder.addIndicator(longEma, PlotType.SUBPLOT, ChartType.LINE);
		chartBuilder.addIndicator(shortEma); // default: PlotType.OVERLAY, ChartType.LINE
		chartBuilder.addIndicator(returns, PlotType.SUBPLOT); // default: ChartType.LINE
		chartBuilder.setTradingRecord(tradingRecord);

		// 3 Create JFrame
		javax.swing.SwingUtilities.invokeLater(() -> {
			final JFrame frame = new JFrame("Ta4j charting");
		    frame.setLayout(new BorderLayout());
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    
		    // 4 add the plot to a JFrame
		    frame.add(chartBuilder.createPlot());
		    frame.pack();
		    frame.setVisible(true);
		});
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
}
