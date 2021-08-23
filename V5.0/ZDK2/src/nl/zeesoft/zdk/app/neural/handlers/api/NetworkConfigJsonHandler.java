package nl.zeesoft.zdk.app.neural.handlers.api;

import java.net.HttpURLConnection;

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
			handlePostRequest(request, response);
		} else if (request.method.equals(HttpRequest.GET)) {
			NetworkConfig config = getNetworkManager().getConfig();
			response.setBody(JsonConstructor.fromObject(config));
		} else {
			response.setContentTypeJson();
		}
	}
	
	protected void handlePostRequest(HttpRequest request, HttpResponse response) {
		NetworkConfig config = parseBody(request, response);
		testConfig(config, response);
		if (response.code == HttpURLConnection.HTTP_OK && checkNetworkReady(response)) {
			getNetworkManager().setConfig(config);
			resetNetwork(response);
		}
	}
	
	protected NetworkConfig parseBody(HttpRequest request, HttpResponse response) {
		NetworkConfig r = (NetworkConfig) ObjectConstructor.fromJson(new Json(request.getBody()));
		if (r==null) {
			response.setBadRequest();
			response.setBody(app.getParseBodyJsonError(NetworkConfig.class));
		}
		return r;
	}
	
	protected void testConfig(NetworkConfig config, HttpResponse response) {
		if (config!=null) {
			StringBuilder err = config.test();
			if (err.length()>0) {
				response.setBadRequest();
				response.setBody(err);
			}
		}
	}
}
