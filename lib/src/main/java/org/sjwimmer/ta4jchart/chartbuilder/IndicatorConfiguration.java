package org.sjwimmer.ta4jchart.chartbuilder;

import org.ta4j.core.Indicator;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class IndicatorConfiguration<T> {

    private final Indicator<T> indicator;
    private final String name;
    private final PlotType plotType;
    private final ChartType chartType;
    private final Color color;
    private final Shape shape;
    private final boolean addToDataTable;

    public IndicatorConfiguration(Builder<T> builder) {
        this.plotType = builder.plotType;
        this.chartType = builder.chartType;
        this.name = builder.name;
        this.color = builder.color;
        this.shape = builder.shape;
        this.indicator = builder.indicator;
        this.addToDataTable = builder.addToDataTable;
    }


    public PlotType getPlotType() {
        return this.plotType;
    }

    public ChartType getChartType() {
        return this.chartType;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }

    public Shape getShape() {
        return this.shape;
    }

    public Indicator<T> getIndicator() {
        return this.indicator;
    }

    public boolean isAddToDataTable() {
        return addToDataTable;
    }

    public static class Builder<T> {
        private final Random random = new Random();

        private final Indicator<T> indicator;
        private String name;
        private Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        private PlotType plotType = PlotType.OVERLAY;
        private ChartType chartType = ChartType.LINE;
        private boolean addToDataTable = true;
        private Shape shape = new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0);

        public static<T> Builder<T> of(Indicator<T> indicator) {
            return new Builder<>(indicator);
        }

        private Builder(Indicator<T> indicator){
            this.indicator = indicator;
            this.name = indicator.toString();
        }

        public Builder<T> chartType(ChartType chartType){
            this.chartType = chartType;
            return this;
        }

        public Builder<T> plotType(PlotType plotType){
            this.plotType = plotType;
            return this;
        }

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> color(Color color) {
            this.color = color;
            return this;
        }

        public Builder<T> shape(Shape shape) {
            this.shape = shape;
            return this;
        }

        public Builder<T> notInTable() {
            this.addToDataTable = false;
            return this;
        }

        public IndicatorConfiguration<T> build() {
            return new IndicatorConfiguration<>(this);
        }
    }
}
