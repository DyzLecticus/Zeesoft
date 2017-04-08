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
import nl.zeesoft.zjmo.test.mocks.MockConductor1;
import nl.zeesoft.zjmo.test.mocks.MockConductor2;
import nl.zeesoft.zjmo.test.mocks.MockPlayers;

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
		System.out.println("Conductor con = new Conductor(null,orchestra,0);");
		System.out.println("// Start the conductor");
		System.out.println("boolean started = con.start();");
		System.out.println("// Create client using conductor control port settings");
		System.out.println("MemberClient client = new MemberClient(\"localhost\",5433);");
		System.out.println("// Send stop command");
		System.out.println("ZStringBuilder response = client.sendCommand(ProtocolControl.STOP_PROGRAM);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("This test uses *MockConductor1*, *MockConductor2* and the *MockPlayers*.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestConductor.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConductor1.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConductor2.class));
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

		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) getTester().getMockedObject(MockPlayers.class.getName());
		Conductor con1 = (Conductor) getTester().getMockedObject(MockConductor1.class.getName());
		Conductor con2 = (Conductor) getTester().getMockedObject(MockConductor2.class.getName());
		
		started = startTestOrchestra(players,con1,con2);
		assertEqual(started,true,"Failed to start orchestra");

		sleep(1000);

		if (started) {
			MemberClient client = new MemberClient("localhost",5433);
			client.open();
			assertEqual(client.isOpen(),true,"Failed to open the client");
			if (client.isOpen()) {
				ZStringBuilder response = client.sendCommand(ProtocolControl.GET_STATE);
				System.out.println();
				System.out.println("GET_STATE command response: " + response);

				sleep(2000);
				System.out.println();
				System.out.println("Orchestra state JSON:");
				System.out.println(con1.getOrchestraState().toStringBuilderReadFormat());
			}
		}

		System.out.println();
		stopTestOrchestra(players,con1,con2);
	}
	
	public static final boolean startTestOrchestra(List<Player> players,Conductor con1,Conductor con2) {
		boolean started = false;
		Date start = new Date();
		System.out.println("Starting orchestra ...");
		for (Player player: players) {
			started = player.start();
			if (!started) {
				break;
			}
		}
		if (started) {
			started = con2.start();
		}
		if (started) {
			started = con1.start();
		}
		System.out.println("Starting orchestra took " + ((new Date()).getTime() - start.getTime()) + " ms");
		return started;
	}

	public static final void stopTestOrchestra(List<Player> players,Conductor con1,Conductor con2) {
		System.out.println("Stopping orchestra ...");
		if (con1.isWorking()) {
			stopConductor(con1);
		}
		if (con2!=null && con2.isWorking()) {
			stopConductor(con2);
		}
		System.out.println("Stopping players ...");
		for (Player player: players) {
			if (player.isWorking()) {
				player.stop();
			}
		}
		System.out.println("Stopped orchestra");
		con1.getMessenger().whileWorking();
	}
	
	public static final void stopConductor(Conductor con) {
		MemberClient stopClient = con.getNewControlClient(con.getMessenger(),con.getUnion());
		stopClient.open();
		if (stopClient.isOpen()) {
			stopClient.sendCommand(ProtocolControl.STOP_PROGRAM);
			stopClient.close();
			System.out.println("Sent stop command to: " + con.getId());
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (con.isWorking()) {
			System.out.println("Stopping: " + con.getId());
			con.stop();
		}
	}
}
