package nl.zeesoft.zdk;

import java.util.Calendar;
import java.util.Date;

/**
 * Zeesoft Date object.
 * 
 * Used to create time stamps.
 */
public class ZDate {
	private Date date	= new Date();

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		if (date!=null) {
			this.date = date;
		}
	}
	
	/**
	 * Formats the date object to 'YYYY-MM-DD HH:MM:SS:MS0'
	 * 
	 * @return The formatted date string
	 */
	public String getDateTimeString() {
		return getDateTimeString(true,true);
	}

	public String getDateTimeString(boolean ymd,boolean ms) {
		return getDateString(ymd) + " " + getTimeString(ms);
	}

	public String getDateString(boolean ymd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String ds = "";
		if (ymd) {
			ds = 
				minStrInt(cal.get(Calendar.YEAR),2) + "-" + 
				minStrInt(cal.get(Calendar.MONTH) + 1,2) + "-" + 
				minStrInt(cal.get(Calendar.DATE),2);
		} else {
			ds = 
				minStrInt(cal.get(Calendar.DATE),2) + "-" +
				minStrInt(cal.get(Calendar.MONTH) + 1,2) + "-" + 
				minStrInt(cal.get(Calendar.YEAR),2);
		}
		return ds;
	}

	public String getTimeString(boolean ms) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String ts = 
			minStrInt(cal.get(Calendar.HOUR_OF_DAY),2) + ":" + 
			minStrInt(cal.get(Calendar.MINUTE),2) + ":" + 
			minStrInt(cal.get(Calendar.SECOND),2);
			;
		if (ms) {
			ts = ts + ":" + minStrInt(cal.get(Calendar.MILLISECOND),3);
		}
		return ts;
	}

	public String getDurationString(boolean includeSeconds, boolean includeMilliSeconds) {
		long milliSeconds = date.getTime();
		long hours = (milliSeconds - (milliSeconds % 3600000)) / 3600000;
		long remaining = (milliSeconds - (hours * 3600000));
		long minutes = (remaining - (remaining % 60000)) / 60000;
		remaining = (remaining - (minutes * 60000));
		long seconds = (remaining - (remaining % 1000)) / 1000;
		remaining = (remaining - (seconds * 1000));
		String ds = "";
		if (includeMilliSeconds) {
			ds = hours + ":" + minutes + ":" + seconds + ":" + remaining;
		} else if (includeSeconds) {
			ds = hours + ":" + minutes + ":" + seconds;
		} else {
			ds = hours + ":" + minutes;
		}
		return ds;
	}

	private String minStrInt(int i, int l) {
		String s = "" + i;
		int sl = s.length();
		if (sl<l) {
			for (int z = 0; z < (l - sl); z++) {
				s = "0" + s;
			}
		}
		return s;
	}
}
