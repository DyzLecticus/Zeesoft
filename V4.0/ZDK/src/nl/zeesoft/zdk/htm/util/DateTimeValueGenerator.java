package nl.zeesoft.zdk.htm.util;

import java.util.Calendar;
import java.util.TimeZone;

import nl.zeesoft.zdk.functions.ZRandomize;

/**
 * A DateTimeValuGenerator can be used to generate mock input data for SDR streams.  
 */
public class DateTimeValueGenerator {
	private long	startDateTime			= 0;
	private long	incrementMs				= 1000;
	private int		minValue				= 0;
	private int		maxValue				= 50;
	private float	resolution				= 1;
	
	private int		randomValueResolutions	= 0;
	
	private long	currentIncrement		= 0;
	private float	currentValue			= 0;
	private boolean incrementCurrentValue	= true;

	public DateTimeValueGenerator(long incrementMs, int minValue, int maxValue, float resolution) {
		initialize(0,incrementMs,minValue,maxValue,resolution);
	}
	
	public DateTimeValueGenerator(long startDateTime, long incrementMs, int minValue, int maxValue, float resolution) {
		initialize(startDateTime,incrementMs,minValue,maxValue,resolution);
	}
	
	public void setRandomValueResolutions(int randomValueResolutions) {
		this.randomValueResolutions = randomValueResolutions;
	}

	public void reset() {
		currentIncrement = 0;
		currentValue = minValue;
		incrementCurrentValue = true;
	}
	
	public DateTimeValue getNextDateTimeValue() {
		return getNextDateTimeValue(null,0);
	}

	public DateTimeValue getNextDateTimeValue(String label) {
		return getNextDateTimeValue(label,0);
	}

	public DateTimeValue getNextDateTimeValue(float addValue) {
		return getNextDateTimeValue(null,addValue);
	}

	public DateTimeValue getNextDateTimeValue(String label,float addValue) {
		DateTimeValue r = new DateTimeValue();
		r.dateTime = startDateTime + (incrementMs * currentIncrement);
		currentIncrement++;
		
		r.value = currentValue + addValue;
		if (randomValueResolutions>0) {
			r.value += ZRandomize.getRandomInt(0,randomValueResolutions) * resolution;
		}
		
		if (incrementCurrentValue) {
			currentValue += resolution;
		} else {
			currentValue -= resolution;
		}
		if (currentValue>=(float)maxValue) {
			incrementCurrentValue = false;
		} else if (currentValue<=(float)minValue) {
			incrementCurrentValue = true;
		}
		
		if (label!=null && label.length()>0) {
			r.label = label;
		}
		return r;
	}
	
	protected void initialize(long startDateTime, long incrementMs, int minValue, int maxValue, float resolution) {
		if (startDateTime==0) {
			Calendar cal = Calendar.getInstance(getTimeZone());
			cal.set(Calendar.YEAR,2019);
			cal.set(Calendar.MONTH,9);
			cal.set(Calendar.DATE,1);
			cal.set(Calendar.HOUR_OF_DAY,20);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MILLISECOND,0);
			this.startDateTime = cal.getTimeInMillis();
		} else {
			this.startDateTime = startDateTime;
		}
		this.incrementMs = incrementMs;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.resolution = resolution;
		currentValue = minValue;
	}
	
	protected TimeZone getTimeZone() {
		return TimeZone.getTimeZone("UTC");
	}
}
