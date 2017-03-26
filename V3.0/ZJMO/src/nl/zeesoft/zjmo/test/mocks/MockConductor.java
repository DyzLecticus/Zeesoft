package nl.zeesoft.zjmo.test.mocks;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zdk.test.impl.MockMessenger;
import nl.zeesoft.zdk.thread.Worker;
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
		// TODO: Multi conductor testing
		Conductor con = new Conductor(null,orch,0) {
			@Override
			public boolean start() {
				System.out.println("Starting " + getId() + " (control: " + getControlPort() + ", work: " + getWorkPort() +  ") ...");
				return super.start();
			}
			@Override
			protected Messenger getNewMessenger() {
				return (Messenger) getTester().getMockedObject(MockMessenger.class.getName());
			}
			@Override
			protected void stopProgram(Worker ignoreWorker) {
				//System.out.println("Stopping MockConductor ...");
				stop(ignoreWorker);
				//System.out.println("Stopped MockConductor");
			}
		};
		con.setDebug(true);
		return con;
	}
}
