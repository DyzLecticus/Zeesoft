package nl.zeesoft.zdk.app.neural.handlers;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppResourceHandler;
import nl.zeesoft.zdk.app.neural.resources.DemoTrainerHtml;
import nl.zeesoft.zdk.http.HttpResponse;

public class DemoTrainerHtmlHandler extends NeuralAppResourceHandler {
	public static String	PATH	= "/demo-trainer.html";
	
	public DemoTrainerHtmlHandler(NeuralApp app) {
		super(app);
		path = PATH;
		body = (new DemoTrainerHtml()).render();
	}

	@Override
	protected void setContentType(HttpResponse response) {
		response.setContentTypeHtml();
	}
}
