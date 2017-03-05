package nl.zeesoft.zjmo.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.Player;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;

public class TestConductor extends TestObject {
	public TestConductor(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestConductor(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to start and stop *Conductor* and *Player* instances.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create conductor");
		System.out.println("Conductor con = new Conductor(orchestra);");
		System.out.println("// Start the conductor");
		System.out.println("boolean started = con.start();");
		System.out.println("// Create client using conductor control port settings");
		System.out.println("MemberClient client = new MemberClient(\"localhost\",5433);");
		System.out.println("// Send stop command");
		System.out.println("ZStringBuilder response = client.sendCommand(ProtocolControl.STOP_PROGRAM);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("This test uses the *MockConductor* and the *MockPlayers*.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestConductor.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConductor.class));
		System.out.println(" * " + getTester().getLinkForClass(MockPlayers.class));
		System.out.println(" * " + getTester().getLinkForClass(Conductor.class));
		System.out.println(" * " + getTester().getLinkForClass(Player.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The orchestra initialization duration.  ");
		System.out.println(" * The conductor GET_STATE command response.  ");
		System.out.println(" * The orchestra state JSON.  ");
	}

	@Override
	protected void test(String[] args) {
		boolean started = false;
		Date start = new Date();
		
		System.out.println("Starting members ...");
		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) getTester().getMockedObject(MockPlayers.class.getName());
		for (Player player: players) {
			started = player.start();
			assertEqual(started,true,"Failed to start player: " + player.getId());
		}
		Conductor con = (Conductor) getTester().getMockedObject(MockConductor.class.getName());
		started = con.start();
		assertEqual(started,true,"Failed to start the conductor");
		System.out.println("Starting members took " + ((new Date()).getTime() - start.getTime()) + " ms");

		if (started) {
			MemberClient client = new MemberClient("localhost",5433);
			client.open();
			assertEqual(client.isOpen(),true,"Failed to open the client");
			if (client.isOpen()) {
				sleep(2000);
				ZStringBuilder response = client.sendCommand(ProtocolControl.GET_STATE);
				System.out.println();
				System.out.println("GET_STATE command response: " + response);

				sleep(2000);
				System.out.println();
				System.out.println("Orchestra state JSON:");
				System.out.println(con.getOrchestraState().toStringBuilderReadFormat());

				sleep(2000);
				response = client.sendCommand(ProtocolControl.STOP_PROGRAM);
				assertEqual(response.toString(),"","Stop program response does not match expectation");
			}

			sleep(2000);
			System.out.println();
			System.out.println("Checking conductor ...");
			boolean working = con.isWorking();
			assertEqual(working,false,"Failed to stop the conductor");
			if (working) {
				con.stop();
			}
		}

		System.out.println();
		System.out.println("Stopping players ...");
		for (Player player: players) {
			boolean working = player.isWorking();
			assertEqual(working,true,"Player is not working: " + player.getId());
			if (working) {
				player.stop();
			}
		}
	}
}
