package nl.zeesoft.zdk;

import java.util.Calendar;

public class TimeStamp {
	/**
	 * Formats the date object to 'YYYY-MM-DD HH:MM:SS:MS0'
	 * 
	 * @return The formatted date string
	 */
	public static Str getDateTimeString() {
		return getDateTimeString(true,true);
	}

	/**
	 * Formats the date object to 'YYYY-MM-DD||DD-MM-YYYY HH:MM:SS[:MS0]'
	 * 
	 * @param ymd Indicates the year should be first in the date format
	 * @param ms Indicates milliseconds should be included in the time format
	 * @return The formatted date string
	 */
	public static Str getDateTimeString(boolean ymd,boolean ms) {
		Str r = new Str();
		r.sb().append(getDateString(ymd));
		r.sb().append(" ");
		r.sb().append(getTimeString(ms));
		return r;
	}

	/**
	 * Formats the date object to 'YYYY-MM-DD||DD-MM-YYYY'
	 * 
	 * @param ymd Indicates the year should be first in the date format
	 * @return The formatted date string
	 */
	public static Str getDateString(boolean ymd) {
		Calendar cal = Calendar.getInstance();
		Str ds = new Str();
		if (ymd) {
			ds.sb().append(String.format("%04d",cal.get(Calendar.YEAR)));
			ds.sb().append("-");
			ds.sb().append(String.format("%02d",cal.get(Calendar.MONTH) + 1));
			ds.sb().append("-");
			ds.sb().append(String.format("%02d",cal.get(Calendar.DATE)));
		} else {
			ds.sb().append(String.format("%02d",cal.get(Calendar.DATE)));
			ds.sb().append("-");
			ds.sb().append(String.format("%02d",cal.get(Calendar.MONTH) + 1));
			ds.sb().append("-");
			ds.sb().append(String.format("%04d",cal.get(Calendar.YEAR)));
		}
		return ds;
	}

	/**
	 * Formats the date object to 'HH:MM:SS[:MS0]'
	 * 
	 * @param ms Indicates milliseconds should be included in the time format
	 * @return The formatted date string
	 */
	public static Str getTimeString(boolean ms) {
		Calendar cal = Calendar.getInstance();
		Str ts = new Str();
		ts.sb().append(String.format("%02d",cal.get(Calendar.HOUR_OF_DAY)));
		ts.sb().append(":");
		ts.sb().append(String.format("%02d",cal.get(Calendar.MINUTE)));
		ts.sb().append(":");
		ts.sb().append(String.format("%02d",cal.get(Calendar.SECOND)));
		if (ms) {
			ts.sb().append(":");
			ts.sb().append(String.format("%03d",cal.get(Calendar.MILLISECOND)));
		}
		return ts;
	}
}
