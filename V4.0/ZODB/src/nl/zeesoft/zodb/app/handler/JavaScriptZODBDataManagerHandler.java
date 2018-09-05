package nl.zeesoft.zodb.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.resource.JavaScriptZODBDataManager;

public class JavaScriptZODBDataManagerHandler extends HandlerObject {
	public JavaScriptZODBDataManagerHandler(Config config,AppObject app) {
		super(config,app,"/dataManager.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZODBDataManager js = new JavaScriptZODBDataManager();
		return js.toStringBuilder();
	}
}
