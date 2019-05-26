package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.resource.JavaScriptZODBIndexManager;

public class JavaScriptZODBIndexManagerHandler extends HandlerObject {
	public JavaScriptZODBIndexManagerHandler(Config config,ModObject mod) {
		super(config,mod,"/indexManager.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZODBIndexManager js = new JavaScriptZODBIndexManager();
		return js.toStringBuilder();
	}
}
