package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;

public class JsonForbiddenHandler extends JsonHandlerObject {
	public JsonForbiddenHandler(Config config, ModObject mod) {
		super(config, mod,"/403.json");
		setAllowPost(true);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		return setResponse(response,403,"Forbidden");
	}
}
