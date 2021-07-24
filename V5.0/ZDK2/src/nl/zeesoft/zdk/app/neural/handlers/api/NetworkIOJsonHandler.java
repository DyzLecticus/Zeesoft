package nl.zeesoft.zdk.app.neural.handlers.api;

import java.util.List;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;
import nl.zeesoft.zdk.neural.network.NetworkIO;

public class NetworkIOJsonHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/network/io.json";
	
	public NetworkIOJsonHandler(NeuralApp app) {
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
			handleGetRequest(request, response);
		} else {
			response.setContentTypeJson();
		}
	}
	
	protected void handlePostRequest(HttpRequest request, HttpResponse response) {
		NetworkIO io = parseBody(request, response);
		if (io!=null) {
			if (getNetworkManager().processNetworkIO(io, getNetworkRecorder())) {
				response.setBody(JsonConstructor.fromObjectUseConvertors(io));
			} else {
				setResponseUnavailable(response);
			}
		}
	}
	
	protected void handleGetRequest(HttpRequest request, HttpResponse response) {
		NetworkIO io = new NetworkIO();
		List<String> inputNames = getNetworkManager().getConfig().getInputNames();
		for (String name: inputNames) {
			Object value = 0F;
			if (name.equals("DateTime")) {
				value = 0L;
			}
			io.addInput(name, value);
		}
		response.setBody(JsonConstructor.fromObject(io));
	}
	
	protected NetworkIO parseBody(HttpRequest request, HttpResponse response) {
		NetworkIO r = (NetworkIO) ObjectConstructor.fromJson(new Json(request.getBody()));
		if (r==null) {
			response.setBadRequest();
			response.setBody(app.getParseBodyJsonError(NetworkIO.class));
		}
		return r;
	}
}
