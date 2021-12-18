package org.sjwimmer.ta4jchart.converter;

import org.ta4j.core.Bar;
import org.ta4j.core.analysis.Returns;

import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Converter class should be used to convert ta4j objects like indicators, time series or trading records
 * into values for the chart.
 */
public interface Converter<T, R> extends Function<T, R> {

	default String getName(T element) {
		return getNameFunction().apply(element);
	}

	default long getMilliseconds(Bar currentBar) {
		return currentBar.getEndTime().toEpochSecond() * 1000;
	}

	default Function<T, String> getNameFunction(){
		return element -> {
			if(element instanceof Returns) {
				return "Returns";
			}
			return String.valueOf(element).replaceAll("Indicator", "").replaceAll("barCount", "");
		};
	}
}
