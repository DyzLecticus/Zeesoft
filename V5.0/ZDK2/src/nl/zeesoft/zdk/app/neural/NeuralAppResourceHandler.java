package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;

public abstract class NeuralAppResourceHandler extends NeuralAppContextHandler {
	protected StringBuilder	body	= new StringBuilder();
	
	public NeuralAppResourceHandler(NeuralApp app) {
		super(app);
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		setContentType(response);
		if (request.method.equals(HttpRequest.GET)) {
			response.setBody(body);
		}
	}
	
	protected abstract void setContentType(HttpResponse response);
}
