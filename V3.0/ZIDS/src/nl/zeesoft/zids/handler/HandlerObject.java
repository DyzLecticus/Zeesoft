package nl.zeesoft.zids.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;

public abstract class HandlerObject {
	private	Messenger		messenger		= null;
	private String			path			= "";
	private ZStringBuilder	cachedResponse	= null;
	
	public HandlerObject(Messenger msgr,String p) {
		this.messenger = msgr;
		this.path = p;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		// Override to implement
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// Override to implement
	}
	
	protected abstract ZStringBuilder buildResponse();
	
	protected ZStringBuilder getCachedResponse() {
		if (cachedResponse==null) {
			cachedResponse = buildResponse();
		}
		return cachedResponse;
	}

	public Messenger getMessenger() {
		return messenger;
	}

	public String getPath() {
		return path;
	}

	protected void setCachedResponse(ZStringBuilder cachedResponse) {
		this.cachedResponse = cachedResponse;
	}
}
