package nl.zeesoft.zdk.neural.encoder.datetime;

import java.util.Calendar;
import java.util.Date;

public class DayOfWeekSdrEncoder extends SeasonSdrEncoder {
	@Override
	public int getBase() {
		return 7;
	}

	@Override
	public DayOfWeekSdrEncoder copy() {
		DayOfWeekSdrEncoder r = new DayOfWeekSdrEncoder();
		r.copyFrom(this);
		return r;
	}
	
	@Override
	protected long getPeriodStart(Date value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(value);
		cal.set(Calendar.DAY_OF_WEEK, 1);
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
		cal.set(Calendar.DAY_OF_WEEK, 7);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTimeInMillis();
	}
}
