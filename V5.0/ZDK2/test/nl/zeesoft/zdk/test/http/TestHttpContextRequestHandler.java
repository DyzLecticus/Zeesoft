package nl.zeesoft.zdk.test.http;

import java.net.HttpURLConnection;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.http.HttpContextHandler;
import nl.zeesoft.zdk.http.HttpContextRequestHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;

public class TestHttpContextRequestHandler {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		assert new HttpContextHandler() != null;
		
		HttpContextRequestHandler handler = new HttpContextRequestHandler();
		assert handler.indexFileName.equals("index.html");
		assert handler.remove("/test/") == null;
		
		handler.put(new HttpContextHandler("/index.html"));		
		HttpResponse response = handler.handleRequest(new HttpRequest(HttpRequest.GET, "/"));
		assert response.code == HttpURLConnection.HTTP_OK;
		response = handler.handleRequest(new HttpRequest(HttpRequest.GET, "/index.html"));
		assert response.code == HttpURLConnection.HTTP_OK;
		
		response = handler.handleRequest(new HttpRequest(HttpRequest.GET, "/pizza"));
		assert response.code == HttpURLConnection.HTTP_NOT_FOUND;

		handler.remove("/index.html");
		assert handler.handlers.size() == 0;
		
		handler.put(new HttpContextHandler("/"));
		response = handler.handleRequest(new HttpRequest(HttpRequest.GET, "/"));
		assert response.code == HttpURLConnection.HTTP_OK;
		response = handler.handleRequest(new HttpRequest(HttpRequest.GET, "/index.html"));
		assert response.code == HttpURLConnection.HTTP_OK;
	}
}
