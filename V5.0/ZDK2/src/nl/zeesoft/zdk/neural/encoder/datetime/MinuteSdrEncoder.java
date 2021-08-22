package nl.zeesoft.zdk.neural.encoder.datetime;

import java.util.Calendar;
import java.util.Date;

public class MinuteSdrEncoder extends SeasonSdrEncoder {
	@Override
	public int getBase() {
		return 60;
	}

	@Override
	public MinuteSdrEncoder copy() {
		MinuteSdrEncoder r = new MinuteSdrEncoder();
		r.copyFrom(this);
		return r;
	}

	@Override
	protected long getPeriodStart(Date value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(value);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	@Override
	protected long getPeriodEnd(Date value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(value);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTimeInMillis();
	}
}
