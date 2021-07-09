package nl.zeesoft.zdk.app.neural.handlers.api;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;

public class NetworkStateTextHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/network/state.txt";
	
	public NetworkStateTextHandler(NeuralApp app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		response.setContentTypeText();
		if (request.method.equals(HttpRequest.GET)) {
			response.setBody(new StringBuilder(getNetworkManager().getState()));
		}
	}
}
