package nl.zeesoft.zdk.htm.enc;

import java.util.Calendar;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

/**
 * A DateTimeValueEncoder provides an easy and configurable way for encoding date and time related values.
 * By default, it only encodes the value in the SDR.
 */
public class DateTimeValueEncoder extends CombinedEncoder implements JsAble {
	private static final String					MONTH				= "MONTH";
	private static final String					DAY_OF_WEEK			= "WEEKDAY";
	private static final String					HOUR_OF_DAY			= "HOUR";
	private static final String					MINUTE				= "MINUTE";
	private static final String					SECOND				= "SECOND";
	private static final String					VALUE				= "VALUE";
	
	protected int								scale				= 1;
	protected int								bitsPerEncoder		= 8;
	
	protected boolean							includeMonth		= false;
	protected boolean							includeDayOfWeek	= false;
	protected boolean							includeHourOfDay	= false;
	protected boolean							includeMinute		= false;
	protected boolean							includeSecond		= false;

	protected boolean							includeValue		= true;
	protected int								valueMin			= 0;
	protected int								valueMax			= 100;
	protected float								valueResolution		= 1;
	protected boolean							valueDistributed	= false;
	
	public DateTimeValueEncoder() {
		initialize();
	}
	
	/**
	 * Returns a copy of this encoder.
	 * 
	 * @return A copy of this encoder
	 */
	public DateTimeValueEncoder copy() {
		DateTimeValueEncoder copy = new DateTimeValueEncoder();
		copy.scale = scale;
		copy.bitsPerEncoder = bitsPerEncoder;
		copy.includeMonth = includeMonth;
		copy.includeDayOfWeek = includeDayOfWeek;
		copy.includeHourOfDay = includeHourOfDay;
		copy.includeMinute = includeMinute;
		copy.includeSecond = includeSecond;
		copy.includeValue = includeValue;
		copy.valueMin = valueMin;
		copy.valueMax = valueMax;
		copy.valueResolution = valueResolution;
		copy.valueDistributed = valueDistributed;
		copy.initialize();
		return copy;
	}
	
	/**
	 * Sets the scale of this encoder.
	 * 
	 * @param scale The scale of the encoder.
	 */
	public void setScale(int scale) {
		if (scale>0) {
			this.scale = scale;
		}
		initialize();
	}
	
	/**
	 * Specifies which properties should be encoded in the SDR.
	 * 
	 * @param includeMonth Indicates the month (season) should be encoded in the SDR
	 * @param includeDayOfWeek Indicates the day of week should be encoded in the SDR
	 * @param includeHourOfDay Indicates the hour of day should be encoded in the SDR
	 * @param includeMinute Indicates the minute should be encoded in the SDR
	 * @param includeSecond Indicates the Second should be encoded in the SDR
	 * @param includeValue Indicates the value should be encoded in the SDR
	 */
	public void setEncodeProperties(boolean includeMonth, boolean includeDayOfWeek, boolean includeHourOfDay, boolean includeMinute, boolean includeSecond, boolean includeValue) {
		this.includeMonth = includeMonth;
		this.includeDayOfWeek = includeDayOfWeek;
		this.includeHourOfDay = includeHourOfDay;
		this.includeMinute = includeMinute;
		this.includeSecond = includeSecond;
		this.includeValue = includeValue;
		initialize();
	}
	
	/**
	 * Specifies the minimum and maximum value to be encoded in the SDR.
	 * 
	 * @param min The minimum value
	 * @param max The maximum value
	 */
	public void setValueMinMax(int min, int max) {
		valueMin = min;
		valueMax = max;
		initialize();
	}
	
	/**
	 * Specifies the value resolution to be used for encoding SDRs.
	 * 
	 * @param res The resolution
	 */
	public void setValueResolution(float res) {
		valueResolution = res;
		initialize();
	}
	
