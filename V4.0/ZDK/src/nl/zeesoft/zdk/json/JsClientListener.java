package nl.zeesoft.zdk.json;

import nl.zeesoft.zdk.ZStringBuilder;

public interface JsClientListener {
	public void handledRequest(JsClientRequest request,JsFile response, ZStringBuilder err, Exception ex);
}
