package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.resource.JavaScriptZODB;

public class JavaScriptZODBHandler extends HandlerObject {
	public JavaScriptZODBHandler(Config config,ModObject mod) {
		super(config,mod,"/zodb.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZODB js = new JavaScriptZODB();
		return js.toStringBuilder();
	}
}
