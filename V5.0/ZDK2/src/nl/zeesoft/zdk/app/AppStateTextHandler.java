package nl.zeesoft.zdk.app;

import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;

public class AppStateTextHandler extends AppContextHandler {
	public static String	PATH	= "/app/state.txt";
	
	public AppStateTextHandler(App app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		response.setContentTypeText();
		response.setBody(new StringBuilder(app.getState()));
	}
}
