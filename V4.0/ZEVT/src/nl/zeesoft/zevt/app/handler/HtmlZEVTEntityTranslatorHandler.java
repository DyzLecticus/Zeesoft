package nl.zeesoft.zevt.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zevt.app.resource.HtmlZEVTEntityTranslator;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.handler.HandlerObject;

public class HtmlZEVTEntityTranslatorHandler extends HandlerObject {
	public HtmlZEVTEntityTranslatorHandler(Config config,AppObject app) {
		super(config,app,"/entityTranslator.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZEVTEntityTranslator html = new HtmlZEVTEntityTranslator(getConfiguration());
		return html.toStringBuilder();
	}
}
