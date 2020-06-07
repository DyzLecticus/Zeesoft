package nl.zeesoft.zdk.http;

import java.net.HttpURLConnection;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunner;

public class ProxyRequestHandler extends HttpRequestHandler {
	protected ProxyRequestHandler(HttpServerConfig config) {
		super(config);
	}

	@Override
	protected void handleGetRequest(HttpRequest request, HttpResponse response) {
		//System.out.println("Request method: " + request.method + ", host: " + request.getHost() + ", path: " + request.path);
		//System.out.println(request.headers.toStr());
		String host = request.getHost();
		if (host!=null && host.length()>0) {
			String url = "";
			if (request.isSelfHost()) {
				Str error = new Str("Bad request: invalid Host header");
				setError(response, HttpURLConnection.HTTP_BAD_REQUEST, error);
			} else {
				if (request.path.startsWith("http://") || request.path.startsWith("https://") && !request.isSelfPath()) {
					url = request.path;
				} else {
					if (host.endsWith(":443")) {
						url = "https://" + host;
					} else {
						url = "http://" + host;
					}
				}
				if (!url.endsWith("/")) {
					url += "/";
				}
				//System.out.println("URL: " + url);
				if (request.method.equals("CONNECT")) {
					request.method = "GET";
				}
				HttpClient client = new HttpClient(config.getLogger(),request.method,url);
				client.getHeaders().addAll(request.headers);
				client.setReadTimeoutMs(getConfig().getReadTimeoutMs());
				CodeRunner runner = client.sendRequest();
				Str res = client.getResponseBody();
				//System.out.println("Response:" + res);
				if (client.getError().length()>0) {
					Str error = new Str(client.getError());
					setError(response, HttpURLConnection.HTTP_INTERNAL_ERROR, error);
				} else {
					response.code = client.getResponseCode();
					response.message = client.getResponseMessage();
					response.headers.addAll(client.getResponseHeaders());
					response.body = res;
				}
			}
		} else {
			Str error = new Str("Bad request: missing Host header");
			setError(response, HttpURLConnection.HTTP_BAD_REQUEST, error);
		}
	}
	
	protected ProxyServerConfig getConfig() {
		return (ProxyServerConfig) config;
	}
}
