package nl.zeesoft.zjmo.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zjmo.orchestra.controller.OrchestraController;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.Player;
import nl.zeesoft.zjmo.test.mocks.MockConductor1;
import nl.zeesoft.zjmo.test.mocks.MockConductor2;
import nl.zeesoft.zjmo.test.mocks.MockPlayers;
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
		
		TestOrchestra orch = new TestOrchestra();
		orch.initialize();
		OrchestraController controller = orch.getNewController(false);
		
		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) getTester().getMockedObject(MockPlayers.class.getName());
		Conductor con1 = (Conductor) getTester().getMockedObject(MockConductor1.class.getName());
		Conductor con2 = (Conductor) getTester().getMockedObject(MockConductor2.class.getName());
		
		started = TestConductor.startTestOrchestra(players,con1,con2);
		assertEqual(started,true,"Failed to start orchestra");
		
		if (started) {
			sleep(500);
			
			String err = controller.start();
			assertEqual(err,"","Failed to start the controller");
			
			sleep(1000);
			System.out.println();
			System.out.println(con1.getOrchestraState().toStringBuilderReadFormat());
			
			if (controller.isWorking()) {
				System.out.println();
				System.out.println("Stop the controller to stop the test orchestra");
				while (controller.isWorking()) {
					sleep(100);
				}
			}
		}

		System.out.println();
		TestConductor.stopTestOrchestra(players,con1,con2);
		
		System.exit(0);
	}
}
