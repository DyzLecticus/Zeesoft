package nl.zeesoft.zjmo.test;

import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.client.ActiveClient;
import nl.zeesoft.zjmo.orchestra.client.ConductorConnector;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.Player;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;
import nl.zeesoft.zjmo.orchestra.protocol.PublishRequest;
import nl.zeesoft.zjmo.test.mocks.MockConductor1;
import nl.zeesoft.zjmo.test.mocks.MockConductor2;
import nl.zeesoft.zjmo.test.mocks.MockPlayers;

public class TestPublishRequest extends TestObject {
	public TestPublishRequest(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestPublishRequest(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how a *PublishRequest* is executed by a *Conductor* instance.");
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
		System.out.println("// Create publish request for a certain channel");
		System.out.println("PublishRequest pr = new PublishRequest(\"Orchestra optional\",\"{\\\"something\\\":\\\"json\\\"}\");");
		System.out.println("// Publish request on the request channel through a conductor");
		System.out.println("pr = connector.publishRequest(pr);");
		System.out.println("// Close the connector");
		System.out.println("connector.close();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("This test uses *MockConductor1*, *MockConductor2* and the *MockPlayers*.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestPublishRequest.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConductor1.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConductor2.class));
		System.out.println(" * " + getTester().getLinkForClass(MockPlayers.class));
		System.out.println(" * " + getTester().getLinkForClass(PublishRequest.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows how the request is handled differently depending on the channel that the request is published on.  ");
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

		JsFile request = new JsFile();
		request.rootElement = new JsElem();
		request.rootElement.children.add(new JsElem("echoMe","Echo me this",true));
		
		if (started) {
			sleep(2000);

			ConductorConnector controlConnector = new ConductorConnector(con1.getMessenger(),con1.getUnion(),true);
			controlConnector.initialize(con1.getOrchestra(),null);
			controlConnector.open();

			ConductorConnector connector = new ConductorConnector(con1.getMessenger(),con1.getUnion(),false);
			connector.initialize(con1.getOrchestra(),null);
			connector.open();
			sleep(1000);
			
			System.out.println();
			List<ActiveClient> clients = connector.getOpenClients();
			assertEqual(clients.size(),2,"Number of open clients does not meet expectation");
			System.out.println("Open clients: " + clients.size());

			if (clients.size()>0) {
				PublishRequest pr = new PublishRequest(Orchestra.ORCHESTRA_CRITICAL,request.toStringBuilder().toString());
				System.out.println();
				ActiveClient ac = clients.get(0);
				System.out.println("Got open client to conductor: " + ac.getMember().getId());
				assertEqual(ac.getMember().getId(),con1.getId(),"Failed to connect to conductor");
				pr = connector.publishRequest(pr);
				assertEqual(pr.getError(),"","Publish request error does not match expectation");
				System.out.println("Publish request response: " + pr.toJson().toStringBuilder());
			}

			controlConnector.getClient(con2.getId()).getClient().sendCommand(ProtocolControlConductor.TAKE_MEMBER_OFFLINE,"id",con1.getId());
			sleep(1000);
			
			System.out.println();
			clients = connector.getOpenClients();
			assertEqual(clients.size(),1,"Number of open clients does not meet expectation");
			System.out.println("Open clients after taking " + con1.getId() + " offline: " + clients.size());

			if (clients.size()>0) {
				PublishRequest pr = new PublishRequest(Orchestra.ORCHESTRA_CRITICAL,request.toStringBuilder().toString());
				System.out.println();
				ActiveClient ac = clients.get(0);
				System.out.println("Got open client to conductor: " + ac.getMember().getId());
				assertEqual(ac.getMember().getId(),con2.getId(),"Failed to connect to backup conductor");
				pr = connector.publishRequest(pr);
				if (pr!=null) {
					assertEqual(pr.getError(),"Channel subscriber is not online: Conductor/0","Publish request error does not match expectation");
					System.out.println("Publish request response: " + pr.toJson().toStringBuilder());
				}
				pr = new PublishRequest(Orchestra.ORCHESTRA_OPTIONAL,request.toStringBuilder().toString());
				pr = connector.publishRequest(pr);
				if (pr!=null) {
					assertEqual(pr.getError(),"","Publish request error does not match expectation");
					System.out.println("Publish request response: " + pr.toJson().toStringBuilder());
				}
			}

			controlConnector.close();
			connector.close();
		}
		
		System.out.println();
		TestConductor.stopTestOrchestra(players,con1,con2);
	}
}
