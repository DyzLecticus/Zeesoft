package nl.zeesoft.zdk.http;

import java.net.HttpURLConnection;

import nl.zeesoft.zdk.Str;

public class ProxyRequestHandler extends HttpRequestHandler {
	protected ProxyRequestHandler(HttpServerConfig config) {
		super(config);
	}

	@Override
	protected void handleGetRequest(HttpRequest request, HttpResponse response) {
		String host = request.getHost();
		if (host!=null && host.length()>0) {
			if (request.isSelfHost()) {
				Str error = new Str("Bad request: invalid Host header");
				setError(response, HttpURLConnection.HTTP_BAD_REQUEST, error);
			} else {
				host = "http://" + host;
				HttpClient client = new HttpClient(config.getLogger(),request.method,host);
				client.setConnectTimeoutMs(getConfig().getConnectTimeoutMs());
				client.setReadTimeoutMs(getConfig().getReadTimeoutMs());
				Str res = client.sendRequest(request.getHeaders());
				if (client.getError().length()>0) {
					Str error = new Str(client.getError());
					setError(response, HttpURLConnection.HTTP_INTERNAL_ERROR, error);
				} else {
					response.code = client.getResponseCode();
					response.message = client.getResponseMessage();
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
