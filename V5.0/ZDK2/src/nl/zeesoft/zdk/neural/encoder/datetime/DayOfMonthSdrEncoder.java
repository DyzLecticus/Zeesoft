package nl.zeesoft.zdk.neural.encoder.datetime;

import java.util.Calendar;
import java.util.Date;

public class DayOfMonthSdrEncoder extends SeasonSdrEncoder {
	@Override
	public int getBase() {
		return 31;
	}

	@Override
	public DayOfMonthSdrEncoder copy() {
		DayOfMonthSdrEncoder r = new DayOfMonthSdrEncoder();
		r.copyFrom(this);
		return r;
	}

	@Override
	protected long getPeriodStart(Date value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(value);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	@Override
	protected long getPeriodEnd(Date value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(value);
		cal.set(Calendar.DATE, 31);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTimeInMillis();
	}
}
