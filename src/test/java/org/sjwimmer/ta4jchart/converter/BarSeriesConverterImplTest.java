package org.sjwimmer.ta4jchart.converter;

import org.jfree.data.xy.OHLCDataset;
import org.junit.jupiter.api.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBarSeries;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BarSeriesConverterImplTest {


    @Test
    public void testCreateConverter(){
        final BarSeriesConverterImpl barSeriesConverter = new BarSeriesConverterImpl();
        final BaseBarSeries barSeries = new BaseBarSeries("test");
        OHLCDataset test = barSeriesConverter.apply(barSeries);
        String name = barSeriesConverter.getName(barSeries);

        assertEquals(barSeries.getName(),name);
    }
}
