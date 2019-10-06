package nl.zeesoft.zodb.test;

import java.util.Date;

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
		System.out.println("This test shows how to convert a *DatabaseResponse* to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the database response");
		System.out.println("DatabaseResponse response = new DatabaseResponse();");
		System.out.println("// Convert the database response to JSON");
		System.out.println("JsFile json = response.toJson();");
		System.out.println("// Convert the database response from JSON");
		System.out.println("response.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDatabaseResponse.class));
		System.out.println(" * " + getTester().getLinkForClass(DatabaseResponse.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		DatabaseResponse response = new DatabaseResponse();
		response.statusCode = 503;
		response.errors.add(new ZStringBuilder("Database is busy. Please wait."));
		testResponse(response,2);

		response = new DatabaseResponse();
		DatabaseResult res = new DatabaseResult();
		res.id = 1;
		res.name = new ZStringBuilder("testName");
		res.modified = (new Date()).getTime();
		res.object = new JsFile();
		res.object.rootElement = new JsElem();
		res.object.rootElement.children.add(new JsElem("data","testObjectData",true));
		response.results.add(res);
		System.out.println();
		testResponse(response,2);

		response = new DatabaseResponse();
		response.size = 2;
		res = new DatabaseResult();
		res.id = 1;
		res.name = new ZStringBuilder("testName1");
		res.modified = (new Date()).getTime();
		response.results.add(res);
		res = new DatabaseResult();
		res.id = 1;
		res.name = new ZStringBuilder("testName2");
		res.modified = (new Date()).getTime();
		response.results.add(res);
		System.out.println();
		testResponse(response,3);
	}
	
	private void testResponse(DatabaseResponse response, int expectedChildren) {
		JsFile json = response.toJson();
		assertEqual(json.rootElement.children.size(),expectedChildren,"Number of children does not match expectation");
		
		if (testJsAble(response,new DatabaseResponse(),"Response created from JSON does not match original")) {
			System.out.println(json.toStringBuilderReadFormat());
		}
	}
}
