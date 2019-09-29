package nl.zeesoft.zdk.htm.sdr;

import nl.zeesoft.zdk.ZDate;

public class DateTimeValue {
	public long		dateTime	= 0;
	public float	value		= 0;
	public String	label		= "";
	
	@Override
	public String toString() {
		String r = "";
		ZDate date = new ZDate();
		date.setTime(dateTime);
		r = date.getDateTimeString() + " " + value + " " + label;
		if (label!=null && label.length()>0) {
			r += " " + label;
		}
		return r;
	}
}
