package org.sjwimmer.ta4jchart.chartbuilder;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;

public class GlobalConstants {

    public static final DateFormatter DATE_FORMATTER = new DateFormatter(DateFormat.getDateInstance(DateFormat.SHORT));

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
}
