package de.sjwimmer.ta4jchart.chartbuilder.converter;

import org.ta4j.core.Bar;
import org.ta4j.core.Indicator;
import org.ta4j.core.analysis.Returns;
import org.ta4j.core.num.Num;

import java.util.function.Function;

/**
 * Converter class should be used to convert ta4j objects like indicators, time series or trading records
 * into values for the chart.
 */
public interface Converter<T, R> {

	default long getMilliseconds(Bar currentBar) {
		return currentBar.getEndTime().toEpochSecond() * 1000;
	}

	R convert(T in, String name);

	default double extractDoubleValue(Indicator<?> indicator, int index) {
		final Object value = indicator.getValue(index);
		if (value instanceof Num) {
			return ((Num)value).doubleValue();
		} else if (value instanceof Boolean) {
			return ((Boolean)value) ? 1d : 0d;
		} else {
			throw new IllegalArgumentException("Cannot convert type " + value.getClass() + " to TimeSeriesCollection!");
		}
	}
}
