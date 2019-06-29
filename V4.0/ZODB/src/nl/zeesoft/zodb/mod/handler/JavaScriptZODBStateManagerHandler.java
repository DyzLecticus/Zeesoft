package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.resource.JavaScriptZODBStateManager;

public class JavaScriptZODBStateManagerHandler extends HandlerObject {
	public JavaScriptZODBStateManagerHandler(Config config,ModObject mod) {
		super(config,mod,"/stateManager.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZODBStateManager js = new JavaScriptZODBStateManager();
		return js.toStringBuilder();
	}
}
