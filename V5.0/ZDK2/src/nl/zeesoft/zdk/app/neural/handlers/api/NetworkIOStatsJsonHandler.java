package nl.zeesoft.zdk.app.neural.handlers.api;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.neural.network.analyzer.NetworkIOStats;

public class NetworkIOStatsJsonHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/network/io/stats.json";
	
	public NetworkIOStatsJsonHandler(NeuralApp app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		if (request.method.equals(HttpRequest.GET)) {
			NetworkIOStats stats = getNetworkRecorder().getAverageStats();
			response.setBody(JsonConstructor.fromObject(stats));
		} else {
			response.setContentTypeJson();
		}
	}
}
