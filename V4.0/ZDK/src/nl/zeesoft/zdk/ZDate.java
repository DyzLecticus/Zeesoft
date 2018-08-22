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
	public ZStringBuilder getDateTimeString() {
		return getDateTimeString(true,true);
	}

	/**
	 * Formats the date object to 'YYYY-MM-DD||DD-MM-YYYY HH:MM:SS[:MS0]'
	 * 
	 * @param ymd Indicates the year should be first in the date format
	 * @param ms Indicates milliseconds should be included in the time format
	 * @return  The formatted date string
	 */
	public ZStringBuilder getDateTimeString(boolean ymd,boolean ms) {
		ZStringBuilder r = new ZStringBuilder();
		r.append(getDateString(ymd));
		r.append(" ");
		r.append(getTimeString(ms));
		return r;
	}

	/**
	 * Formats the date object to 'YYYY-MM-DD||DD-MM-YYYY'
	 * 
	 * @param ymd Indicates the year should be first in the date format
	 * @return  The formatted date string
	 */
	public ZStringBuilder getDateString(boolean ymd) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		ZStringBuilder ds = new ZStringBuilder();
		if (ymd) {
			ds.append(minStrInt(cal.get(Calendar.YEAR),2));
			ds.append("-");
			ds.append(minStrInt(cal.get(Calendar.MONTH) + 1,2));
			ds.append("-");
			ds.append(minStrInt(cal.get(Calendar.DATE),2));
		} else {
			ds.append(minStrInt(cal.get(Calendar.DATE),2));
			ds.append("-");
			ds.append(minStrInt(cal.get(Calendar.MONTH) + 1,2));
			ds.append("-");
			ds.append(minStrInt(cal.get(Calendar.YEAR),2));
		}
		return ds;
	}

	/**
	 * Formats the date object to 'HH:MM:SS[:MS0]'
	 * 
	 * @param ms Indicates milliseconds should be included in the time format
	 * @return  The formatted date string
	 */
	public ZStringBuilder getTimeString(boolean ms) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		ZStringBuilder ts = new ZStringBuilder();
		ts.append(minStrInt(cal.get(Calendar.HOUR_OF_DAY),2));
		ts.append(":");
		ts.append(minStrInt(cal.get(Calendar.MINUTE),2));
		ts.append(":");
		ts.append(minStrInt(cal.get(Calendar.SECOND),2));
		if (ms) {
			ts.append(":");
			ts.append(minStrInt(cal.get(Calendar.MILLISECOND),3));
		}
		return ts;
	}

	private ZStringBuilder minStrInt(int i, int l) {
		ZStringBuilder s = new ZStringBuilder("" + i);
		int sl = s.length();
		if (sl<l) {
			for (int z = 0; z < (l - sl); z++) {
				s.insert(0,"0");
			}
		}
		return s;
	}
}
