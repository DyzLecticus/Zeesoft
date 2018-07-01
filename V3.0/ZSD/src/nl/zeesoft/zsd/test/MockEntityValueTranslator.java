package nl.zeesoft.zsd.test;

import java.util.Date;

import nl.zeesoft.zdk.test.MockObject;

public class MockEntityValueTranslator extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockEntityValueTranslator*.");
	}

	@Override
	protected Object initialzeMock() {
		Date started = new Date();
		FixedDateEntityValueTranslator t = new FixedDateEntityValueTranslator();
		t.initialize();
		System.out.println("Initializing the EntityValueTranslator took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		return t;
	}
}
