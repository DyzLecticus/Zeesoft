package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;

public class HtmlForbiddenHandler extends HandlerObject {
	public HtmlForbiddenHandler(Config config, ModObject mod) {
		super(config,mod,"/403.html");
		setAllowPost(true);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		return setResponse(response,403,"Forbidden");
	}
}
