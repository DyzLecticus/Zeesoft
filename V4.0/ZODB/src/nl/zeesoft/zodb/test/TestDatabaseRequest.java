package nl.zeesoft.zodb.test;

import java.util.Date;

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
		System.out.println("This test shows how to convert a *DatabaseRequest* to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the database request");
		System.out.println("DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_LIST);");
		System.out.println("// Convert the database request to JSON");
		System.out.println("JsFile json = request.toJson();");
		System.out.println("// Convert the database request from JSON");
		System.out.println("request.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDatabaseRequest.class));
		System.out.println(" * " + getTester().getLinkForClass(DatabaseRequest.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
		request.startsWith = new ZStringBuilder("testObject");
		testRequest(request,4);

		request = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
		request.modAfter = (new Date()).getTime();
		request.modBefore = request.modAfter + 1;
		System.out.println();
		testRequest(request,5);

		request = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
		request.index = "objectNamePrefix:propertyName";
		request.operator = DatabaseRequest.OP_CONTAINS;
		request.value = new ZStringBuilder("value");
		System.out.println();
		testRequest(request,8);

		request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
		request.id = 1;
		System.out.println();
		testRequest(request,2);

		request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
		request.contains = new ZStringBuilder("testObject");
		request.modAfter = (new Date()).getTime();
		request.modBefore = request.modAfter + 1;
		System.out.println();
		testRequest(request,4);
		
		JsFile obj = new JsFile();
		obj.rootElement = new JsElem();
		obj.rootElement.children.add(new JsElem("data","addObjectData",true));
		
		request = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
		request.name = new ZStringBuilder("objectName");
		request.obj = obj;
		System.out.println();
		testRequest(request,3);

		obj = new JsFile();
		obj.rootElement = new JsElem();
		obj.rootElement.children.add(new JsElem("data","setObjectData",true));

		request = new DatabaseRequest(DatabaseRequest.TYPE_SET);
		request.id = 1;
		request.obj = obj;
		System.out.println();
		testRequest(request,3);
		
		request = new DatabaseRequest(DatabaseRequest.TYPE_REMOVE);
		request.id = 1;
		System.out.println();
		testRequest(request,2);
		
		request = new DatabaseRequest(DatabaseRequest.TYPE_REMOVE);
		request.contains = new ZStringBuilder("testObject");
		request.modAfter = (new Date()).getTime();
		request.modBefore = request.modAfter + 1;
		System.out.println();
		testRequest(request,4);
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
