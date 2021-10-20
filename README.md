# Ta4jCharting
A simple charting application for [ta4j](https://github.com/ta4j/ta4j).

## How to use
```java
// 1 Create a barSeries, indicators and run your strategy
BarSeries barSeries = loadAppleIncSeries();
HighPriceIndicator highPrice = new HighPriceIndicator(barSeries);
ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);
EMAIndicator longEma = new EMAIndicator(closePrice, 20);
EMAIndicator shortEma = new EMAIndicator(closePrice, 6);
CrossedDownIndicatorRule exit = new CrossedDownIndicatorRule(shortEma, longEma);
CrossedUpIndicatorRule entry = new CrossedUpIndicatorRule(shortEma, longEma);

Strategy strategy = new BaseStrategy(entry, exit);
TradingRecord tradingRecord = new BarSeriesManager(barSeries).run(strategy);

// 2 Add your ta4j objects to a ChartBuilder instance
ChartBuilder chartBuilder = new ChartBuilderImpl(barSeries);
chartBuilder.addIndicator(highPrice);
chartBuilder.addIndicator(longEma);
chartBuilder.addIndicator(shortEma);
chartBuilder.setTradingRecord(tradingRecord);

// 3 Create JFrame
javax.swing.SwingUtilities.invokeLater(() -> {
    JFrame frame = new JFrame("Ta4j charting");
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // 4 add the plot to a JFrame
    frame.add(chartBuilder.createPlot());
    frame.pack();
    frame.setVisible(true);
});

```
![Example picture](repo/example1.png)
