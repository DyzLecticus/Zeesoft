package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zsd.dialog.DialogSet;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;

public class MockInterpreterConfiguration extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockInterpreterConfiguration*.");
	}

	@Override
	protected Object initialzeMock() {
		DialogSet ds = new DialogSet();
		ds.initialize();
		InterpreterConfiguration config = new InterpreterConfiguration(ds);
		config.setEntityValueTranslator(new FixedDateEntityValueTranslator());
		config.setBaseDir("resources/");
		return config;
	}
}
