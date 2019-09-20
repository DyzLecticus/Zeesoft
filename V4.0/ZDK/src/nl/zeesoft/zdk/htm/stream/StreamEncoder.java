package nl.zeesoft.zdk.htm.stream;

import java.util.Calendar;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.enc.CombinedEncoder;
import nl.zeesoft.zdk.htm.enc.EncoderObject;
import nl.zeesoft.zdk.htm.enc.RDScalarEncoder;
import nl.zeesoft.zdk.htm.enc.ScalarEncoder;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;

public class StreamEncoder extends CombinedEncoder {
	public static final String					VALUE_KEY			= "value";
	public static final String					LABEL_KEY			= "label";
	
	private static final String					MONTH				= "MONTH";
	private static final String					DAY_OF_WEEK			= "WEEKDAY";
	private static final String					HOUR_OF_DAY			= "HOUR";
	private static final String					MINUTE				= "MINUTE";
	private static final String					VALUE				= "VALUE";
	
	//private int									length				= 0;
	//private int									bits				= 0;
	//private SortedMap<String,EncoderObject>		encoders			= new TreeMap<String,EncoderObject>();

	protected int								scale				= 1;
	protected int								bitsPerEncoder		= 8;
	
	protected boolean							includeMonth		= false;
	protected boolean							includeDayOfWeek	= true;
	protected boolean							includeHourOfDay	= true;
	protected boolean							includeMinute		= true;

	protected boolean							includeValue		= true;
	protected int								valueMin			= 0;
	protected int								valueMax			= 100;
	protected float								valueRes			= 1;
	protected boolean							valueDistributed	= false;
	
	public StreamEncoder() {
		initialize();
	}
	
	public void setScale(int scale) {
		if (scale>0) {
			this.scale = scale;
			bitsPerEncoder = 8 * scale;
		}
		initialize();
	}
	
	public void setEncodeProperties(boolean includeMonth, boolean includeDayOfWeek, boolean includeHourOfDay, boolean includeMinute, boolean includeValue) {
		this.includeMonth = includeMonth;
		this.includeDayOfWeek = includeDayOfWeek;
		this.includeHourOfDay = includeHourOfDay;
		this.includeMinute = includeMinute;
		this.includeValue = includeValue;
		initialize();
	}
	
	public void setValueMinMax(int min, int max) {
		valueMin = min;
		valueMax = max;
		initialize();
	}
	
	public void setValueResolution(float res) {
		valueRes = res;
		initialize();
	}
	
	public void valueDistributed(boolean valueDistributed) {
		this.valueDistributed = valueDistributed;
		initialize();
	}

	public DateTimeSDR getSDRForSDR(SDR sdr,long dateTime,Object value,String label) {
		DateTimeSDR r = null;
		if (sdr.length()==length()) {
			if (sdr instanceof DateTimeSDR) {
				r = (DateTimeSDR) sdr;
			} else {
				r = new DateTimeSDR(sdr);
			}
		} else {
			r = new DateTimeSDR(length());
		}
		r.dateTime = dateTime;
		r.keyValues.put(VALUE_KEY,value);
		if (label!=null) {
			r.keyValues.put(LABEL_KEY,label);
		}
		return r;
	}

	public DateTimeSDR getSDRForValue(long dateTime,int value) {
		return getSDRForValue(dateTime,(Object)value,null);
	}
	
	public DateTimeSDR getSDRForValue(long dateTime,int value,String label) {
		return getSDRForValue(dateTime,(Object)value,label);
	}
	
	public DateTimeSDR getSDRForValue(long dateTime,float value) {
		return getSDRForValue(dateTime,(Object)value,null);
	}
	
	public DateTimeSDR getSDRForValue(long dateTime,float value,String label) {
		return getSDRForValue(dateTime,(Object)value,label);
	}
	
	public DateTimeSDR getSDRForValue(long dateTime,long value) {
		return getSDRForValue(dateTime,(Object)value,null);
	}
	
	public DateTimeSDR getSDRForValue(long dateTime,long value,String label) {
		return getSDRForValue(dateTime,(Object)value,label);
	}
	
