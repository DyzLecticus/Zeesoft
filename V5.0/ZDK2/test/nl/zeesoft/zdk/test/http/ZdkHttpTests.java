package nl.zeesoft.zdk.test.http;

import nl.zeesoft.zdk.Console;

public class ZdkHttpTests {
	public static void main(String[] args) {
		Console.log("Test HttpServer ...");
		TestHttpIO.main(args);
		TestHttpServer.main(args);
		TestHttpContextRequestHandler.main(args);
	}
}
