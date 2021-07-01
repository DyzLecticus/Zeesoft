package nl.zeesoft.zdk.app.handlers;

import nl.zeesoft.zdk.app.App;
import nl.zeesoft.zdk.app.AppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;

public class IndexHandler extends AppContextHandler {
	public IndexHandler(App app) {
		super(app);
		path = "/";
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		if (request.method.equals(HttpRequest.GET)) {
			StringBuilder html = new StringBuilder();
			html.append("<html>Hello world!</html>");
			response.setBody(html);
		} else {
			response.setNotImplemented();
		}
	}
}
