package nl.zeesoft.zdbd.api;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdbd.ThemeController;
import nl.zeesoft.zdbd.api.html.Index;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpRequestHandler;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.http.HttpServerConfig;

public class RequestHandler extends HttpRequestHandler {
	protected SortedMap<String,Str>	pathResponses = new TreeMap<String,Str>();
	
	protected RequestHandler(HttpServerConfig config, ThemeController controller) {
		super(config);
		pathResponses.put("/", (new Index()).render());
		pathResponses.put("/index.html", (new Index()).render());
	}

	@Override
	protected void handleGetRequest(HttpRequest request, HttpResponse response) {
		Str body = pathResponses.get(request.path);
		if (body!=null) {
			response.code = 200;
			response.body = body;
		} else {
			super.handleGetRequest(request, response);
		}
	}
}
