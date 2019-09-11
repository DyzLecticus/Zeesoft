package nl.zeesoft.zdk.test.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.test.MockObject;

public class MockDateTimeValues extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockDateTimeValues*.");
	}

	@Override
	protected Object initialzeMock() {
		List<MockDateTimeValue> r = new ArrayList<MockDateTimeValue>();
		
		int num = 24 * 365 * 2; // Every hour, every day for 2 years
		int numFinalQuarter = num - (num / 4);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,2019);
		cal.set(Calendar.MONTH,0);
		cal.set(Calendar.DATE,1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		
		int val1 = 0;
		int val2 = 0;
		for (int i = 0; i<=num; i++) {
			int base = 20;
			
			int month = cal.get(Calendar.MONTH);
			if (month<6) {
				base += month;
			} else {
				base += 12 - month;
			}
			if (i>numFinalQuarter) {
				base = base * 2;
			}
			
			val1 = base;

			val2 = (int) ((base + cal.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_WEEK)) * 1.5F);
			
			int hour = cal.get(Calendar.HOUR);
			if (hour<12) {
				val2 += (hour * 4);
			} else {
				val2 += 96 - (hour * 4);
			}
			
			val2 += ZRandomize.getRandomInt(0,3);
			
			MockDateTimeValue mockVal = new MockDateTimeValue();
			mockVal.dateTime = cal.getTimeInMillis();
			mockVal.value1 = val1;
			mockVal.value2 = val2;
			r.add(mockVal);
			
			cal.set(Calendar.HOUR_OF_DAY,cal.get(Calendar.HOUR_OF_DAY) + 1);
		}
		
		return r;
	}
}
