package nl.zeesoft.zdk.app.neural.handlers.api;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.neural.model.CellStats;

public class NetworkStatsJsonHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/network/stats.json";
	
	public NetworkStatsJsonHandler(NeuralApp app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		if (request.method.equals(HttpRequest.GET)) {
			CellStats stats = getNetworkManager().getCellStats();
			if (stats!=null) {
				response.setBody(JsonConstructor.fromObject(stats));
			} else {
				setResponseUnavailable(response);
			}
		} else {
			response.setContentTypeJson();
		}
	}
}
