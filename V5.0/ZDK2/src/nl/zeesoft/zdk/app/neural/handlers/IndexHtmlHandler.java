package nl.zeesoft.zdk.app.neural.handlers;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;

public class IndexHtmlHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/";
	
	public IndexHtmlHandler(NeuralApp app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		request.setContentTypeHtml();
		if (request.method.equals(HttpRequest.GET)) {
			StringBuilder html = new StringBuilder();
			html.append("<html>Hello world!</html>");
			response.setBody(html);
		}
	}
}
