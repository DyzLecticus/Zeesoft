package nl.zeesoft.zjmo.test.mocks;

import nl.zeesoft.zdk.test.MockObject;

public class MockTestOrchestra extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockTestOrchestra*.");
	}

	@Override
	protected Object initialzeMock() {
		TestOrchestra orch = new TestOrchestra();
		orch.initialize();
		return orch;
	}
}
