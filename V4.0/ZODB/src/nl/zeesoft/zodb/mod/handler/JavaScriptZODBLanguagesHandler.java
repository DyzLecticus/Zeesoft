package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.resource.JavaScriptZODBLanguages;

public class JavaScriptZODBLanguagesHandler extends HandlerObject {
	public JavaScriptZODBLanguagesHandler(Config config,ModObject mod) {
		super(config,mod,"/languages.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZODBLanguages js = new JavaScriptZODBLanguages();
		return js.toStringBuilder();
	}
}
