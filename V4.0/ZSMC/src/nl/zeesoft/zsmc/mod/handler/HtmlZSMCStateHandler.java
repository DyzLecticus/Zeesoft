package nl.zeesoft.zsmc.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zsmc.mod.resource.HtmlZSMCState;

public class HtmlZSMCStateHandler extends HandlerObject {
	public HtmlZSMCStateHandler(Config config,ModObject mod) {
		super(config,mod,"/stateManager.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZSMCState html = new HtmlZSMCState(getConfiguration());
		return html.toStringBuilder();
	}
}
