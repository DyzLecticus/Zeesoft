package nl.zeesoft.zevt.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zevt.app.resource.JavaScriptZEVTEntityTranslator;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.handler.HandlerObject;

public class JavaScriptZEVTEntityTranslatorHandler extends HandlerObject {
	public JavaScriptZEVTEntityTranslatorHandler(Config config,AppObject app) {
		super(config,app,"/entityTranslator.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZEVTEntityTranslator js = new JavaScriptZEVTEntityTranslator();
		return js.toStringBuilder();
	}
}
