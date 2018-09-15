package nl.zeesoft.zsdm.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zsdm.mod.resource.HtmlZSDMIndex;

public class HtmlZSDMIndexHandler extends HandlerObject {
	public HtmlZSDMIndexHandler(Config config,ModObject mod) {
		super(config,mod,"/index.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZSDMIndex html = new HtmlZSDMIndex(getConfiguration());
		return html.toStringBuilder();
	}
}
