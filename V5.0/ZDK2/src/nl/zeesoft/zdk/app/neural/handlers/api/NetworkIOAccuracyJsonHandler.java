package nl.zeesoft.zdk.app.neural.handlers.api;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.neural.network.analyzer.NetworkIOAccuracy;
import nl.zeesoft.zdk.str.StrUtil;

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
			int max = parseMaxFromQueryString(request);
			NetworkIOAccuracy acc = getNetworkRecorder().getAccuracy(max);
			response.setBody(JsonConstructor.fromObject(acc));
		} else {
			response.setContentTypeJson();
		}
	}
	
	protected int parseMaxFromQueryString(HttpRequest request) {
		int r = 1000;
		StringBuilder str = new StringBuilder(request.query);
		if (StrUtil.startsWith(str, "max=")) {
			str = StrUtil.substring(str, 4, str.length());
		}
		if (str.length()>0) {
			r = Util.parseInt(str.toString());
		}
		return r;
	}
}
