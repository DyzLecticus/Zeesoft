package nl.zeesoft.zdk.htm.enc;

import java.util.Calendar;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.util.SDR;

/**
 * A DateTimeEncoder is an example implementation of a combined encoder for date and time periods.
 */
public class DateTimeEncoder extends CombinedEncoder {
	private static final String		MONTH		= "MONTH";
	private static final String		WEEKDAY		= "WEEKDAY";
	private static final String		HOUR		= "HOUR";
	
	public SDR getSDRForValue(long dateTime) {
		return getSDRForValues(getValuesForDateTime(dateTime));
	}
	
	@Override
	public void initialize() {
		addEncoder(MONTH,getNewMonthEncoder());
		addEncoder(WEEKDAY,getNewWeekdayEncoder());
		addEncoder(HOUR,getNewHourEncoder());
	}
	
	protected EncoderObject getNewMonthEncoder() {
		ScalarEncoder r = new ScalarEncoder(48,8,0,12);
		r.setPeriodic(true);
		return r;
	}
	
	protected EncoderObject getNewWeekdayEncoder() {
		ScalarEncoder r = new ScalarEncoder(24,8,0,7);
		r.setPeriodic(true);
		return r;
	}
	
	protected EncoderObject getNewHourEncoder() {
		ScalarEncoder r = new ScalarEncoder(48,8,0,24);
		r.setPeriodic(true);
		return r;
	}
	
	protected SortedMap<String,Float> getValuesForDateTime(long dateTime) {
		SortedMap<String,Float> r = new TreeMap<String,Float>();
		Calendar cal = Calendar.getInstance(getTimeZone());
		cal.setTimeInMillis(dateTime);
		
		float hour = (float) cal.get(Calendar.HOUR_OF_DAY);

		float weekday = (float) (cal.get(Calendar.DAY_OF_WEEK) - 1);
		weekday += hour / 24F; 
		
		float month = (float) cal.get(Calendar.MONTH);
		month += cal.get(Calendar.DAY_OF_MONTH) / 32F;
		month += hour / 24F / 32F; 
		
		r.put(MONTH,month);
		r.put(WEEKDAY,weekday);
		r.put(HOUR,hour);
		return r;
	}

	protected TimeZone getTimeZone() {
		return TimeZone.getTimeZone("UTC");
	}
}
