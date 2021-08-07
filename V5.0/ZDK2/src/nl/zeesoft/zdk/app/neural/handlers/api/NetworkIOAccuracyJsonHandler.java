package nl.zeesoft.zdk.app.neural.handlers.api;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.neural.network.analyzer.NetworkIOAccuracy;

public class NetworkIOAccuracyJsonHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/network/io/accuracy.json";
	
	public NetworkIOAccuracyJsonHandler(NeuralApp app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		if (request.method.equals(HttpRequest.GET)) {
			NetworkIOAccuracy acc = getNetworkRecorder().getAccuracy();
			response.setBody(JsonConstructor.fromObject(acc));
		} else {
			response.setContentTypeJson();
		}
	}
}
