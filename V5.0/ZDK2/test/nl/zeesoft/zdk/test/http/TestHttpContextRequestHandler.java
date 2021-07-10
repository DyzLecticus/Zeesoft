package nl.zeesoft.zdk.test.http;

import java.net.HttpURLConnection;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.http.HttpContextHandler;
import nl.zeesoft.zdk.http.HttpContextRequestHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.str.StrUtil;

public class TestHttpContextRequestHandler {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		assert new HttpContextHandler() != null;
		
		HttpContextRequestHandler handler = new HttpContextRequestHandler();
		assert handler.indexFileName.equals("index.html");
		assert handler.remove("/test/") == null;
		
		HttpContextHandler contextHandler = new HttpContextHandler("/index.html");
		contextHandler.allowedMethods.add(HttpRequest.GET);
		handler.put(contextHandler);		
		HttpResponse response = handler.handleRequest(new HttpRequest(HttpRequest.GET, "/"));
		assert response.code == HttpURLConnection.HTTP_OK;
		response = handler.handleRequest(new HttpRequest(HttpRequest.GET, "/index.html"));
		assert response.code == HttpURLConnection.HTTP_OK;
		
		response = handler.handleRequest(new HttpRequest(HttpRequest.GET, "/pizza"));
		assert response.code == HttpURLConnection.HTTP_NOT_FOUND;

		response = handler.handleRequest(new HttpRequest(HttpRequest.POST, "/index.html"));
		assert response.code == HttpURLConnection.HTTP_BAD_METHOD;
		
		handler.remove("/index.html");
		assert handler.handlers.size() == 0;
		
		contextHandler = new HttpContextHandler("/");
		contextHandler.allowedMethods.add(HttpRequest.GET);
		handler.put(contextHandler);
		assert StrUtil.startsWith(handler.getPathHandlersStringBuilder(), "Path handlers:\n- / (GET)");
		response = handler.handleRequest(new HttpRequest(HttpRequest.GET, "/"));
		assert response.code == HttpURLConnection.HTTP_OK;
		response = handler.handleRequest(new HttpRequest(HttpRequest.GET, "/index.html"));
		assert response.code == HttpURLConnection.HTTP_OK;
	}
}
