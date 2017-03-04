package nl.zeesoft.zjmo.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.ProtocolControl;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.Player;

public class TestMemberOnlineOffline extends TestObject {
	public TestMemberOnlineOffline(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestMemberOnlineOffline(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/*
		System.out.println("This test shows how to control an orchestra through a *Conductor* instance.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create conductor");
		System.out.println("Conductor con = new Conductor(orchestra);");
		System.out.println("// Start the conductor");
		System.out.println("boolean started = con.start();");
		System.out.println("// Create client");
		System.out.println("MemberClient client = new MemberClient(\"localhost\",5433);");
		System.out.println("// Send stop command to conductor");
		System.out.println("ZStringBuilder response = client.sendCommand(Protocol.STOP_PROGRAM);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("This test uses the *MockConductor* and the *MockPlayers*.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestMemberOnLineOffLine.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConductor.class));
		System.out.println(" * " + getTester().getLinkForClass(MockPlayers.class));
		System.out.println(" * " + getTester().getLinkForClass(Conductor.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The orchestra initialization duration.  ");
		System.out.println(" * The conductor GET_STATE command response.  ");
		System.out.println(" * The orchestra state JSON.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		boolean started = false;
		Date start = new Date();
		
		System.out.println("Starting members ...");
		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) getTester().getMockedObject(MockPlayers.class.getName());
		Player backup = null;
		int i = 0;
		for (Player player: players) {
			if (i<(players.size() - 1)) {
				started = player.start();
				assertEqual(started,true,"Failed to start player: " + player.getId());
			} else {
				backup = player;
			}
			i++;
		}
		Conductor con = (Conductor) getTester().getMockedObject(MockConductor.class.getName());
		started = con.start();
		assertEqual(started,true,"Failed to start the conductor");
		System.out.println("Starting members took " + ((new Date()).getTime() - start.getTime()) + " ms");
		System.out.println();

		System.out.println("Starting backup ...");
		started = backup.start();
		assertEqual(started,true,"Failed to start the backup: ");
		if (started) {
			sleep(1000);
			System.out.println();
			System.out.println("Orchestra state JSON:");
			System.out.println(con.getMemberState().toStringBuilderReadFormat());
			
			// TODO: Test online offline variations
			
			MemberClient client = new MemberClient("localhost",5433);
			client.open();
			assertEqual(client.isOpen(),true,"Failed to open the client");
			if (client.isOpen()) {
				sleep(1000);
				System.out.println();
				System.out.println("Stopping conductor ...");
				ZStringBuilder response = client.sendCommand(ProtocolControl.STOP_PROGRAM);
				assertEqual(response.toString(),"","Stop program response does not match expectation");
			}

			sleep(1000);
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
			//assertEqual(working,false,"Failed to stop player: " + player.getId());
			if (working) {
				player.stop();
			}
		}
	}
}
