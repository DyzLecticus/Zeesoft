package nl.zeesoft.zids.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.messenger.Messenger;

public abstract class HandlerObject {
	private	Messenger	messenger	= null;
	private String		path		= "";
	
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

	public Messenger getMessenger() {
		return messenger;
	}

	public String getPath() {
		return path;
	}
}
