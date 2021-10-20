package org.sjwimmer.ta4jchart.converter;

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
		return String.valueOf(element);
	}
}
