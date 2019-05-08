package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.http.ZHttpRequest;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestZHttpRequest extends TestObject {
	public TestZHttpRequest(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestZHttpRequest(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *ZHttpRequest* instance to call a JSON API and return the response as a *JsFile*.");
		System.out.println("A *ZHttpRequest* instance can also be used to make regular GET, POST and PUT requests.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create ZHttpRequest");
		System.out.println("ZHttpRequest http = new ZHttpRequest(\"GET\",\"http://url.domain\");");
		System.out.println("// Send the request");
		System.out.println("JsFile json = http.sendJsonRequest();");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockZHttpRequest.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestZHttpRequest.class));
		System.out.println(" * " + getTester().getLinkForClass(MockZHttpRequest.class));
		System.out.println(" * " + getTester().getLinkForClass(ZHttpRequest.class));
		System.out.println(" * " + getTester().getLinkForClass(JsFile.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the JSON response of the *MockZHttpRequest*.  ");
	}

	@Override
	protected void test(String[] args) {
		ZHttpRequest http = (ZHttpRequest) getTester().getMockedObject(MockZHttpRequest.class.getName());
		JsFile json = http.sendJsonRequest();
		System.out.println("Response:");
		System.out.println(json.toStringBuilderReadFormat());
		assertEqual(json.rootElement.children.size(),1,"The number of children does not match expectation");
	}
}
