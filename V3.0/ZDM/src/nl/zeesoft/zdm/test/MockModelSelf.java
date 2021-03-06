package nl.zeesoft.zdm.test;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zdm.model.ModelSelf;

public class MockModelSelf extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockModelSelf*.");
	}

	@Override
	protected Object initialzeMock() {
		ModelSelf modelSelf = new ModelSelf();
		modelSelf.initializeSelf();
		return modelSelf;
	}
}
