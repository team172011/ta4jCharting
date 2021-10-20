package org.sjwimmer.ta4jchart.plotter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;

public class BarSeriesConverterImpl implements BarSeriesConverter {

	private final String barSeriesName;
	
	List<Number> openData = new ArrayList<>();
	List<Number> highData = new ArrayList<>();
	List<Number> lowData = new ArrayList<>();
	List<Number> closeData = new ArrayList<>();
	List<Number>volumeData = new ArrayList<>();
	List<Date> dates = new ArrayList<>();

	public BarSeriesConverterImpl(BarSeries barSeries){
		Objects.requireNonNull(barSeries, "Bar series can not be null!");
		this.barSeriesName = barSeries.getName();
		for(int i = barSeries.getBeginIndex(); i <= barSeries.getEndIndex(); i++) {
			addData(barSeries.getBar(i));
		}
	}

	private void addData(Bar bar) {
		openData.add(bar.getOpenPrice().getDelegate());
		highData.add(bar.getHighPrice().getDelegate());
		lowData.add(bar.getLowPrice().getDelegate());
		closeData.add(bar.getClosePrice().getDelegate());
		dates.add(Date.from(bar.getEndTime().toInstant()));
		volumeData.add(bar.getVolume().getDelegate());
	}


	@Override
	public List<Number> getOpenData() {
		return openData;
	}


	@Override
	public List<Number> getLowData() {
		return lowData;
	}


	@Override
	public List<Number> getHighData() {
		return highData;
	}


	@Override
	public List<Number> getCloseData() {
		return closeData;
	}

	@Override
	public List<Number> getVolumeData() {
		return this.volumeData;
	}


	@Override
	public String getBarSeriesName() {
		return this.barSeriesName;
	}

	@Override
	public List<Date> getDates() {
		return this.dates;
	}
}
