package nl.zeesoft.zals.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zals.mod.resource.HtmlZALSIndex;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;

public class HtmlZALSIndexHandler extends HandlerObject {
	public HtmlZALSIndexHandler(Config config,ModObject mod) {
		super(config,mod,"/index.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZALSIndex html = new HtmlZALSIndex(getConfiguration());
		return html.toStringBuilder();
	}
}
