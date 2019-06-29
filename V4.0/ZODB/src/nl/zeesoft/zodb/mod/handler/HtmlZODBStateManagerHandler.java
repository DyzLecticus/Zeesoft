package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.resource.HtmlZODBStateManager;

public class HtmlZODBStateManagerHandler extends HandlerObject {
	public HtmlZODBStateManagerHandler(Config config,ModObject mod) {
		super(config,mod,"/stateManager.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZODBStateManager html = new HtmlZODBStateManager(getConfiguration());
		return html.toStringBuilder();
	}
}
