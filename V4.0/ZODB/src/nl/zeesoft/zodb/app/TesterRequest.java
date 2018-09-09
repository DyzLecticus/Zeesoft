package nl.zeesoft.zodb.app;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;

public class TesterRequest {
	public JsFile			request				= null;
	public JsFile			expectedResponse	= null;
	
	public JsFile			response			= null;
	public ZStringBuilder	error				= new ZStringBuilder();
	public long				time				= 0;
}
