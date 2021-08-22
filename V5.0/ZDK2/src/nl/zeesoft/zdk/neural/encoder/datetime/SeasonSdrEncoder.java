package nl.zeesoft.zdk.neural.encoder.datetime;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.encoder.ScalarSdrEncoder;

public class SeasonSdrEncoder extends ScalarSdrEncoder {
	public SeasonSdrEncoder() {
		minValue = 1;
		setMaxValue(getBase());
		setOnBits(8);
		periodic = true;
	}
	
	@Override
	public SeasonSdrEncoder copy() {
		SeasonSdrEncoder r = new SeasonSdrEncoder();
		r.copyFrom(this);
		return r;
	}

	@Override
	public Sdr getEncodedValue(Object value) {
		Sdr r = new Sdr(encodeLength);
		if (value instanceof Date) {
			setBits(r, getPeriodPercentage((Date)value));
		} else {
			r = super.getEncodedValue(value);
		}
		return r;
	}
	
	public int getBase() {
		return 12;
	}

	public void setScale(int targetLength, int minOverlap) {
		int value = (targetLength - (targetLength % getBase()));
		if (minOverlap>0) {
			int max = getBase() * (onBits - minOverlap);
			if (value > max) {
				value = max;
			}
		}
		setMaxValue(value);
		resolution = value / getBase();
	}
	
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		determineEncodeLength();
	}
	
	public void setOnBits(int onBits) {
		this.onBits = onBits;
		determineEncodeLength();
	}
	
	protected float getPeriodPercentage(Date value) {
		long start = getPeriodStart(value);
		long valueNormalized = value.getTime() - start;
		long totalNormalized = getPeriodEnd(value) - start;
		return (float)valueNormalized / (float)totalNormalized;
	}
	
	protected long getPeriodStart(Date value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(value);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	protected long getPeriodEnd(Date value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(value);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DATE, 31);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTimeInMillis();
	}
	
	protected void determineEncodeLength() {
		encodeLength = (((int)maxValue) - ((int)minValue)) + 1;
	}
}
