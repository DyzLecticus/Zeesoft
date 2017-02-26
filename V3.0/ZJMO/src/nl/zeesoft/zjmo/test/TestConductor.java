package nl.zeesoft.zjmo.test;

import java.util.Date;

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
		getTester().describeMock(MockConductor.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestConductor.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConductor.class));
		System.out.println(" * " + getTester().getLinkForClass(Conductor.class));
		System.out.println();
		System.out.println("**Test output**  ");
		// TODO: Finish
		System.out.println("The output of this test shows JSON structure of the *TestOrchestra*.  ");
	}

	@Override
	protected void test(String[] args) {
		Conductor con = (Conductor) getTester().getMockedObject(MockConductor.class.getName());
		Date start = new Date();
		System.out.println("Starting conductor ...");
		boolean started = con.start();
		System.out.println("Starting conductor took " + ((new Date()).getTime() - start.getTime()) + " ms");
		assertEqual(started,true,"Failed to start the conductor");
		if (started) {
			MemberClient client = new MemberClient("localhost",5433);
			client.open();
			assertEqual(client.isOpen(),true,"Failed to open the client");
			if (client.isOpen()) {
				ZStringBuilder response = client.sendCommand(Protocol.GET_STATE);
				System.out.println("Get state response: " + response);
				response = client.sendCommand(Protocol.STOP_PROGRAM);
				assertEqual(response.toString(),"","Stop program response does not match expectation");
			}
			System.out.println("Conductor member state JSON:");
			System.out.println(con.getMemberState().toStringBuilderReadFormat());
		} else {
			System.err.println("Failed to start conductor");
		}
	}
}
