package nl.zeesoft.zsda.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zsda.mod.resource.HtmlZSDAGridConfig;

public class HtmlZSDAGridConfigHandler extends HandlerObject {
	public HtmlZSDAGridConfigHandler(Config config,ModObject mod) {
		super(config,mod,"/gridConfig.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZSDAGridConfig html = new HtmlZSDAGridConfig(getConfiguration());
		return html.toStringBuilder();
	}
}
