package org.sjwimmer.ta4jchart.converter;

import org.ta4j.core.Bar;

import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Converter class should be used to convert ta4j objects like indicators, time series or trading records
 * into values for the chart.
 */
public interface Converter<T, R> extends Function<T, R> {

	default String getName(T element){
		return String.valueOf(element).replaceAll("Indicator", "").replaceAll("barCount", "");
	}

	default long getMilliseconds(Bar currentBar) {
		return currentBar.getEndTime().toEpochSecond() * 1000;
	}
}
