package nl.zeesoft.zjmo.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.controller.OrchestraController;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.Player;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;
import nl.zeesoft.zjmo.test.mocks.MockConductor;
import nl.zeesoft.zjmo.test.mocks.MockPlayers;
import nl.zeesoft.zjmo.test.mocks.MockTestOrchestra;
import nl.zeesoft.zjmo.test.mocks.TestOrchestra;

public class TestOrchestraController extends TestObject {
	public TestOrchestraController(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestOrchestraController(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how the *Controller* can be used to manage an active orchestra.");
		System.out.println("It is not included in the ZJMO test set because it waits for user interaction.");
	}

	@Override
	protected void test(String[] args) {
		boolean started = false;
		Date start = new Date();
		
		TestOrchestra orch = (TestOrchestra) getTester().getMockedObject(MockTestOrchestra.class.getName());
		OrchestraController controller = new OrchestraController(orch);
		
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
			sleep(500);
			
			String err = controller.start();
			assertEqual(err,"","Failed to start the controller");
			
			sleep(1000);
			System.out.println();
			System.out.println(con.getOrchestraState().toStringBuilderReadFormat());
			
			if (controller.isWorking()) {
				System.out.println();
				System.out.println("Stop the controller to stop the test orchestra");
				while (controller.isWorking()) {
					sleep(100);
				}
			}
			
			ZStringBuilder response = null;

			MemberClient stopClient = new MemberClient("localhost",5433);
			stopClient.open();
			assertEqual(stopClient.isOpen(),true,"Failed to open the control client");
			if (stopClient.isOpen()) {
				System.out.println();
				System.out.println("Stopping conductor ...");
				response = stopClient.sendCommand(ProtocolControl.STOP_PROGRAM);
				assertEqual(response.toString(),"{\"command\":\"CLOSE_SESSION\"}","Stop program response does not match expectation");
				stopClient.close();
			}
			
			sleep(1000);
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
		con.getMessenger().whileWorking();
		
		// TODO: Include exit in controller stop.
		System.exit(0);
	}
}
