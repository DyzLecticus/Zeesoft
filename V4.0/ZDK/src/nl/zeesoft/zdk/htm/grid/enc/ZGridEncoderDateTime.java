package nl.zeesoft.zdk.htm.grid.enc;

import java.util.Calendar;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.enc.CombinedEncoder;
import nl.zeesoft.zdk.htm.enc.EncoderObject;
import nl.zeesoft.zdk.htm.enc.ScalarEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridColumnEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridResult;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class ZGridEncoderDateTime extends ZGridColumnEncoder {
	private static final String					MONTH				= "MONTH";
	private static final String					DAY_OF_WEEK			= "WEEKDAY";
	private static final String					HOUR_OF_DAY			= "HOUR";
	private static final String					MINUTE				= "MINUTE";
	private static final String					SECOND				= "SECOND";
	
	protected int								scale				= 1;
	protected int								bitsPerEncoder		= 8;
	
	protected boolean							includeMonth		= true;
	protected boolean							includeDayOfWeek	= true;
	protected boolean							includeHourOfDay	= true;
	protected boolean							includeMinute		= true;
	protected boolean							includeSecond		= true;

	public ZGridEncoderDateTime() {
		rebuildEncoder();
	}
	
	@Override
	public ZGridEncoderDateTime copy() {
		ZGridEncoderDateTime r = new ZGridEncoderDateTime();
		r.scale = scale;
		r.bitsPerEncoder = bitsPerEncoder;
		r.includeMonth = includeMonth;
		r.includeDayOfWeek = includeDayOfWeek;
		r.includeHourOfDay = includeHourOfDay;
		r.includeMinute = includeMinute;
		r.includeSecond = includeSecond;
		r.rebuildEncoder();
		return r;
	}
	
	@Override
	public String getValueKey() {
		return "DATETIME";
	}
	
	public void setScale(int scale) {
		this.scale = scale;
		rebuildEncoder();
	}

	public void setBitsPerEncoder(int bitsPerEncoder) {
		this.bitsPerEncoder = bitsPerEncoder;
		rebuildEncoder();
	}

	public void setIncludeMonth(boolean includeMonth) {
		this.includeMonth = includeMonth;
		rebuildEncoder();
	}

	public void setIncludeDayOfWeek(boolean includeDayOfWeek) {
		this.includeDayOfWeek = includeDayOfWeek;
		rebuildEncoder();
	}

	public void setIncludeHourOfDay(boolean includeHourOfDay) {
		this.includeHourOfDay = includeHourOfDay;
		rebuildEncoder();
	}

	public void setIncludeMinute(boolean includeMinute) {
		this.includeMinute = includeMinute;
		rebuildEncoder();
	}

	public void setIncludeSecond(boolean includeSecond) {
		this.includeSecond = includeSecond;
		rebuildEncoder();
	}
	
	public DateTimeSDR getSDRForDateTime(long dateTime) {
		SortedMap<String,Float> values = getValuesForDateTime(dateTime);
		DateTimeSDR r = new DateTimeSDR(encoder.getSDRForValues(values));
		r.dateTime = dateTime;
		r.keyValues.put(getValueKey(),dateTime);
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("scale","" + scale));
		json.rootElement.children.add(new JsElem("bitsPerEncoder","" + bitsPerEncoder));
		json.rootElement.children.add(new JsElem("includeMonth","" + includeMonth));
		json.rootElement.children.add(new JsElem("includeDayOfWeek","" + includeDayOfWeek));
		json.rootElement.children.add(new JsElem("includeHourOfDay","" + includeHourOfDay));
		json.rootElement.children.add(new JsElem("includeMinute","" + includeMinute));
		json.rootElement.children.add(new JsElem("includeSecond","" + includeSecond));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			scale = json.rootElement.getChildInt("scale",scale);
			bitsPerEncoder = json.rootElement.getChildInt("bitsPerEncoder",bitsPerEncoder);
			includeMonth = json.rootElement.getChildBoolean("includeMonth",includeMonth);
			includeDayOfWeek = json.rootElement.getChildBoolean("includeDayOfWeek",includeDayOfWeek);
			includeHourOfDay = json.rootElement.getChildBoolean("includeHourOfDay",includeHourOfDay);
			includeMinute = json.rootElement.getChildBoolean("includeMinute",includeMinute);
			includeSecond = json.rootElement.getChildBoolean("includeSecond",includeSecond);
			rebuildEncoder();
		}
	}

	@Override
	protected DateTimeSDR encodeRequestValue(int columnIndex,ZGridResult result) {
		DateTimeSDR r = null;
		if (hasInputValue(columnIndex,result)) {
			long dateTime = getInputValueAsLong(columnIndex,result);
			r = getSDRForDateTime(dateTime);
		} else {
			r = getNewDateTimeSDR();
		}
		return r;
	}
	
	protected void rebuildEncoder() {
		encoder = new CombinedEncoder();
		
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
			encoder.addEncoder(MONTH,getNewMonthEncoder());
		}
		if (includeDayOfWeek) {
			encoder.addEncoder(DAY_OF_WEEK,getNewDayOfWeekEncoder());
		}
		if (includeHourOfDay) {
			encoder.addEncoder(HOUR_OF_DAY,getNewHourOfDayEncoder());
		}
		if (includeMinute) {
			encoder.addEncoder(MINUTE,getNewMinuteEncoder());
		}
		if (includeSecond) {
			encoder.addEncoder(SECOND,getNewSecondEncoder());
		}
	}
	
	protected EncoderObject getNewMonthEncoder() {
		int length = 48 * scale;
		ScalarEncoder r = new ScalarEncoder(length,bitsPerEncoder,0,12);
		r.setPeriodic(true);
		return r;
	}
	
	protected EncoderObject getNewDayOfWeekEncoder() {
		int length = 32 * scale;
		if (scale==2) {
			length += 7;
		}
		ScalarEncoder r = new ScalarEncoder(length,bitsPerEncoder,0,7);
		r.setPeriodic(true);
		return r;
	}
	
	protected EncoderObject getNewHourOfDayEncoder() {
		int length = 48 * scale;
		if (scale==2) {
			length += 10;
		}
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
		
	protected TimeZone getTimeZone() {
		return TimeZone.getTimeZone("UTC");
	}
}
