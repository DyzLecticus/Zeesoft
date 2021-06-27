package nl.zeesoft.zdk.test.http;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.http.HttpClient;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpRequestHandler;
import nl.zeesoft.zdk.http.HttpRequestStringConvertor;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.http.HttpResponseStringConvertor;
import nl.zeesoft.zdk.http.HttpServer;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.str.ObjectStringConvertors;

public class TestHttpServer {
	private static String	LOCALHOST	= "http://127.0.0.1";
	private static String	SERVER_URL	= LOCALHOST + ":1234";
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		HttpClient testClient = new HttpClient();
		assert !testClient.connect("pizza");
		assert !testClient.connect(LOCALHOST);
		
		HttpRequestHandler handler = new HttpRequestHandler() {
			@Override
			public void handleRequest(HttpRequest request, HttpResponse response) {
				super.handleRequest(request, response);
				if (request.method.length()>0) {
					assert request.path.length()>0;
					assert request.protocol.equals("HTTP/1.1");
					assert request.head.headers.size()>0;
					response.message = "Coolio";
					response.body.append("<html></html>");
				}
			}
		};
		
		HttpServerConfig config = new HttpServerConfig();
		config.setPort(1234);
		config.setDebugLogHeaders(true);
		config.setRequestConvertor((HttpRequestStringConvertor) ObjectStringConvertors.getConvertor(HttpRequest.class));
		config.setResponseConvertor((HttpResponseStringConvertor) ObjectStringConvertors.getConvertor(HttpResponse.class));
		config.setRequestHandler(handler);
		
		MockHttpConnection connection = new MockHttpConnection();
		assert !connection.isOpen();
		assert connection.readLine(null, true);
		assert !connection.createIO(true);
		assert !connection.destroyReaderAndSocket(true);
		connection.setOpen(true);
		assert connection.readLine(null, true);
		assert connection.isOpen();
		connection.close();
		assert connection.isOpen();
		
		HttpServerConfig config2 = new HttpServerConfig();
		config2.setDebugLogHeaders(false);
		MockHttpServerConnection serverConnection = new MockHttpServerConnection(config2);
		serverConnection.debugLogRequestHeaders(new HttpRequest());
		serverConnection.startThread(false);
		serverConnection.handleIO();
				
		MockHttpConnections connections = new MockHttpConnections(config);
		connections.open = true;
		connections.acceptConnection(null, null, true);
		connections.acceptConnection(connections.getNewMockServerSocket(), connections.getNewConnection(), false);
		
		MockHttpServer server = new MockHttpServer(config);
		assert !server.closeSocketMockException();
		assert !server.isOpen();
		assert !server.close();
		assert server.open();
		assert server.isOpen();
		assert !server.open();
		
		HttpServer server2 = new HttpServer(config);
		assert !server2.open();
		Util.sleep(10);
		
		assert server.getNumberOfConnections() == 0;
		HttpClient client = new HttpClient();
		assert !client.isConnected();
		assert client.sendRequest(null) == null;
		assert client.connect(SERVER_URL);
		Util.sleep(10);
		assert server.getNumberOfConnections() == 1;
		assert client.disconnect();
		Util.sleep(10);
		assert server.getNumberOfConnections() == 0;
		
		assert !client.isConnected();
		assert client.connect(SERVER_URL);
		HttpRequest request = new HttpRequest("GET", "/index.html");
		request.head.add("Connection", "close");
		HttpResponse response = client.sendRequest(request);
		assert response.message.equals("Coolio");
		//assert response.body.toString().equals("<html></html>");
		
		assert server.close();
		assert !server.isOpen();
		assert server.getNumberOfConnections() == 0;
		
		assert client.disconnect();
	}
}
