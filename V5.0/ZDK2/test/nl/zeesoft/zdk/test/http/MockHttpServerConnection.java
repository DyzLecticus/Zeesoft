package nl.zeesoft.zdk.test.http;

import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.http.HttpServerConnection;

public class MockHttpServerConnection extends HttpServerConnection {
	public MockHttpServerConnection(HttpServerConfig config) {
		super(config);
	}
	
	@Override
	public void debugLogRequestHeaders(HttpRequest request) {
		assert config.isDebugLogHeaders() == false;
		super.debugLogRequestHeaders(request);
	}
	
	@Override
	public void debugLogResponseHeaders(HttpResponse response) {
		assert config.isDebugLogHeaders() == false;
		super.debugLogResponseHeaders(response);
	}

	@Override
	public void startThread(boolean open) {
		assert thread == null;
		super.startThread(false);
		assert thread == null;
	}

	@Override
	public void handleIO() {
		assert open == false;
		super.handleIO();
	}
}
