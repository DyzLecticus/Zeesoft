package nl.zeesoft.zdk.app;

import java.net.HttpURLConnection;

import nl.zeesoft.zdk.http.HttpContextRequestHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;

public class AppContextRequestHandler extends HttpContextRequestHandler {
	protected App	app		= null;
	
	public AppContextRequestHandler(App app) {
		this.app = app;
	}
	
	@Override
	public void handleRequest(HttpRequest request, HttpResponse response) {
		if (app.isStarted() || request.path.startsWith(AppStateTextHandler.PATH)) {
			super.handleRequest(request, response);
		} else {
			serviceUnavailable(request, response);
		}
	}

	protected void initializeContextHandlers() {
		put(new AppStateTextHandler(app));
	}
	
	protected void serviceUnavailable(HttpRequest request, HttpResponse response) {
		response.code = HttpURLConnection.HTTP_UNAVAILABLE;
		response.message = "Service Unavailable";
	}
}
