package nl.zeesoft.znlb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.znlb.mod.resource.JavaScriptZNLBLanguages;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;

public class JavaScriptZNLBLanguagesHandler extends HandlerObject {
	public JavaScriptZNLBLanguagesHandler(Config config,ModObject mod) {
		super(config,mod,"/languages.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZNLBLanguages js = new JavaScriptZNLBLanguages();
		return js.toStringBuilder();
	}
}
