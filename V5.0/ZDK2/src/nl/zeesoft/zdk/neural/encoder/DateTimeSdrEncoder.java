package nl.zeesoft.zdk.neural.encoder;

import java.util.Calendar;

public class DateTimeSdrEncoder extends DateSdrEncoder {
	public ScalarSdrEncoder			hourEncoder		= new ScalarSdrEncoder();
	public ScalarSdrEncoder			minuteEncoder	= new ScalarSdrEncoder();
	public ScalarSdrEncoder			secondEncoder	= new ScalarSdrEncoder();
	
	public boolean					includeHour		= true;
	public boolean					includeMinute	= true;
	public boolean					includeSecond	= true;
	
	public DateTimeSdrEncoder() {
		initializeEncoders(4);
	}

	@Override
	public DateTimeSdrEncoder copy() {
		DateTimeSdrEncoder r = new DateTimeSdrEncoder();
		r.copyFrom(this);
		return r;
	}
	
	@Override
	protected int getCalendarPropertyForEncoder(ScalarSdrEncoder encoder) {
		int r = super.getCalendarPropertyForEncoder(encoder);
		if (encoder == hourEncoder) {
			r = Calendar.HOUR_OF_DAY;
		} else if (encoder == minuteEncoder) {
			r = Calendar.MINUTE;
		} else if (encoder == secondEncoder) {
			r = Calendar.SECOND;
		}
		return r;
	}
	
	protected void copyFrom(DateTimeSdrEncoder other) {
		super.copyFrom(other);
		this.hourEncoder = other.hourEncoder.copy();
		this.minuteEncoder = other.minuteEncoder.copy();
		this.secondEncoder = other.secondEncoder.copy();
		this.includeHour = other.includeHour;
		this.includeMinute = other.includeMinute;
		this.includeSecond = other.includeSecond;
	}
	
	@Override
	protected void initializeEncoders(int onBits) {
		super.initializeEncoders(onBits);
		initializeHourEncoder(onBits);
		initializeMinuteEncoder(onBits);
		initializeSecondEncoder(onBits);
	}
	
	protected void initializeHourEncoder(int onBits) {
		hourEncoder.encodeLength = (onBits / 2) * 24;
		hourEncoder.onBits = onBits;
		hourEncoder.minValue = 0;
		hourEncoder.maxValue = 24;
		hourEncoder.periodic = true;
	}
	
	protected void initializeMinuteEncoder(int onBits) {
		minuteEncoder.encodeLength = (onBits / 2) * 60;
		minuteEncoder.onBits = onBits;
		minuteEncoder.minValue = 0;
		minuteEncoder.maxValue = 60;
		minuteEncoder.periodic = true;
	}
	
	protected void initializeSecondEncoder(int onBits) {
		secondEncoder.encodeLength = (onBits / 2) * 60;
		secondEncoder.onBits = onBits;
		secondEncoder.minValue = 0;
		secondEncoder.maxValue = 60;
		secondEncoder.periodic = true;
	}
	
	@Override
	protected boolean[] getIncludes() {
		boolean[] r = {includeMonth, includeDate, includeWeekday, includeHour, includeMinute, includeSecond};
		return r;
	}

	@Override
	protected ScalarSdrEncoder getEncoder(int index) {
		ScalarSdrEncoder r = super.getEncoder(index);
		if (index==3) {
			r = hourEncoder;
		} else if (index==4) {
			r = minuteEncoder;
		} else if (index==5) {
			r = secondEncoder;
		}
		return r;
	}
}
