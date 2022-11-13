package de.sjwimmer.ta4jchart.chartbuilder;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class BarSeriesHelper {

    /**
     * Creates a series of mock bars with given close prices
     * @param closes the expected close prices of the bars
     * @return a series of mock bars with given close prices
     */
    public static List<Bar> createBars(final double... closes){
        final List<Bar> bars = new ArrayList<>(closes.length);
        for(var i = 0; i < closes.length; i ++){
            bars.add(new BaseBar(Duration.ofDays(1), ZonedDateTime.now().minusDays((closes.length - i)), closes[i], closes[i] + (double) i, Math.max(0, closes[i] - i), closes[i], closes[i] * (double) i));
        }
        return bars;
    }

    /**
     * Creates a series of mock bars
     * @return a series of mock bars with given close prices
     */
    public static List<Bar> createBars(){
        return createBars(32, 26, 28, 20, 21, 23, 24, 23, 26);
    }
}
