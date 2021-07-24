package nl.zeesoft.zdk.app.neural.handlers;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppResourceHandler;
import nl.zeesoft.zdk.app.neural.resources.HotGymHtml;
import nl.zeesoft.zdk.http.HttpResponse;

public class HotGymHtmlHandler extends NeuralAppResourceHandler {
	public static String	PATH	= "/hot-gym.html";
	
	public HotGymHtmlHandler(NeuralApp app) {
		super(app);
		path = PATH;
		body = (new HotGymHtml()).render();
	}

	@Override
	protected void setContentType(HttpResponse response) {
		response.setContentTypeHtml();
	}
}
