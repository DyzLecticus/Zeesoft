package nl.zeesoft.zevt.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zevt.mod.resource.HtmlZEVTEntityTranslator;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;

public class HtmlZEVTEntityTranslatorHandler extends HandlerObject {
	public HtmlZEVTEntityTranslatorHandler(Config config,ModObject mod) {
		super(config,mod,"/entityTranslator.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZEVTEntityTranslator html = new HtmlZEVTEntityTranslator(getConfiguration());
		return html.toStringBuilder();
	}
}
