package nl.zeesoft.zdk.app.neural.handlers;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppResourceHandler;
import nl.zeesoft.zdk.app.neural.resources.HotGymJs;
import nl.zeesoft.zdk.http.HttpResponse;

public class HotGymJsHandler extends NeuralAppResourceHandler {
	public static String	PATH	= "/hot-gym.js";
	
	public HotGymJsHandler(NeuralApp app) {
		super(app);
		path = PATH;
		body = (new HotGymJs()).render();
	}

	@Override
	protected void setContentType(HttpResponse response) {
		response.setContentTypeJavaScript();
	}
}
