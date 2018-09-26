package nl.zeesoft.zsc.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zsc.mod.resource.HtmlZSCState;

public class HtmlZSCStateHandler extends HandlerObject {
	public HtmlZSCStateHandler(Config config,ModObject mod) {
		super(config,mod,"/stateManager.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZSCState html = new HtmlZSCState(getConfiguration());
		return html.toStringBuilder();
	}
}
