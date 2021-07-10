package nl.zeesoft.zdk.app.neural.handlers.api;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;

public class NetworkSettingsJsonHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/network/settings.json";
	
	public NetworkSettingsJsonHandler(NeuralApp app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
		allowedMethods.add(HttpRequest.POST);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		if (request.method.equals(HttpRequest.POST)) {
			NetworkSettings settings = parseBody(request, response);
			if (settings!=null && !settings.configure(getNetworkManager())) {
				setResponseUnavailable(response);
			}
		} else if (request.method.equals(HttpRequest.GET)) {
			NetworkSettings settings = new NetworkSettings(getNetworkManager());
			if (settings.processorLearning!=null) {
				response.setBody(JsonConstructor.fromObject(settings));
			} else {
				setResponseUnavailable(response);
			}
		} else {
			request.setContentTypeJson();
		}
	}
	
	protected NetworkSettings parseBody(HttpRequest request, HttpResponse response) {
		NetworkSettings r = (NetworkSettings) ObjectConstructor.fromJson(new Json(request.getBody()));
		if (r==null) {
			response.setBadRequest();
			response.setBody(app.getParseBodyJsonError(NetworkSettings.class));
		}
		return r;
	}
}
