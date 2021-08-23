package nl.zeesoft.zdk.app.neural.handlers;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppResourceHandler;
import nl.zeesoft.zdk.app.neural.resources.ManagerJs;
import nl.zeesoft.zdk.http.HttpResponse;

public class ManagerJsHandler extends NeuralAppResourceHandler {
	public static String	PATH	= "/manager.js";
	
	public ManagerJsHandler(NeuralApp app) {
		super(app);
		path = PATH;
		body = (new ManagerJs()).render();
	}

	@Override
	protected void setContentType(HttpResponse response) {
		response.setContentTypeJavaScript();
	}
}
