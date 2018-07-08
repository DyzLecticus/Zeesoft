package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;

public class MockInterpreterConfiguration extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockInterpreterConfiguration*.");
	}

	@Override
	protected Object initialzeMock() {
		InterpreterConfiguration config = new InterpreterConfiguration();
		config.setEntityValueTranslator(new FixedDateEntityValueTranslator());
		config.getBase().setBaseDir("resources/");
		return config;
	}
}
