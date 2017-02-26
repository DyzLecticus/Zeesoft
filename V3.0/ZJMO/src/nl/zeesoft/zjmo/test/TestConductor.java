package nl.zeesoft.zjmo.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.Protocol;
import nl.zeesoft.zjmo.orchestra.members.Conductor;

public class TestConductor extends TestObject {
	public TestConductor(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestConductor(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create, start and stop a *Conductor* instance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create conductor");
		System.out.println("Conductor con = new Conductor(orchestra);");
		System.out.println("// Start the conductor");
		System.out.println("con.start();");
		// TODO: Finish
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockTestOrchestra.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestTestOrchestra.class));
		System.out.println(" * " + getTester().getLinkForClass(MockTestOrchestra.class));
		System.out.println(" * " + getTester().getLinkForClass(TestOrchestra.class));
		System.out.println(" * " + getTester().getLinkForClass(Conductor.class));
		System.out.println();
		System.out.println("**Test output**  ");
		// TODO: Finish
		System.out.println("The output of this test shows JSON structure of the *TestOrchestra*.  ");
	}

	@Override
	protected void test(String[] args) {
		// TODO: Assertions
		TestOrchestra orch = (TestOrchestra) getTester().getMockedObject(MockTestOrchestra.class.getName());
		Conductor con = new Conductor(orch) {
			@Override
			protected void stopProgram() {
				System.out.println("Conductor stop program method called");
				stop();
				System.out.println("Conductor stopped");
			}
		};
		
		boolean started = con.start();
		if (started) {
			MemberClient client = new MemberClient("localhost",5433);
			client.open();
			if (!client.isOpen()) {
				System.err.println("Failed to connect to conductor");
			} else {
				ZStringBuilder response = client.sendCommand(Protocol.STOP_PROGRAM);
				System.out.println("Stop program command response: " + response);
			}
			/*
			if (con.isWorking()) {
				con.stop();
			}
			*/
		} else {
			System.err.println("Failed to start conductor");
		}
	}
}
