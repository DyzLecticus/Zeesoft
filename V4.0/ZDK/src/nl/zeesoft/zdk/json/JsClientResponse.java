package nl.zeesoft.zdk.json;

import nl.zeesoft.zdk.ZStringBuilder;

public class JsClientResponse {
	public JsClientRequest	request 	= null;
	public JsFile			response	= null;
	public ZStringBuilder	error		= new ZStringBuilder();
	public Exception		ex			= null;
}
