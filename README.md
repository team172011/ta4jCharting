[![Test develop](https://github.com/team172011/ta4jCharting/actions/workflows/test-action-master.yml/badge.svg?branch=master&event=push)](https://github.com/team172011/ta4jCharting/actions/workflows/test-action-master.yml) [![Test develop](https://github.com/team172011/ta4jCharting/actions/workflows/test-action-develop.yml/badge.svg?branch=develop&event=push)](https://github.com/team172011/ta4jCharting/actions/workflows/test-action-develop.yml)
# Ta4jCharting
A simple charting application for [ta4j](https://github.com/ta4j/ta4j).

## How to build and start the example
```shell
> git clone https://github.com/team172011/ta4jCharting.git
> cd ta4jCharting
> mvn clean package
> java -jar target/ta4j-charting-0.0.1-SNAPSHOT.jar
```

## How to use
```java
// 1 Create a barSeries, indicators and run your strategy with ta4j
final BarSeries barSeries = loadAppleIncSeries();
final VolumeIndicator volume = new VolumeIndicator(barSeries);
final ParabolicSarIndicator parabolicSar = new ParabolicSarIndicator(barSeries);
final ClosePriceIndicator closePrice = new ClosePriceIndicator(barSeries);
final EMAIndicator longEma = new EMAIndicator(closePrice, 12);
final EMAIndicator shortEma = new EMAIndicator(closePrice, 4);
final CrossedDownIndicatorRule exit = new CrossedDownIndicatorRule(shortEma, longEma);
final CrossedUpIndicatorRule entry = new CrossedUpIndicatorRule(shortEma, longEma);

final Strategy strategy = new BaseStrategy(entry, exit);
final TradingRecord tradingRecord = new BarSeriesManager(barSeries).run(strategy);

final Returns returns = new Returns(barSeries, tradingRecord, Returns.ReturnType.ARITHMETIC);

// 2 Use the ChartBuilder to create a plot with barSeries, indicators and trading record
TacChartBuilder.of(barSeries)
    .withIndicator(
        of(shortEma)
            .name("Short Ema")
            .color(Color.BLUE)) // default: ChartType.LINE, PlotType.OVERLAY
    .withIndicator(
        of(volume)
            .name("Volume")
            .plotType(PlotType.SUBPLOT)
            .chartType(ChartType.BAR)
            .color(Color.BLUE))
    withIndicator(
        of(parabolicSar)  // default name = toString()
            .plotType(PlotType.OVERLAY)
            .chartType(ChartType.LINE)
            .color(Color.MAGENTA))
    .withIndicator(
        of(longEma)
            .name("Long Ema")
            .plotType(PlotType.SUBPLOT)
            .chartType(ChartType.LINE)) // random color
    .withIndicator(
        of(returns)
            .name("Returns")
            .plotType(PlotType.SUBPLOT)
            .color(Color.BLACK)) // default: ChartType.LINE
    .withTradingRecord(tradingRecord)
    .buildAndShow(); // Creates and displays the JPanel in a JFrame
```
![Example picture](repo/example1.png)
