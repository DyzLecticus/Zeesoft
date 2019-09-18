package nl.zeesoft.zdk.test.impl.htm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.test.MockObject;

public class MockAnomalyDateTimeValues extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockAnomalyDateTimeValues*.");
	}

	@Override
	protected Object initialzeMock() {
		List<MockDateTimeValue> r = new ArrayList<MockDateTimeValue>();
		
		int num = 24 * 365 * 2; // Every hour, every day for 2 years
		int numAnomaly = num / 2;
		
		int maxVal2 = 0;
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.YEAR,2018);
		cal.set(Calendar.MONTH,0);
		cal.set(Calendar.DATE,1);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		
		int val1 = 0;
		int val2 = 0;
		for (int i = 0; i<=num; i++) {
			int base = 0;
			
			int month = cal.get(Calendar.MONTH);
			if (month<6) {
				base += month;
			} else {
				base += 12 - month;
			}
			
			if (i>=numAnomaly) {
				base = 100 + base;
			}
			
			val1 = base;

			int hour = cal.get(Calendar.HOUR_OF_DAY);
			if (hour<12) {
				hour += (hour * 2);
			} else {
				hour += 48 - (hour * 2);
			}
			
			val2 = base + hour;
			val2 += ZRandomize.getRandomInt(0,3);
			
			MockDateTimeValue mockVal = new MockDateTimeValue();
			mockVal.dateTime = cal.getTimeInMillis();
			mockVal.value1 = val1;
			mockVal.value2 = val2;
			r.add(mockVal);
			
			cal.set(Calendar.HOUR_OF_DAY,cal.get(Calendar.HOUR_OF_DAY) + 1);
			
			if (val2>maxVal2) {
				maxVal2 = val2;
			}
		}
		
		return r;
	}
}
