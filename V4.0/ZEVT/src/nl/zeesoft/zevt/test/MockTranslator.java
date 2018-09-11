package nl.zeesoft.zevt.test;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zevt.ZEVTConfig;

public class MockTranslator extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockEntityValueTranslator*.");
	}

	@Override
	protected Object initialzeMock() {
		FixedDateTranslator t = new FixedDateTranslator(new ZEVTConfig());
		t.initialize();
		return t;
	}
}
