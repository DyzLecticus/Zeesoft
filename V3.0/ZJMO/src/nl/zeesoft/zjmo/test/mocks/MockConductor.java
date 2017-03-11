package nl.zeesoft.zjmo.test.mocks;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zdk.test.impl.MockMessenger;
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
		Messenger msgr = (Messenger) getTester().getMockedObject(MockMessenger.class.getName());
		msgr.setPrintDebugMessages(true);
		msgr.start();
		Conductor con = new Conductor(msgr,orch) {
			@Override
			public boolean start() {
				//getMessenger().getMessages();
				System.out.println("Starting " + getId() + " (control: " + getControlPort() + ", work: " + getWorkPort() +  ") ...");
				boolean started = super.start();
				if (started) {
					System.out.println("Started " + getId());
				} else {
					System.err.println("Failed to start " + getId());
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
		con.setDebug(true);
		return con;
	}
}
