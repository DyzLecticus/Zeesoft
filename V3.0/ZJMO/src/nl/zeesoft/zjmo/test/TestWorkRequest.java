package nl.zeesoft.zjmo.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.Player;
import nl.zeesoft.zjmo.orchestra.members.WorkClient;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;
import nl.zeesoft.zjmo.orchestra.protocol.WorkRequest;
import nl.zeesoft.zjmo.test.mocks.MockConductor1;
import nl.zeesoft.zjmo.test.mocks.MockConductor2;
import nl.zeesoft.zjmo.test.mocks.MockPlayers;
import nl.zeesoft.zjmo.test.mocks.PlayerCommandWorker;

public class TestWorkRequest extends TestObject {
	public TestWorkRequest(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestWorkRequest(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how a *Conductor* handles work requests.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create conductor");
		System.out.println("Conductor con = new Conductor(null,orchestra,0);");
		System.out.println("// Start the conductor");
		System.out.println("boolean started = con.start();");
		System.out.println("// Create client using conductor work port settings");
		System.out.println("MemberClient client = new MemberClient(\"localhost\",5432);");
		System.out.println("// Create work request");
		System.out.println("WorkRequest wr = new WorkRequest(\"Database X\",\"{\\\"something\\\":\\\"json\\\"}\");");
		System.out.println("// Send work request to conductor");
		System.out.println("ZStringBuilder response = client.writeOutputReadInput(wr.toJson().toStringBuilder());");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("This test uses *MockConductor1*, *MockConductor2* and the *MockPlayers*.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestWorkRequest.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConductor1.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConductor2.class));
		System.out.println(" * " + getTester().getLinkForClass(MockPlayers.class));
		System.out.println(" * " + getTester().getLinkForClass(WorkRequest.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows how one of the players handles a work request while draining offline, allowing a backup player to take over the position for the next work requests.  ");
	}

	@Override
	protected void test(String[] args) {
		boolean started = false;
		Date start = new Date();

		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) getTester().getMockedObject(MockPlayers.class.getName());
		Conductor con1 = (Conductor) getTester().getMockedObject(MockConductor1.class.getName());
		Conductor con2 = (Conductor) getTester().getMockedObject(MockConductor2.class.getName());
		
		started = TestConductor.startTestOrchestra(players,con1,con2);
		assertEqual(started,true,"Failed to start orchestra");

		int i = 0;
		Player dbX = null;
		Player dbX1 = null;
		for (Player player: players) {
			if (i==0) {
				dbX = player;
			} else if (i==4) {
				dbX1 = player;
			}
			i++;
		}

		WorkRequest wr = new WorkRequest();
		JsFile request = new JsFile();
		request.rootElement = new JsElem();
		request.rootElement.children.add(new JsElem("echoMe","Echo me this",true));
		request.rootElement.children.add(new JsElem("sleep","1000"));
		
		if (started) {
			sleep(2000);
			ZStringBuilder response = null;

			WorkClient client = new WorkClient("localhost",5432,5000);
			client.open();
			assertEqual(client.isOpen(),true,"Failed to open the work client");
			if (client.isOpen()) {
				response = client.sendRequest(wr);
				wr.fromStringBuilder(response);
				assertEqual(wr.getError(),"Unrecognized input","Response error does not match expectation");
				wr.setError("");

				wr.setPositionName("Database Z");

				response = client.sendRequest(wr);
				wr.fromStringBuilder(response);
				assertEqual(wr.getError(),"Work request is empty","Response error does not match expectation");
				wr.setError("");
				
				wr.setPositionName("Database Z");
				wr.setRequest(request);

				response = client.sendRequest(wr);
				wr.fromStringBuilder(response);
				assertEqual(wr.getError(),"Work request position does not exist: Database Z","Response error does not match expectation");
				wr.setError("");

				PlayerCommandWorker pcw = new PlayerCommandWorker(con1,dbX,ProtocolControlConductor.DRAIN_MEMBER_OFFLINE);
				//PlayerCommandWorker pcw = new PlayerCommandWorker(con,dbX,ProtocolControlConductor.TAKE_MEMBER_OFFLINE);
				pcw.start();
				
				wr.setPositionName("Database X");
				wr.setRequest(request);

				System.out.println();	
				System.out.println("Sending work request: " + wr.toJson().toStringBuilder());
				response = client.sendRequest(wr);

				System.out.println();	
				System.out.println("Work request response: " + response);
				wr.fromStringBuilder(response);
				assertEqual(wr.getResponse()!=null,true,"Response is empty");
				if (wr.getResponse()!=null) {
					assertEqual(wr.getRequest().toStringBuilder(),wr.getResponse().toStringBuilder(),"Response does not equal request");
				}

				sleep(2000);

				request = new JsFile();
				request.rootElement = new JsElem();
				request.rootElement.children.add(new JsElem("echoMe","Echo me this",true));
				
				wr.setPositionName("Database X");
				wr.setRequest(request);
				wr.setResponse(null);
				
				System.out.println();	
				start = new Date();
				System.out.println("Sending work request to backup: " + wr.toJson().toStringBuilder());
				response = client.sendRequest(wr);
				System.out.println("Work request response from backup: " + response);
				System.out.println("First work request to backup took " + ((new Date()).getTime() - start.getTime()) + " ms");
				wr.fromStringBuilder(response);
				assertEqual(wr.getResponse()!=null,true,"Response is empty");
				if (wr.getResponse()!=null) {
					assertEqual(wr.getRequest().toStringBuilder(),wr.getResponse().toStringBuilder(),"Response does not equal request");
				}

				wr.setPositionName("Database X");
				wr.setRequest(request);
				wr.setResponse(null);
				
				System.out.println();	
				start = new Date();
				System.out.println("Sending work request to backup: " + wr.toJson().toStringBuilder());
				response = client.sendRequest(wr);
				System.out.println("Work request response from backup: " + response);
				System.out.println("Second work request to backup took " + ((new Date()).getTime() - start.getTime()) + " ms");
				wr.fromStringBuilder(response);
				assertEqual(wr.getResponse()!=null,true,"Response is empty");
				if (wr.getResponse()!=null) {
					assertEqual(wr.getRequest().toStringBuilder(),wr.getResponse().toStringBuilder(),"Response does not equal request");
				}

				request = new JsFile();
				request.rootElement = new JsElem();
				request.rootElement.children.add(new JsElem("echoMe","Echo me this",true));
				request.rootElement.children.add(new JsElem("sleep","3000"));
				
				wr.setPositionName("Database X");
				wr.setRequest(request);
				wr.setResponse(null);
				
				System.out.println();
				start = new Date();
				System.out.println("Sending work request to backup: " + wr.toJson().toStringBuilder());
				response = client.sendRequest(wr);
				System.out.println("Work request response from backup: " + response);
				System.out.println("Time out work request to backup took " + ((new Date()).getTime() - start.getTime()) + " ms");
				wr.fromStringBuilder(response);
				assertEqual(wr.getResponse()==null,true,"Response is not empty");
				assertEqual(wr.getError(),"Work request timed out on: Database X/1","Time out error does not match expectation");
				
				System.out.println();
				System.out.println("Player state JSON:");
				System.out.println(con1.getMemberState(dbX1.getId()).toStringBuilderReadFormat());
				
				sleep(1000);

				System.out.println();
				System.out.println("Player state JSON:");
				System.out.println(con1.getMemberState(dbX1.getId()).toStringBuilderReadFormat());
			}
		}
		
		System.out.println();
		TestConductor.stopTestOrchestra(players,con1,con2);
	}
}
