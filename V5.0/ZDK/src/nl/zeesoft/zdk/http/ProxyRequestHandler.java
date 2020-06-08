package nl.zeesoft.zdk.http;

import java.net.HttpURLConnection;

import nl.zeesoft.zdk.Str;

public class ProxyRequestHandler extends HttpRequestHandler {
	private HttpClient	client	= null;
	
	protected ProxyRequestHandler(HttpServerConfig config) {
		super(config);
		client = new HttpClient(config.getLogger());
		client.setReadTimeoutMs(getConfig().getReadTimeoutMs());
		runners.add(client.getRunner());
	}

	@Override
	protected void handleHeadRequest(HttpRequest request, HttpResponse response) {
		forwardRequestAndHandleResponse(request, response);
	}

	@Override
	protected void handleGetRequest(HttpRequest request, HttpResponse response) {
		forwardRequestAndHandleResponse(request, response);
	}

	@Override
	protected void handlePostRequest(HttpRequest request, HttpResponse response) {
		forwardRequestAndHandleResponse(request, response);
	}

	@Override
	protected void handleDeleteRequest(HttpRequest request, HttpResponse response) {
		forwardRequestAndHandleResponse(request, response);
	}
	
	protected void forwardRequestAndHandleResponse(HttpRequest request, HttpResponse response) {
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
					if (!url.endsWith("/")) {
						url += "/";
					}
					if (request.path.startsWith(url)) {
						url += request.getPath();
					}
				}
				if (url.startsWith("https://")) {
					Str error = new Str("HTTPS support is not impemented");
					setError(response, HttpURLConnection.HTTP_NOT_IMPLEMENTED, error);
				} else {
					HttpClient client = new HttpClient(config.getLogger(),request.method,url);
					client.setUrl(url);
					client.setMethod(request.method);
					client.setHeaders(request.headers.copy());
					client.sendRequest(request.body,request.keepProxyConnectionAlive(),false);
					if (client.getError().length()>0) {
						Str error = new Str(client.getError());
						setError(response, HttpURLConnection.HTTP_INTERNAL_ERROR, error);
					} else {
						response.code = client.getResponseCode();
						response.message = client.getResponseMessage();
						response.headers.addAll(client.getResponseHeaders());
						response.body = client.getResponseBody();
					}
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
