package org.sjwimmer.ta4jchart.plotter;

import org.ta4j.core.Order;
import org.ta4j.core.TradingRecord;

import java.util.List;
import java.util.Map;

public interface TradingRecordConverter extends Converter {

	void addTradingRecord(String name, TradingRecord tradingRecord);

	Map<Integer, Order.OrderType> getValues(String name);

	List<String> getNames();
}
