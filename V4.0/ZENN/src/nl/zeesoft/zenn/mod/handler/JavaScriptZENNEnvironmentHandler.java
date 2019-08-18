package nl.zeesoft.zenn.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zenn.mod.resource.JavaScriptZENNEnvironment;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;

public class JavaScriptZENNEnvironmentHandler extends HandlerObject {
	public JavaScriptZENNEnvironmentHandler(Config config,ModObject mod) {
		super(config,mod,"/state.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZENNEnvironment js = new JavaScriptZENNEnvironment();
		return js.toStringBuilder();
	}
}
