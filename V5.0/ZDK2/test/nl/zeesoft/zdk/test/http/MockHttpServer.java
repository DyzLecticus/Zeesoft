package nl.zeesoft.zdk.test.http;

import nl.zeesoft.zdk.http.HttpServer;
import nl.zeesoft.zdk.http.HttpServerConfig;

public class MockHttpServer extends HttpServer {
	public MockHttpServer(HttpServerConfig config) {
		super(config);
	}

	public synchronized boolean closeSocketMockException() {
		return super.closeSocket(true);
	}
}
