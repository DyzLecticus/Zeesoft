package nl.zeesoft.zdk.app.neural.handlers;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppResourceHandler;
import nl.zeesoft.zdk.app.neural.resources.IndexHtml;
import nl.zeesoft.zdk.http.HttpResponse;

public class IndexHtmlHandler extends NeuralAppResourceHandler {
	public static String	PATH	= "/index.html";
	
	public IndexHtmlHandler(NeuralApp app) {
		super(app);
		path = PATH;
		body = (new IndexHtml(app.getPort())).render();
	}

	@Override
	protected void setContentType(HttpResponse response) {
		response.setContentTypeHtml();
	}
}
