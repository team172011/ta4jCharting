package org.sjwimmer.ta4jchart.chartbuilder;

public class BaseChartBuilderConfig implements ChartBuilderConfig {

    private boolean isPlotDataTable = true;

    @Override
    public boolean isPlotDataTable() {
        return isPlotDataTable;
    }

    public void setPlotDataTable(boolean isPlotDataTable){
        this.isPlotDataTable = isPlotDataTable;
    }
}
