package nl.zeesoft.zjmo.test;

import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zjmo.orchestra.client.ActiveClient;
import nl.zeesoft.zjmo.orchestra.client.ConductorConnector;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.Player;
import nl.zeesoft.zjmo.orchestra.protocol.WorkRequest;
import nl.zeesoft.zjmo.test.mocks.MockConductor1;
import nl.zeesoft.zjmo.test.mocks.MockConductor2;
import nl.zeesoft.zjmo.test.mocks.MockPlayers;

public class TestConductorConnector extends TestObject {
	public TestConductorConnector(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestConductorConnector(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *ConductorConnector* to create reliable (external) connections to a working orchestra.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create conductor");
		System.out.println("Conductor con = new Conductor(null,orchestra,0);");
		System.out.println("// Start the conductor");
		System.out.println("boolean started = con.start();");
		System.out.println("// Create conductor connector");
		System.out.println("ConductorConnector connector = new ConductorConnector(null,null,false);");
		System.out.println("// Initialize the connector");
		System.out.println("connector.initialize(con.getOrchestra(),null);");
		System.out.println("// Open the connector");
		System.out.println("connector.open();");
		System.out.println("// Create work request");
		System.out.println("WorkRequest wr = new WorkRequest(\"Database X\",\"{\\\"something\\\":\\\"json\\\"}\");");
		System.out.println("// Send work request to a conductor");
		System.out.println("wr = connector.sendWorkRequest(wr);");
		System.out.println("// Close the connector");
		System.out.println("connector.close();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("This test uses *MockConductor1*, *MockConductor2* and the *MockPlayers*.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestConductorConnector.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConductor1.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConductor2.class));
		System.out.println(" * " + getTester().getLinkForClass(MockPlayers.class));
		System.out.println(" * " + getTester().getLinkForClass(ConductorConnector.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows how the *ConductorConnector* always maintains open connections to working conductors.  ");
	}

	@Override
	protected void test(String[] args) {
		boolean started = false;

		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) getTester().getMockedObject(MockPlayers.class.getName());
		Conductor con1 = (Conductor) getTester().getMockedObject(MockConductor1.class.getName());
		Conductor con2 = (Conductor) getTester().getMockedObject(MockConductor2.class.getName());
		
		started = TestConductor.startTestOrchestra(players,con1,con2);
		assertEqual(started,true,"Failed to start orchestra");

		WorkRequest wr = new WorkRequest();
		JsFile request = new JsFile();
		request.rootElement = new JsElem();
		request.rootElement.children.add(new JsElem("echoMe","Echo me this",true));
		request.rootElement.children.add(new JsElem("sleep","1000"));
		
		if (started) {
			sleep(2000);

			System.out.println();
			System.out.println("Testing connector ...");
			
			ConductorConnector connector = new ConductorConnector(con1.getMessenger(),con1.getUnion(),false);
			connector.initialize(con1.getOrchestra(),null);
			connector.open();
			sleep(1000);
			
			System.out.println();
			List<ActiveClient> clients = connector.getOpenClients();
			assertEqual(clients.size(),2,"Number of open clients does not meet expectation");
			System.out.println("Open clients: " + clients.size());

			con1.takeOffLine();
			sleep(1000);
			
			System.out.println();
			clients = connector.getOpenClients();
			assertEqual(clients.size(),1,"Number of open clients does not meet expectation");
			System.out.println("Open clients after taking " + con1.getId() + " offline: " + clients.size());

			if (clients.size()>0) {
				System.out.println();
				ActiveClient ac = clients.get(0);
				System.out.println("Got open client to conductor: " + ac.getMember().getId());
				assertEqual(ac.getMember().getId(),con2.getId(),"Failed to connect to backup conductor");
				connector.sendWorkRequest(wr);
			}

			con1.bringOnLine();
			sleep(1000);

			System.out.println();
			clients = connector.getOpenClients();
			assertEqual(clients.size(),2,"Number of open clients does not meet expectation");
			System.out.println("Open clients after bringing " + con1.getId() + " online: " + clients.size());
			
			if (clients.size()>0) {
				System.out.println();
				ActiveClient ac = clients.get(0);
				System.out.println("Got open client to conductor: " + ac.getMember().getId());
				assertEqual(ac.getMember().getId(),con1.getId(),"Failed to connect to backup conductor");
				connector.sendWorkRequest(wr);
			}

			connector.close();
		}
		
		System.out.println();
		TestConductor.stopTestOrchestra(players,con1,con2);
	}
}
