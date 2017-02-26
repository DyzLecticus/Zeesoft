package nl.zeesoft.zjmo.test;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zjmo.orchestra.members.Conductor;

public class MockConductor extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockConductor*.");
		System.out.println("The *MockConductor* uses the *MockTestOrchestra*.");
	}

	@Override
	protected Object initialzeMock() {
		TestOrchestra orch = (TestOrchestra) getTester().getMockedObject(MockTestOrchestra.class.getName());
		Conductor con = new Conductor(orch) {
			@Override
			protected void stopProgram() {
				stop();
			}
		};
		return con;
	}
}
