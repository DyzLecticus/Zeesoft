package nl.zeesoft.zdk.test.impl.htm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import nl.zeesoft.zdk.test.MockObject;

public class MockRegularDateTimeValues extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockRegularDateTimeValues*.");
	}

	@Override
	protected Object initialzeMock() {
		List<MockDateTimeValue> r = new ArrayList<MockDateTimeValue>();
		
		int num = 24 * 365 * 2; // Every hour, every day for 2 years
		
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
			val1 = cal.get(Calendar.HOUR);
			val2 = cal.get(Calendar.HOUR) * cal.get(Calendar.DAY_OF_WEEK);
			
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
