package com.salesforce.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Utilities {
	
	
	public static double roundValue(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	public static long calculateDaysDiff(String startDate, String endDate, String dateFormat) throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
	    Date firstDate = sdf.parse(startDate);
	    Date secondDate = sdf.parse(endDate);

	    long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
	    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    return diff;
	}
	
	


}
