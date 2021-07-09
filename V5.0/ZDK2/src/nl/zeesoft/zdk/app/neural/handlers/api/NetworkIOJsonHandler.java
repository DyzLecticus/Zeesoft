package nl.zeesoft.zdk.app.neural.handlers.api;

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
		allowedMethods.add(HttpRequest.POST);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		NetworkIO io = parseBody(request, response);
		if (io!=null) {
			if (getNetworkManager().processNetworkIO(io)) {
				response.setBody(JsonConstructor.fromObjectUseConvertors(io));
			} else {
				setResponseUnavailable(response);
			}
		}
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