	public ZStringBuilder testScalarOverlap(boolean checkValue) {
		ZStringBuilder r = new ZStringBuilder();
		for (String name: getEncoderNames()) {
			if (!name.equals(VALUE)|| checkValue) {
				ScalarEncoder encoder = getScalarEncoder(name);
				if (encoder!=null) {
					ZStringBuilder err = ((ScalarEncoder) encoder).testScalarOverlap(1,bitsPerEncoder - 1);
					if (err.length()>0) {
						if (r.length()>0) {
							r.append("\n");
						}
						r.append(name);
						r.append(": ");
						r.append(err);
					}
				}
			}
		}
		return r;
	}
	
	protected DateTimeSDR getSDRForValue(long dateTime,Object value,String label) {
		DateTimeSDR r = null;
		
		float encVal = 0;
		if (value instanceof Float) {
			encVal = (Float) value;
		} else if (value instanceof Integer) {
			encVal = (Integer) value;
		} else if (value instanceof Long) {
			encVal = (Long) value;
		}
		
		SortedMap<String,Float> values = getValuesForDateTime(dateTime);
		values.put(VALUE,encVal);
		
		r = new DateTimeSDR(getSDRForValues(values));
		r.dateTime = dateTime;
		r.keyValues.put(VALUE_KEY,value);
		if (label!=null) {
			r.keyValues.put(LABEL_KEY,label);
		}
		return r;
	}
	
	protected SortedMap<String,Float> getValuesForDateTime(long dateTime) {
		SortedMap<String,Float> r = new TreeMap<String,Float>();
		Calendar cal = Calendar.getInstance(getTimeZone());
		cal.setTimeInMillis(dateTime);
		
		float factor = 1.5F;
		
		float minute = (float) cal.get(Calendar.MINUTE);
		
		float hour = (float) cal.get(Calendar.HOUR_OF_DAY);
		hour += minute / (60F * factor);

		float weekday = (float) (cal.get(Calendar.DAY_OF_WEEK) - 1);
		weekday += hour / (24F * factor); 
		
		float month = (float) cal.get(Calendar.MONTH);
		month += cal.get(Calendar.DAY_OF_MONTH) / (31F * factor);
		
		r.put(MONTH,month);
		r.put(DAY_OF_WEEK,weekday);
		r.put(HOUR_OF_DAY,hour);
		r.put(MINUTE,minute);
		return r;
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		if (includeMonth) {
			addEncoder(MONTH,getNewMonthEncoder());
		}
		if (includeDayOfWeek) {
			addEncoder(DAY_OF_WEEK,getNewDayOfWeekEncoder());
		}
		if (includeHourOfDay) {
			addEncoder(HOUR_OF_DAY,getNewHourOfDayEncoder());
		}
		if (includeMinute) {
			addEncoder(MINUTE,getNewMinuteEncoder());
		}
		addEncoder(VALUE,getNewValueEncoder());
	}
	
	protected EncoderObject getNewMonthEncoder() {
		int length = 48 * scale;
		ScalarEncoder r = new ScalarEncoder(length,bitsPerEncoder,0,12);
		r.setPeriodic(true);
		return r;
	}
	
	protected EncoderObject getNewDayOfWeekEncoder() {
		int length = 24 * scale;
		ScalarEncoder r = new ScalarEncoder(length,bitsPerEncoder,0,7);
		r.setPeriodic(true);
		return r;
	}
	
	protected EncoderObject getNewHourOfDayEncoder() {
		int length = 48 * scale;
		ScalarEncoder r = new ScalarEncoder(length,bitsPerEncoder,0,24);
		r.setPeriodic(true);
		return r;
	}
	
	protected EncoderObject getNewMinuteEncoder() {
		int length = 64 * scale;
		ScalarEncoder r = new ScalarEncoder(length,bitsPerEncoder,0,60);
		r.setPeriodic(true);
		return r;
	}
	
	protected EncoderObject getNewValueEncoder() {
		int length = 120 * scale;
		EncoderObject r = null;
		if (valueDistributed) {
			r = new RDScalarEncoder(length,bitsPerEncoder);
		} else {
			r = new ScalarEncoder(length,bitsPerEncoder,valueMin,valueMax);
		}
		r.setResolution(valueRes);
		return r;
	}
		
	protected TimeZone getTimeZone() {
		return TimeZone.getTimeZone("UTC");
	}
}
