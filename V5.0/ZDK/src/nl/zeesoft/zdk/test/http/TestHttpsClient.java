package nl.zeesoft.zdk.test.http;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.http.HttpClientManager;
import nl.zeesoft.zdk.http.HttpsClient;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerManager;

public class TestHttpsClient extends TestObject {
	public TestHttpsClient(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestHttpsClient(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test is not included in the ZDK test set");
	}

	@Override
	protected void test(String[] args) {
		Logger logger = new Logger(true);
		HttpsClient request = new HttpsClient(logger,"GET","https://www.google.com/");
		
		request.sendRequest(null,false,false);
		
		System.out.println("Response status: " + request.getResponseCode() + " " + request.getResponseMessage());
		
		System.out.println();
		System.out.println("Response headers;");
		System.out.println(request.getResponseHeaders().toStr());
		
		System.out.println();
		System.out.println("Response body;");
		System.out.println(request.getResponseBody());
		
		assertEqual(HttpClientManager.getConnectedClients().size(),0,"Number of connected clients does not match expectation");		
		assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
	}
}
