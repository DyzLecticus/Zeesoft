package nl.zeesoft.zevt.test;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zevt.ZEVTConfig;
import nl.zeesoft.zodb.Config;

public class MockTranslator extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockEntityValueTranslator*.");
	}

	@Override
	protected Object initialzeMock() {
		Config config = new ZEVTConfig();
		config.setDebug(true);
		FixedDateTranslator t = new FixedDateTranslator(config);
		t.initialize();
		return t;
	}
}
