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
			public boolean start() {
				System.out.println("Starting " + getPosition().getName() + "/" + getPositionBackupNumber() + " (control: " + getControlPort() + ", work: " + getWorkPort() +  ") ...");
				boolean started = super.start();
				if (started) {
					System.out.println("Started " + getPosition().getName() + "/" + getPositionBackupNumber());
				} else {
					System.err.println("Failed to start " + getPosition().getName() + "/" + getPositionBackupNumber());
				}
				return started;
			}
			@Override
			protected void stopProgram() {
				//System.out.println("Stopping MockConductor ...");
				stop();
				//System.out.println("Stopped MockConductor");
			}
		};
		return con;
	}
}
