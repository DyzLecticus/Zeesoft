package nl.zeesoft.zsda.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zsda.mod.resource.HtmlZSDAIndex;

public class HtmlZSDAIndexHandler extends HandlerObject {
	public HtmlZSDAIndexHandler(Config config,ModObject mod) {
		super(config,mod,"/index.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZSDAIndex html = new HtmlZSDAIndex(getConfiguration());
		return html.toStringBuilder();
	}
}
