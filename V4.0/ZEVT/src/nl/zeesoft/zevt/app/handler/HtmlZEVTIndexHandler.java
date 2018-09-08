package nl.zeesoft.zevt.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zevt.app.resource.HtmlZEVTIndex;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.handler.HandlerObject;

public class HtmlZEVTIndexHandler extends HandlerObject {
	public HtmlZEVTIndexHandler(Config config,AppObject app) {
		super(config,app,"/index.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZEVTIndex html = new HtmlZEVTIndex(getConfiguration());
		return html.toStringBuilder();
	}
}
