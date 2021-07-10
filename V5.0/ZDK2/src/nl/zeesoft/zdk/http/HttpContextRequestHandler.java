package nl.zeesoft.zdk.http;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.str.StrUtil;

public class HttpContextRequestHandler extends HttpRequestHandler {
	public List<HttpContextHandler>		handlers		= new ArrayList<HttpContextHandler>();
	public String						indexFileName	= "index.html";
	
	@Override
	public void handleRequest(HttpRequest request, HttpResponse response) {
		HttpContextHandler handler = getHandler(request.path);
		if (handler!=null) {
			if (handler.allowedMethods.contains(request.method)) {
				handler.handleRequest(request, response);
			} else {
				response.setNotAllowed(handler.allowedMethods);
			}
		} else {
			response.setNotFound();
		}
	}
	
	public StringBuilder getPathHandlersStringBuilder() {
		StringBuilder r = new StringBuilder();
		for (HttpContextHandler contextHandler: handlers) {
			StringBuilder line = new StringBuilder();
			for (String method: contextHandler.allowedMethods) {
				StrUtil.append(line, method, ", ");
			}
			line.append(")");
			line.insert(0, " (");
			line.insert(0, contextHandler.path);
			line.insert(0, "- ");
			StrUtil.appendLine(r, line);
		}
		return r;
	}
	
	public void put(HttpContextHandler handler) {
		remove(handler.path);
		handlers.add(handler);
	}
	
	public HttpContextHandler get(String path) {
		HttpContextHandler r = null;
		for (HttpContextHandler handler: handlers) {
			if (handler.path.equals(path)) {
				r = handler;
				break;
			}
		}
		return r;
	}
	
	public HttpContextHandler remove(String path) {
		HttpContextHandler r = get(path);
		if (r!=null) {
			handlers.remove(r);
		}
		return r;
	}
	
	protected HttpContextHandler getHandler(String path) {
		HttpContextHandler r = get(path);
		if (r==null) {
			if (path.endsWith("/")) {
				r = get(path + indexFileName);
			} else if (path.endsWith(indexFileName)) {
				r = get(path.substring(0, path.length() - indexFileName.length()));
			}
		}
		return r;
	}
}
