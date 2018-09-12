package nl.zeesoft.zspp.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zspp.mod.resource.HtmlZSPPIndex;

public class HtmlZSPPIndexHandler extends HandlerObject {
	public HtmlZSPPIndexHandler(Config config,ModObject mod) {
		super(config,mod,"/index.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZSPPIndex html = new HtmlZSPPIndex(getConfiguration());
		return html.toStringBuilder();
	}
}
