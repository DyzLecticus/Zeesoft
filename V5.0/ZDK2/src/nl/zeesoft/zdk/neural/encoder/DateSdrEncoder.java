package nl.zeesoft.zdk.neural.encoder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.neural.Sdr;

public class DateSdrEncoder extends AbstractEncoder {
	public ScalarSdrEncoder			monthEncoder	= new ScalarSdrEncoder();
	public ScalarSdrEncoder			dateEncoder		= new ScalarSdrEncoder();
	public ScalarSdrEncoder			weekdayEncoder	= new ScalarSdrEncoder();
	
	public boolean					includeMonth	= true;
	public boolean					includeDate		= true;
	public boolean					includeWeekday	= true;

	@Override
	public Sdr getEncodedValue(Object value) {
		Sdr r = new Sdr(getEncodeLength());
		if (value instanceof Long) {
			value = new Date((Long)value);
		}
		if (value instanceof Date) {
			Calendar cal = Calendar.getInstance();
			cal.setTime((Date)value);
			r.concat(getSdrs(cal));
		}
		return r;
	}

	public DateSdrEncoder copy() {
		DateSdrEncoder r = new DateSdrEncoder();
		r.copyFrom(this);
		return r;
	}
	
	public void setOnBitsPerEncoder(int onBits) {
		initializeEncoders(onBits);
	}

	public List<ScalarSdrEncoder> getActiveEncoders() {
		List<ScalarSdrEncoder> r = new ArrayList<ScalarSdrEncoder>();
		boolean[] includes = getIncludes();
		for (int i = 0; i < includes.length; i++) {
			if (includes[i]) {
				r.add(getEncoder(i));
			}
		}
		return r;
	}	
	
	public int getEncodeLength() {
		int r = 0;
		for (ScalarSdrEncoder encoder: getActiveEncoders()) {
			r += encoder.encodeLength;
		}
		return r;
	}
	
	public int getOnBits() {
		int r = 0;
		for (ScalarSdrEncoder encoder: getActiveEncoders()) {
			r += encoder.onBits;
		}
		return r;
	}

	protected List<Sdr> getSdrs(Calendar cal) {
		List<Sdr> r = new ArrayList<Sdr>();
		for (ScalarSdrEncoder encoder: getActiveEncoders()) {
			r.add(encoder.getEncodedValue(cal.get(getCalendarPropertyForEncoder(encoder))));
		}
		return r;
	}
	
	protected int getCalendarPropertyForEncoder(ScalarSdrEncoder encoder) {
		int r = 0;
		if (encoder == monthEncoder) {
			r = Calendar.MONTH;
		} else if (encoder == dateEncoder) {
			r = Calendar.DATE;
		} else if (encoder == weekdayEncoder) {
			r = Calendar.DAY_OF_WEEK;
		}
		return r;
	}
	
	protected void copyFrom(DateSdrEncoder other) {
		this.monthEncoder = other.monthEncoder.copy();
		this.dateEncoder = other.dateEncoder.copy();
		this.weekdayEncoder = other.weekdayEncoder.copy();
		this.includeMonth = other.includeMonth;
		this.includeDate = other.includeDate;
		this.includeWeekday = other.includeWeekday;
	}
	
	protected void initializeEncoders(int onBits) {
		initializeMonthEncoder(onBits);
		initializeDateEncoder(onBits);
		initializeWeekdayEncoder(onBits);
	}
	
	protected void initializeMonthEncoder(int onBits) {
		monthEncoder.encodeLength = (onBits / 2) * 12;
		monthEncoder.onBits = onBits;
		monthEncoder.minValue = 0;
		monthEncoder.maxValue = 12;
		monthEncoder.periodic = true;
	}
	
	protected void initializeDateEncoder(int onBits) {
		dateEncoder.encodeLength = (onBits / 2) * 31;
		dateEncoder.onBits = onBits;
		dateEncoder.minValue = 1;
		dateEncoder.maxValue = 32;
		dateEncoder.periodic = true;
	}
	
	protected void initializeWeekdayEncoder(int onBits) {
		weekdayEncoder.encodeLength = (onBits / 2) * 7;
		weekdayEncoder.onBits = onBits;
		weekdayEncoder.minValue = 1;
		weekdayEncoder.maxValue = 8;
		weekdayEncoder.periodic = true;
	}
	
	protected boolean[] getIncludes() {
		boolean[] r = {includeMonth, includeDate, includeWeekday};
		return r;
	}
	
	protected ScalarSdrEncoder getEncoder(int index) {
		ScalarSdrEncoder r = null;
		if (index==0) {
			r = monthEncoder;
		} else if (index==1) {
			r = dateEncoder;
		} else if (index==2) {
			r = weekdayEncoder;
		}
		return r;
	}
}
