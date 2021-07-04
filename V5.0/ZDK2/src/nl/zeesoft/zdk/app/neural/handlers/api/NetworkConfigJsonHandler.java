package nl.zeesoft.zdk.app.neural.handlers.api;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;

public class NetworkConfigJsonHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/network/config.json";
	
	public NetworkConfigJsonHandler(NeuralApp app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		NetworkConfig config = getNetworkManager().getNetworkConfig();
		response.setBody(JsonConstructor.fromObject(config));
	}
}
