package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.resource.JavaScriptZODBDataManager;

public class JavaScriptZODBDataManagerHandler extends HandlerObject {
	public JavaScriptZODBDataManagerHandler(Config config,ModObject mod) {
		super(config,mod,"/dataManager.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZODBDataManager js = new JavaScriptZODBDataManager();
		return js.toStringBuilder();
	}
}
