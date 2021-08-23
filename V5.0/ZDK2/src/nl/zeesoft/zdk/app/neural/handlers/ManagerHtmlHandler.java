package nl.zeesoft.zdk.app.neural.handlers;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppResourceHandler;
import nl.zeesoft.zdk.app.neural.resources.ManagerHtml;
import nl.zeesoft.zdk.http.HttpResponse;

public class ManagerHtmlHandler extends NeuralAppResourceHandler {
	public static String	PATH	= "/manager.html";
	
	public ManagerHtmlHandler(NeuralApp app) {
		super(app);
		path = PATH;
		body = (new ManagerHtml()).render();
	}

	@Override
	protected void setContentType(HttpResponse response) {
		response.setContentTypeHtml();
	}
}
