package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;

public class JsonNotFoundHandler extends JsonHandlerObject {
	public JsonNotFoundHandler(Config config, ModObject mod) {
		super(config, mod,"/404.json");
		setAllowPost(true);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		return setResponse(response,404,"Not Found");
	}
}
