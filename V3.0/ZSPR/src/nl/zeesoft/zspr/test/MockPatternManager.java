package nl.zeesoft.zspr.test;

import java.util.Calendar;
import java.util.Date;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zspr.pattern.PatternManager;

public class MockPatternManager extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockPatternManager*. It is used to instantiate a shared *PatternManager* for tests.");
	}

	@Override
	protected Object initialzeMock() {
		Date now = new Date();
		PatternManager manager = new PatternManager() {
			@Override
			public Date getCurrentDate() {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR,2017);
				cal.set(Calendar.MONTH,0);
				cal.set(Calendar.DATE,1);
				return cal.getTime();
			}
		};
		manager.initializePatterns();
		System.out.println("Initializing pattern manager took " + ((new Date()).getTime() - now.getTime()) + " ms");
		System.out.println();
		return manager;
	}
}
