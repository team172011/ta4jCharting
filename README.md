[![Test develop](https://github.com/team172011/ta4jCharting/actions/workflows/test-action-master.yml/badge.svg?branch=master&event=push)](https://github.com/team172011/ta4jCharting/actions/workflows/test-action-master.yml) [![Test develop](https://github.com/team172011/ta4jCharting/actions/workflows/test-action-develop.yml/badge.svg?branch=develop&event=push)](https://github.com/team172011/ta4jCharting/actions/workflows/test-action-develop.yml)
# Ta4jCharting
A simple charting application for [ta4j](https://github.com/ta4j/ta4j).

## How to use
```java
        // 1 Create a barSeries, indicators and run your strategy
        BarSeries barSeries = loadAppleIncSeries();
        VolumeIndicator volume = new VolumeIndicator(barSeries);
        HighPriceIndicator highPrice = new HighPriceIndicator(barSeries);
        ParabolicSarIndicator parabolicSar = new ParabolicSarIndicator(barSeries);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);
        EMAIndicator longEma = new EMAIndicator(closePrice, 20);
        EMAIndicator shortEma = new EMAIndicator(closePrice, 6);
        CrossedDownIndicatorRule exit = new CrossedDownIndicatorRule(shortEma, longEma);
        CrossedUpIndicatorRule entry = new CrossedUpIndicatorRule(shortEma, longEma);

        Strategy strategy = new BaseStrategy(entry, exit);
        TradingRecord tradingRecord = new BarSeriesManager(barSeries).run(strategy);

        // 2 Add your ta4j objects to a ChartBuilder instance
        ChartBuilder chartBuilder = new ChartBuilderImpl(barSeries);

        chartBuilder.addIndicator(volume, PlotType.SUBPLOT, ChartType.BAR);
        chartBuilder.addIndicator(parabolicSar, PlotType.OVERLAY, ChartType.LINE);
        chartBuilder.addIndicator(longEma, PlotType.SUBPLOT, ChartType.LINE);
        chartBuilder.addIndicator(shortEma); // default: PlotType.OVERLAY, ChartType.LINE
        chartBuilder.setTradingRecord(tradingRecord);

        // 3 Create and show a JFrame with the chart
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
