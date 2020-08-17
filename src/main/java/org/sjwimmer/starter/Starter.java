package org.sjwimmer.starter;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import javax.swing.JFrame;

import org.sjwimmer.ta4jchart.chartbuilder.ChartBuilder;
import org.sjwimmer.ta4jchart.chartbuilder.ChartBuilderImpl;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.num.Num;


public class Starter {

	public static void main(String[] args) {
		
		// 1 Create some barSeries and indicators
		BarSeries barSeries = loadAppleIncSeries();
		HighPriceIndicator highPrice = new HighPriceIndicator(barSeries);
		ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);
		
		// 2 Add them to a ChartBuilder instance
		ChartBuilder chartBuilder = new ChartBuilderImpl();
		chartBuilder.addBarSeries(barSeries);
		chartBuilder.addIndicator("High Price Indicator", highPrice);
		chartBuilder.addIndicator("Close Price Indicator", closePrice);
		
		javax.swing.SwingUtilities.invokeLater(() -> {

		    JFrame frame = new JFrame("Ta4j charting");
		    frame.setLayout(new BorderLayout());
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    
		    // 3 add the plot to a JFrame
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

        URI uri;
		try {
			uri = Starter.class.getClassLoader().getResource(filename).toURI();
			BarSeries series = new BaseBarSeries("AAPL");

	        try(Stream<String> lines = Files.lines(Paths.get(uri))){
	        	lines.forEach(st -> {
		        	String[] line = st.split(",");
		            ZonedDateTime date = LocalDate.parse(line[0], DATE_FORMAT).atStartOfDay(ZoneId.systemDefault());
		            double open = Double.parseDouble(line[1]);
		            double high = Double.parseDouble(line[2]);
		            double low = Double.parseDouble(line[3]);
		            double close = Double.parseDouble(line[4]);
		            double volume = Double.parseDouble(line[5]);
		            series.addBar(date, open, high, low, close, volume);
	        	});
	        	return series;
	        }
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

}
