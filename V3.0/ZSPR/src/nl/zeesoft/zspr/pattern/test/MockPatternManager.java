package nl.zeesoft.zspr.pattern.test;

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
		PatternManager manager = new PatternManager();
		manager.initializePatterns();
		System.out.println("Initializing pattern manager took " + ((new Date()).getTime() - now.getTime()) + " ms");
		System.out.println();
		return manager;
	}
}
