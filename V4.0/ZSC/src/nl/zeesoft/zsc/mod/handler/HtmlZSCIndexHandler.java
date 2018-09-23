package nl.zeesoft.zsc.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zsc.mod.resource.HtmlZSCIndex;

public class HtmlZSCIndexHandler extends HandlerObject {
	public HtmlZSCIndexHandler(Config config,ModObject mod) {
		super(config,mod,"/index.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZSCIndex html = new HtmlZSCIndex(getConfiguration());
		return html.toStringBuilder();
	}
}
