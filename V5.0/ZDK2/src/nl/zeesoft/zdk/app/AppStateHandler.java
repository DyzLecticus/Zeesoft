package nl.zeesoft.zdk.app;

import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;

public class AppStateHandler extends AppContextHandler {
	public static String	PATH	= "/app/state/";
	
	public AppStateHandler(App app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		StringBuilder html = new StringBuilder();
		html.append(app.getState());
		response.setBody(html);
	}
}
