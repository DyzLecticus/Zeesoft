package nl.zeesoft.zdk.test.http;

import nl.zeesoft.zdk.Console;

public class ZdkHttpTests {
	public static void main(String[] args) {
		Console.log("Test HttpServer ...");
		TestHttpRequest.main(args);
		TestHttpServer.main(args);
	}
}
