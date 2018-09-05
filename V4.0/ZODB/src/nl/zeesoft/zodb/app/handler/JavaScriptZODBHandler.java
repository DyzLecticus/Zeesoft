package nl.zeesoft.zodb.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.resource.JavaScriptZODB;

public class JavaScriptZODBHandler extends HandlerObject {
	public JavaScriptZODBHandler(Config config,AppObject app) {
		super(config,app,"/zodb.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZODB js = new JavaScriptZODB();
		return js.toStringBuilder();
	}
}