	/**
	 * Indicates a random distributed scalar encoder should be used to encode values.
	 *  
	 * @param valueDistributed Indicates a random distributed scalar encoder should be used to encode values
	 */
	public void valueDistributed(boolean valueDistributed) {
		this.valueDistributed = valueDistributed;
		initialize();
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("scale","" + scale));
		json.rootElement.children.add(new JsElem("includeMonth","" + includeMonth));
		json.rootElement.children.add(new JsElem("includeDayOfWeek","" + includeDayOfWeek));
		json.rootElement.children.add(new JsElem("includeHourOfDay","" + includeHourOfDay));
		json.rootElement.children.add(new JsElem("includeMinute","" + includeMinute));
		json.rootElement.children.add(new JsElem("includeSecond","" + includeSecond));
		json.rootElement.children.add(new JsElem("includeValue","" + includeValue));
		if (includeValue) {
			json.rootElement.children.add(new JsElem("valueMin","" + valueMin));
			json.rootElement.children.add(new JsElem("valueMax","" + valueMax));
			json.rootElement.children.add(new JsElem("valueResolution","" + valueResolution));
			json.rootElement.children.add(new JsElem("valueDistributed","" + valueDistributed));
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			scale = json.rootElement.getChildInt("scale",scale);
			includeMonth = json.rootElement.getChildBoolean("includeMonth",includeMonth);
			includeDayOfWeek = json.rootElement.getChildBoolean("includeDayOfWeek",includeDayOfWeek);
			includeHourOfDay = json.rootElement.getChildBoolean("includeHourOfDay",includeHourOfDay);
			includeMinute = json.rootElement.getChildBoolean("includeMinute",includeMinute);
			includeSecond = json.rootElement.getChildBoolean("includeSecond",includeSecond);
			includeValue = json.rootElement.getChildBoolean("includeValue",includeDayOfWeek);
			if (includeValue) {
				valueMin = json.rootElement.getChildInt("valueMin",valueMin);
				valueMax = json.rootElement.getChildInt("valueMax",valueMax);
				valueResolution = json.rootElement.getChildFloat("valueResolution",valueResolution);
				valueDistributed = json.rootElement.getChildBoolean("valueDistributed",valueDistributed);
			}
			initialize();
		}
	}

	public DateTimeSDR getSDRForValue(int value) {
		return getSDRForValue(0,(Object)value,null);
	}
	
	public DateTimeSDR getSDRForValue(float value) {
		return getSDRForValue(0,(Object)value,null);
	}
	
	public DateTimeSDR getSDRForValue(long value) {
		return getSDRForValue(0,(Object)value,null);
	}
	
	public DateTimeSDR getSDRForValue(int value,String label) {
		return getSDRForValue(0,(Object)value,label);
	}
	
	public DateTimeSDR getSDRForValue(float value,String label) {
		return getSDRForValue(0,(Object)value,label);
	}
	
	public DateTimeSDR getSDRForValue(long value,String label) {
		return getSDRForValue(0,(Object)value,label);
	}

	public DateTimeSDR getSDRForValue(long dateTime,int value) {
		return getSDRForValue(dateTime,(Object)value,null);
	}
	
	public DateTimeSDR getSDRForValue(long dateTime,float value) {
		return getSDRForValue(dateTime,(Object)value,null);
	}
	
	public DateTimeSDR getSDRForValue(long dateTime,long value) {
		return getSDRForValue(dateTime,(Object)value,null);
	}
	
	public DateTimeSDR getSDRForValue(long dateTime,int value,String label) {
		return getSDRForValue(dateTime,(Object)value,label);
	}
	
	public DateTimeSDR getSDRForValue(long dateTime,float value,String label) {
		return getSDRForValue(dateTime,(Object)value,label);
	}
	
	public DateTimeSDR getSDRForValue(long dateTime,long value,String label) {
		return getSDRForValue(dateTime,(Object)value,label);
	}

	public DateTimeSDR getSDRForSDR(SDR sdr) {
		return getSDRForSDR(0,sdr,null,null);
	}

	public DateTimeSDR getSDRForSDR(SDR sdr,String label) {
		return getSDRForSDR(0,sdr,null,label);
	}

	public DateTimeSDR getSDRForSDR(SDR sdr,int value,String label) {
		return getSDRForSDR(0,sdr,(Object)value,label);
	}

	public DateTimeSDR getSDRForSDR(SDR sdr,float value,String label) {
		return getSDRForSDR(0,sdr,(Object)value,label);
	}

	public DateTimeSDR getSDRForSDR(SDR sdr,long value,String label) {
		return getSDRForSDR(0,sdr,(Object)value,label);
	}

	public DateTimeSDR getSDRForSDR(long dateTime,SDR sdr) {
		return getSDRForSDR(dateTime,sdr,null,null);
	}

	public DateTimeSDR getSDRForSDR(long dateTime,SDR sdr,String label) {
		return getSDRForSDR(dateTime,sdr,null,label);
	}

	public DateTimeSDR getSDRForSDR(long dateTime,SDR sdr,int value,String label) {
		return getSDRForSDR(dateTime,sdr,(Object)value,label);
	}

	public DateTimeSDR getSDRForSDR(long dateTime,SDR sdr,float value,String label) {
		return getSDRForSDR(dateTime,sdr,(Object)value,label);
	}

	public DateTimeSDR getSDRForSDR(long dateTime,SDR sdr,long value,String label) {
		return getSDRForSDR(dateTime,sdr,(Object)value,label);
	}

	/**
	 * Iterates through all possible values of the encoders to determine if the SDR values have a certain minimal and maximal overlap.
	 * 
	 * @param checkValue Indicates the value encoder should be checked as well (using this on RDScalarEncoders will cause state generation)
	 * @return An empty string builder or a string builder containing an error message
	 */
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
		setSDRProperties(r,dateTime,value,label);
		return r;
	}

	protected DateTimeSDR getSDRForSDR(long dateTime,SDR sdr,Object value,String label) {
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
		setSDRProperties(r,dateTime,value,label);
		return r;
	}
	
	protected void setSDRProperties(DateTimeSDR sdr,long dateTime,Object value,String label) {
		if (dateTime>0) {
			sdr.dateTime = dateTime;
		} else {
			sdr.dateTime = System.currentTimeMillis();
		}
		if (value!=null) {
			sdr.keyValues.put(DateTimeSDR.VALUE_KEY,value);
		}
		if (label!=null) {
			sdr.keyValues.put(DateTimeSDR.LABEL_KEY,label);
		}
	}
	
	protected SortedMap<String,Float> getValuesForDateTime(long dateTime) {
		SortedMap<String,Float> r = new TreeMap<String,Float>();
		Calendar cal = Calendar.getInstance(getTimeZone());
		cal.setTimeInMillis(dateTime);
		
		float factor = 1.5F;
		
		float second = (float) cal.get(Calendar.SECOND);
		
		float minute = (float) cal.get(Calendar.MINUTE);
		minute += second / (60F * factor);
		
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
		r.put(SECOND,second);
		return r;
	}
	
	@Override
	public void initialize() {
		super.initialize();
		int factor = 1;
		
		if (includeDayOfWeek) {
			factor++;
		}
		if (includeHourOfDay) {
			factor++;
		}
		if (includeMinute) {
			factor++;
		}
		if (includeSecond) {
			factor++;
		}
		bitsPerEncoder = (32 / factor) * scale;
		
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
		if (includeSecond) {
			addEncoder(SECOND,getNewSecondEncoder());
		}
		if (includeValue) {
			addEncoder(VALUE,getNewValueEncoder());
		}
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
	
	protected EncoderObject getNewSecondEncoder() {
		int length = 64 * scale;
		ScalarEncoder r = new ScalarEncoder(length,bitsPerEncoder,0,60);
		r.setPeriodic(true);
		return r;
	}
	
	protected EncoderObject getNewValueEncoder() {
		int baseLength = 256;
		if (includeMonth) {
			baseLength = baseLength - 48;
		}
		if (includeDayOfWeek) {
			baseLength = baseLength - 24;
		}
		if (includeHourOfDay) {
			baseLength = baseLength - 48;
		}
		if (includeMinute) {
			baseLength = baseLength - 64;
		}
		if (includeSecond) {
			baseLength = baseLength - 64;
		}
		int length = baseLength * scale;
		EncoderObject r = null;
		if (valueDistributed) {
			r = new RDScalarEncoder(length,bitsPerEncoder);
		} else {
			r = new ScalarEncoder(length,bitsPerEncoder,valueMin,valueMax);
		}
		r.setResolution(valueResolution);
		return r;
	}
		
	protected TimeZone getTimeZone() {
		return TimeZone.getTimeZone("UTC");
	}
}
