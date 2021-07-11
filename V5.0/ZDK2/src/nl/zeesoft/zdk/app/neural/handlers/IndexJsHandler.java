package nl.zeesoft.zdk.app.neural.handlers;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.app.neural.resources.IndexJs;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;

public class IndexJsHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/index.js";
	
	private StringBuilder	js		= (new IndexJs()).render();
	
	public IndexJsHandler(NeuralApp app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		request.setContentTypeJavaScript();
		if (request.method.equals(HttpRequest.GET)) {
			response.setBody(js);
		}
	}
}
