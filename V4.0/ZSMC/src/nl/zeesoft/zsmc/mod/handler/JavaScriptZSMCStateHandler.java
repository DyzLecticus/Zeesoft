package nl.zeesoft.zsmc.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zsmc.mod.resource.JavaScriptZSMCState;

public class JavaScriptZSMCStateHandler extends HandlerObject {
	public JavaScriptZSMCStateHandler(Config config,ModObject mod) {
		super(config,mod,"/stateManager.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZSMCState js = new JavaScriptZSMCState();
		return js.toStringBuilder();
	}
}
