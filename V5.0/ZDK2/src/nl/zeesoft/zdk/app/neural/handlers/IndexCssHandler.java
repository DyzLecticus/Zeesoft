package nl.zeesoft.zdk.app.neural.handlers;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppResourceHandler;
import nl.zeesoft.zdk.app.neural.resources.IndexCss;
import nl.zeesoft.zdk.http.HttpResponse;

public class IndexCssHandler extends NeuralAppResourceHandler {
	public static String	PATH	= "/index.css";
	
	public IndexCssHandler(NeuralApp app) {
		super(app);
		path = PATH;
		body = (new IndexCss()).render();
	}

	@Override
	protected void setContentType(HttpResponse response) {
		response.setContentTypeCss();
	}
}
