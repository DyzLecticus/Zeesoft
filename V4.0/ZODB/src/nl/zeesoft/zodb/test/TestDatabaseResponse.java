package nl.zeesoft.zodb.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.DatabaseResult;

public class TestDatabaseResponse extends TestObject {
	public TestDatabaseResponse(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDatabaseResponse(new Tester())).test(args);
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
		DatabaseResponse response = new DatabaseResponse();
		response.statusCode = 503;
		response.errors.add(new ZStringBuilder("Database is busy. Please wait."));
		testResponse(response,2);

		DatabaseResult res = new DatabaseResult();
		res.id = 1;
		res.name = "testName";
		res.obj = new JsFile();
		res.obj.rootElement = new JsElem("data","testObjectData",true);
		
		response = new DatabaseResponse();
		response.results.add(res);
		System.out.println();
		testResponse(response,2);
	}
	
	private void testResponse(DatabaseResponse response, int expectedChildren) {
		JsFile json = response.toJson();
		assertEqual(json.rootElement.children.size(),expectedChildren,"Number of children does not match expectation");
		ZStringBuilder oriStr = json.toStringBuilderReadFormat();
		System.out.println(oriStr);
		
		response = new DatabaseResponse();
		response.fromJson(json);
		
		json = response.toJson();
		ZStringBuilder newStr = json.toStringBuilderReadFormat();
		
		assertEqual(oriStr.equals(newStr),true,"Response created from JSON does not match original");
		if (!oriStr.equals(newStr)) {
			System.err.println(newStr);
		}
	}
}
