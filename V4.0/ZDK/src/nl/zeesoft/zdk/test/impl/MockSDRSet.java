package nl.zeesoft.zdk.test.impl;

import java.util.Calendar;

import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.enc.DateTimeValueEncoder;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;
import nl.zeesoft.zdk.test.MockObject;

public class MockSDRSet extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockSDRSet*.");
	}

	@Override
	protected Object initialzeMock() {
		DateTimeValueEncoder enc = new DateTimeValueEncoder();
		SDRSet sdrSet = new SDRSet(enc.size());
		
		int sdrs = 24 * 365 * 2; // Every hour, every day for 2 years
		int sdrsFinalQuarter = sdrs - (sdrs / 4);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,2019);
		cal.set(Calendar.MONTH,0);
		cal.set(Calendar.DATE,1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		
		SDR sdr = null;

		int val1 = 0;
		int val2 = 0;
		for (int i = 0; i<=sdrs; i++) {
			int base = 20;
			
			int month = cal.get(Calendar.MONTH);
			if (month<6) {
				base += month;
			} else {
				base += 12 - month;
			}
			if (i>sdrsFinalQuarter) {
				base+=10;
			}
			
			val1 = base;

			val2 = (int) ((base + cal.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_WEEK)) * 1.5F);
			
			int hour = cal.get(Calendar.HOUR);
			if (hour<12) {
				val2 += hour;
			} else {
				val2 += 24 - hour;
			}
			
			val2 += ZRandomize.getRandomInt(0,3);
			
			sdr = enc.getSDRForValue(cal.getTimeInMillis(),val1,val2);
			sdrSet.add(sdr);
		}
		return sdrSet;
	}
}
