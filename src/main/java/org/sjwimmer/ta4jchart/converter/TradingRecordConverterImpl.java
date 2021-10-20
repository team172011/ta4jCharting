package org.sjwimmer.ta4jchart.plotter;

import org.ta4j.core.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TradingRecordConverterImpl implements TradingRecordConverter {

    private final Map<String, Map<Integer, Order.OrderType>> tradingRecordValues = new LinkedHashMap<>();

    @Override
    public void addTradingRecord(String name, TradingRecord tradingRecord) {
        Objects.requireNonNull(name, "Name cannot be nulL!");
        Objects.requireNonNull(tradingRecord, "Trading record cannot be null!");
        tradingRecordValues.put(name, createValues(tradingRecord));
    }

    private Map<Integer, Order.OrderType> createValues(TradingRecord tradingRecord) {
        return tradingRecord.getTrades().stream()
                .flatMap(t -> Stream.of(t.getEntry(), t.getEntry()))
                .collect(Collectors.toMap(Order::getIndex, Order::getType));
    }
    
    @Override
    public Map<Integer, Order.OrderType> getValues(String name) {
        return this.tradingRecordValues.get(name);
    }

    @Override
    public List<String> getNames() {
        return new ArrayList<>(this.tradingRecordValues.keySet());
    }


}
