package nl.zeesoft.zevt.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zevt.mod.resource.JavaScriptZEVTEntityTranslator;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;

public class JavaScriptZEVTEntityTranslatorHandler extends HandlerObject {
	public JavaScriptZEVTEntityTranslatorHandler(Config config,ModObject mod) {
		super(config,mod,"/entityTranslator.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZEVTEntityTranslator js = new JavaScriptZEVTEntityTranslator();
		return js.toStringBuilder();
	}
}
