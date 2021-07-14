package nl.zeesoft.zdk.app.neural.handlers;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppResourceHandler;
import nl.zeesoft.zdk.app.neural.resources.IndexJs;
import nl.zeesoft.zdk.http.HttpResponse;

public class IndexJsHandler extends NeuralAppResourceHandler {
	public static String	PATH	= "/index.js";
	
	public IndexJsHandler(NeuralApp app) {
		super(app);
		path = PATH;
		body = (new IndexJs()).render();
	}

	@Override
	protected void setContentType(HttpResponse response) {
		response.setContentTypeJavaScript();
	}
}
