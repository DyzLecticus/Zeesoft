package nl.zeesoft.zdk.test.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import nl.zeesoft.zdk.http.HttpConnections;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.http.HttpServerConnection;

public class MockHttpConnections extends HttpConnections {
	public MockHttpConnections(HttpServerConfig config) {
		super(config);
	}

	@Override
	public void acceptConnection(ServerSocket serverSocket, HttpServerConnection connection, boolean mockException) {
		super.acceptConnection(serverSocket, connection, mockException);
	}
	
	@Override
	public HttpServerConnection getNewConnection() {
		return new HttpServerConnection(config) {
			@Override
			public boolean open(Socket socket) {
				return false;
			}
		};
	}

	public ServerSocket getNewMockServerSocket() {
		ServerSocket r = null;
		try {
			r = new ServerSocket() {
				@Override
				public Socket accept() {
					return null;
				}
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}
}
