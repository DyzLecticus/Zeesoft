package nl.zeesoft.zjmo.test.mocks;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zdk.test.impl.MockMessenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.members.Conductor;

public class MockConductor1 extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockConductor1*.");
		System.out.println("The *MockConductor1* uses the *MockTestOrchestra*.");
	}

	protected int getPositionBackupNumber() {
		return 0;
	}
	
	@Override
	protected Object initialzeMock() {
		TestOrchestra orch = (TestOrchestra) getTester().getMockedObject(MockTestOrchestra.class.getName());
		Conductor con = new Conductor(null,orch,getPositionBackupNumber()) {
			@Override
			public boolean start() {
				System.out.println("Starting " + getId() + " (control: " + getControlPort() + ", work: " + getWorkPort() +  ") ...");
				return super.start();
			}
			@Override
			public void updateOrchestra(Orchestra newOrchestra) {
				System.out.println(getId() + ": Updated orchestra"); 
				updatedOrchestra();
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
