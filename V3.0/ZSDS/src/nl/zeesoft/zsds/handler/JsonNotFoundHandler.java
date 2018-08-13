package nl.zeesoft.zsds.handler;

import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonNotFoundHandler extends JsonBaseHandlerObject {
	public JsonNotFoundHandler(AppConfiguration config) {
		super(config,"/404.json");
		setUseGetCache(true);
	}

	@Override
	protected ZStringBuilder buildResponse() {
		return getErrorResponse("404","Resource not found");
	}

	@Override
	protected ZStringBuilder buildPostResponse(HttpServletResponse response,JsFile json) {
		return getCachedResponse();
	}
}
