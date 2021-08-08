package nl.zeesoft.zdk.app.neural.handlers.api;

import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.SdrPanel;
import nl.zeesoft.zdk.neural.SdrStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class SdrPngHandler extends NeuralAppContextHandler {
	public static String	PATH	= "/sdr/image.png";
	
	public SdrPngHandler(NeuralApp app) {
		super(app);
		path = PATH;
		allowedMethods.add(HttpRequest.HEAD);
		allowedMethods.add(HttpRequest.GET);
	}
	
	public void handleRequest(HttpRequest request, HttpResponse response) {
		response.setContentType("image/png");
		if (request.method.equals(HttpRequest.GET)) {
			Sdr sdr = parseSdrFromQueryString(request);
			if (sdr!=null) {
				SdrPanel panel = new SdrPanel(sdr);
				panel.render();
				response.body = panel.getByteArray(false);
			} else {
				response.setBadRequest();
			}
		}
	}
	
	protected Sdr parseSdrFromQueryString(HttpRequest request) {
		StringBuilder str = new StringBuilder(request.query);
		if (StrUtil.startsWith(str, "sdr=")) {
			str = StrUtil.substring(str, 4, str.length());
		}
		SdrStringConvertor convertor = (SdrStringConvertor) ObjectStringConvertors.getConvertor(Sdr.class);
		return convertor.fromStringBuilder(str);
	}
}
