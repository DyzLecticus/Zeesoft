package nl.zeesoft.zbe.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zbe.mod.resource.HtmlZBEIndex;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;

public class HtmlZBEIndexHandler extends HandlerObject {
	public HtmlZBEIndexHandler(Config config,ModObject mod) {
		super(config,mod,"/index.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZBEIndex html = new HtmlZBEIndex(getConfiguration());
		return html.toStringBuilder();
	}
}
