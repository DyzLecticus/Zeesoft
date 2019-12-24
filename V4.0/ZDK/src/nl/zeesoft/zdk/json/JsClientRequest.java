package nl.zeesoft.zdk.json;

public class JsClientRequest {
	public JsFile	request 		= null;
	public String	url				= null;
	public int		timeoutSeconds	= 3;
	
	public int		retries			= 0;
	
	public JsClientRequest(JsFile request, String url) {
		this.request = request;
		this.url = url;
	}
}
