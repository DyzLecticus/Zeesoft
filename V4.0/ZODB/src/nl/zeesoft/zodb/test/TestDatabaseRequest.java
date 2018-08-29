package nl.zeesoft.zodb.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.db.DatabaseRequest;

public class TestDatabaseRequest extends TestObject {
	public TestDatabaseRequest(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDatabaseRequest(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		// TODO: Describe
		/*
		System.out.println("This test shows how to convert a *TestConfiguration* to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the test configuration");
		System.out.println("TestConfiguration tco = new TestConfiguration();");
		System.out.println("// Initialize the test configuration");
		System.out.println("tco.initialize();");
		System.out.println("// Convert the test configuration to JSON");
		System.out.println("JsFile json = tco.toJson();");
		System.out.println("// Convert the test configuration from JSON");
		System.out.println("tco.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestTestConfiguration.class));
		System.out.println(" * " + getTester().getLinkForClass(TestConfiguration.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		DatabaseRequest request = new DatabaseRequest();
		request.type = DatabaseRequest.TYPE_LIST;
		request.startsWith = "testObject";
		testRequest(request,4);
		
		request = new DatabaseRequest();
		request.type = DatabaseRequest.TYPE_GET;
		request.id = 1;
		System.out.println();
		testRequest(request,2);
		
		JsFile obj = new JsFile();
		obj.rootElement = new JsElem();
		obj.rootElement.children.add(new JsElem("data","addObjectData",true));
		
		request = new DatabaseRequest();
		request.type = DatabaseRequest.TYPE_ADD;
		request.obj = obj;
		System.out.println();
		testRequest(request,2);

		obj = new JsFile();
		obj.rootElement = new JsElem();
		obj.rootElement.children.add(new JsElem("data","setObjectData",true));

		request = new DatabaseRequest();
		request.type = DatabaseRequest.TYPE_SET;
		request.id = 1;
		request.obj = obj;
		System.out.println();
		testRequest(request,3);
		
		request = new DatabaseRequest();
		request.type = DatabaseRequest.TYPE_REMOVE;
		request.id = 1;
		System.out.println();
		testRequest(request,2);
	}
	
	private void testRequest(DatabaseRequest request, int expectedChildren) {
		JsFile json = request.toJson();
		assertEqual(json.rootElement.children.size(),expectedChildren,"Number of children does not match expectation");
		ZStringBuilder oriStr = json.toStringBuilderReadFormat();
		System.out.println(oriStr);
		
		request = new DatabaseRequest();
		request.fromJson(json);
		
		json = request.toJson();
		ZStringBuilder newStr = json.toStringBuilderReadFormat();
		
		assertEqual(oriStr.equals(newStr),true,"Request created from JSON does not match original");
		if (!oriStr.equals(newStr)) {
			System.err.println(newStr);
		}
	}
}
