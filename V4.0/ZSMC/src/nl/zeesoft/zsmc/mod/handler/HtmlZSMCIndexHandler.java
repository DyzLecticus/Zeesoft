package nl.zeesoft.zsmc.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zsmc.mod.resource.HtmlZSMCIndex;

public class HtmlZSMCIndexHandler extends HandlerObject {
	public HtmlZSMCIndexHandler(Config config,ModObject mod) {
		super(config,mod,"/index.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZSMCIndex html = new HtmlZSMCIndex(getConfiguration());
		return html.toStringBuilder();
	}
}
