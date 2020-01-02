package nl.zeesoft.zsda.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zsda.mod.resource.HtmlZSDAGridMonitor;

public class HtmlZSDAGridMonitorHandler extends HandlerObject {
	public HtmlZSDAGridMonitorHandler(Config config,ModObject mod) {
		super(config,mod,"/gridMonitor.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZSDAGridMonitor html = new HtmlZSDAGridMonitor(getConfiguration());
		return html.toStringBuilder();
	}
}
