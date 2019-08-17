package nl.zeesoft.zenn.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zenn.mod.resource.HtmlZENNEnvironment;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;

public class HtmlZENNEnvironmentHandler extends HandlerObject {
	public HtmlZENNEnvironmentHandler(Config config,ModObject mod) {
		super(config,mod,"/environment.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZENNEnvironment html = new HtmlZENNEnvironment(getConfiguration());
		return html.toStringBuilder();
	}
}
