package nl.zeesoft.zdk.app.neural.handlers.api;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;

public class NetworkConfigJsonHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/network/config.json";
	
	public NetworkConfigJsonHandler(NeuralApp app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
		allowedMethods.add(HttpRequest.POST);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		if (request.method.equals(HttpRequest.POST)) {
			Json json = new Json();
			json.fromStringBuilder(request.getBody());
			NetworkConfig config = (NetworkConfig) ObjectConstructor.fromJson(json);
			StringBuilder err = config.test();
			if (err.length()==0) {
				getNetworkManager().setConfig(config);
				getNetworkManager().resetNetwork();
			} else {
				response.setBadRequest();
				response.setBody(err);
			}
		} else {
			NetworkConfig config = getNetworkManager().getConfig();
			response.setBody(JsonConstructor.fromObject(config));
		}
	}
}
