package nl.zeesoft.zdk.app.neural.handlers;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppResourceHandler;
import nl.zeesoft.zdk.app.neural.resources.DemoTrainerJs;
import nl.zeesoft.zdk.http.HttpResponse;

public class DemoTrainerJsHandler extends NeuralAppResourceHandler {
	public static String	PATH	= "/demo-trainer.js";
	
	public DemoTrainerJsHandler(NeuralApp app) {
		super(app);
		path = PATH;
		body = (new DemoTrainerJs()).render();
	}

	@Override
	protected void setContentType(HttpResponse response) {
		response.setContentTypeJavaScript();
	}
}
