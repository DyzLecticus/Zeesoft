package nl.zeesoft.zodb.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;

public class JsonNotFoundHandler extends JsonHandlerObject {
	public JsonNotFoundHandler(Config config, AppObject app) {
		super(config, app,"404.json");
		setAllowPost(true);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		return setResponse(response,404,"Not Found");
	}
}
