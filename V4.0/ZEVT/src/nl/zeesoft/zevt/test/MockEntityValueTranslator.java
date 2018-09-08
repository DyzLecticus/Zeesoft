package nl.zeesoft.zevt.test;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zevt.ZEVTConfig;

public class MockEntityValueTranslator extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockEntityValueTranslator*.");
	}

	@Override
	protected Object initialzeMock() {
		FixedDateEntityValueTranslator t = new FixedDateEntityValueTranslator(new ZEVTConfig());
		t.initialize();
		return t;
	}
}
