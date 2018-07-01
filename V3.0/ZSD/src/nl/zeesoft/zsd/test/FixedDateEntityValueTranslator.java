package nl.zeesoft.zsd.test;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zsd.EntityValueTranslator;

public class FixedDateEntityValueTranslator extends EntityValueTranslator {
	@Override
	public Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,2018);
		cal.set(Calendar.MONTH,6);
		cal.set(Calendar.DATE,16);
		cal.set(Calendar.HOUR_OF_DAY,9);
		cal.set(Calendar.MINUTE,54);
		cal.set(Calendar.SECOND,12);
		cal.set(Calendar.MILLISECOND,764);
		return cal.getTime();
	}
}
