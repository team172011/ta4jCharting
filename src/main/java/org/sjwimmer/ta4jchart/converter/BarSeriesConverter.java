package org.sjwimmer.ta4jchart.plotter;

import java.util.Date;
import java.util.List;

public interface BarSeriesConverter extends Converter {
	String getBarSeriesName();
	List<Date> getDates();
	List<Number> getOpenData();
	List<Number> getLowData();
	List<Number> getHighData();
	List<Number> getCloseData();
	List<Number> getVolumeData();
}
