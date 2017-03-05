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
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;

public class TestMemberOnlineOffline extends TestObject {
	public TestMemberOnlineOffline(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestMemberOnlineOffline(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to how a *Conductor* maintains its orchestra member state representation.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create conductor");
		System.out.println("Conductor con = new Conductor(orchestra);");
		System.out.println("// Start the conductor");
		System.out.println("boolean started = con.start();");
		System.out.println("// Create client using conductor control port settings");
		System.out.println("MemberClient client = new MemberClient(\"localhost\",5433);");
		System.out.println("// Send get member state request");
		System.out.println("ZStringBuilder response = client.sendCommand(ProtocolControl.GET_MEMBER_STATE,\"id\",\"[MEMBERID]\");");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("This test uses the *MockConductor* and the *MockPlayers*.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestMemberOnlineOffline.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConductor.class));
		System.out.println(" * " + getTester().getLinkForClass(MockPlayers.class));
		System.out.println(" * " + getTester().getLinkForClass(Conductor.class));
		System.out.println(" * " + getTester().getLinkForClass(Player.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows how the orchestra member state representation changes while the state of one of the backup members is manipulated  ");
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
			sleep(2000);
			System.out.println();
			System.out.println("Backup member state JSON:");
			System.out.println(con.getMemberState(backup.getId()).toStringBuilderReadFormat());
			
			System.out.println();
			System.out.println("Stopping backup ...");
			backup.stop();
			System.out.println("Stopped backup");

			sleep(2000);
			System.out.println();
			System.out.println("Backup member state JSON:");
			System.out.println(con.getMemberState(backup.getId()).toStringBuilderReadFormat());

			System.out.println();
			System.out.println("Starting backup ...");
			backup.start();
			System.out.println("Started backup");

			sleep(2000);
			System.out.println();
			System.out.println("Backup member state JSON:");
			System.out.println(con.getMemberState(backup.getId()).toStringBuilderReadFormat());
			
			MemberClient client = new MemberClient("localhost",5433);
			client.open();
			assertEqual(client.isOpen(),true,"Failed to open the client");
			if (client.isOpen()) {
				ZStringBuilder response = null;
				
				sleep(2000);
				System.out.println();
				System.out.println("Taking backup offline ...");
				response = client.sendCommand(ProtocolControlConductor.TAKE_MEMBER_OFFLINE,"id",backup.getId());
				assertEqual(response.toString(),"{\"response\":\"Executed command\"}","Take backup offline response does not match expectation");
				System.out.println("Take backup offline response: " + response);
				
				sleep(2000);
				System.out.println();
				System.out.println("Backup member state JSON:");
				System.out.println(con.getMemberState(backup.getId()).toStringBuilderReadFormat());
				
				sleep(2000);
				System.out.println();
				System.out.println("Bringing backup online ...");
				response = client.sendCommand(ProtocolControlConductor.BRING_MEMBER_ONLINE,"id",backup.getId());
				assertEqual(response.toString(),"{\"response\":\"Executed command\"}","Bring backup online response does not match expectation");
				System.out.println("Bring backup online response: " + response);
				
				sleep(2000);
				System.out.println();
				System.out.println("Backup member state JSON:");
				System.out.println(con.getMemberState(backup.getId()).toStringBuilderReadFormat());

				sleep(2000);
				System.out.println();
				System.out.println("Draining backup offline ...");
				response = client.sendCommand(ProtocolControlConductor.DRAIN_MEMBER_OFFLINE,"id",backup.getId());
				assertEqual(response.toString(),"{\"response\":\"Executed command\"}","Drain backup offline response does not match expectation");
				System.out.println("Drain backup offline response: " + response);

				System.out.println();
				System.out.println("Backup member state JSON:");
				System.out.println(con.getMemberState(backup.getId()).toStringBuilderReadFormat());
				
				sleep(2000);
				System.out.println();
				System.out.println("Backup member state JSON:");
				System.out.println(con.getMemberState(backup.getId()).toStringBuilderReadFormat());
				
				sleep(2000);
				System.out.println();
				System.out.println("Stopping conductor ...");
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
