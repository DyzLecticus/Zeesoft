package nl.zeesoft.zsds.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsds.AppConfiguration;

public abstract class HandlerObject {
	private	AppConfiguration	configuration	= null;
	private String				path			= "";
	private ZStringBuilder		cachedResponse	= null;
	
	public HandlerObject(AppConfiguration config,String path) {
		this.configuration = config;
		this.path = path;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		// Override to implement
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// Override to implement
	}
	
	protected void setDefaultHeadersAndStatus(HttpServletResponse response) {
		response.setHeader("Last-Modified",getConfiguration().getLastModifiedHeader());
		response.setStatus(200);
	}
	
	protected abstract ZStringBuilder buildResponse();
	
	protected ZStringBuilder getCachedResponse() {
		if (cachedResponse==null) {
			cachedResponse = buildResponse();
		}
		return cachedResponse;
	}

	protected AppConfiguration getConfiguration() {
		return configuration;
	}

	public String getPath() {
		return path;
	}
}
