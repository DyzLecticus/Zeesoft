package nl.zeesoft.zsda.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zsda.mod.resource.JavaScriptZSDAGridMonitor;

public class JavaScriptZSDAGridMonitorHandler extends HandlerObject {
	public JavaScriptZSDAGridMonitorHandler(Config config,ModObject mod) {
		super(config,mod,"/gridMonitor.js");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		JavaScriptZSDAGridMonitor js = new JavaScriptZSDAGridMonitor();
		return js.toStringBuilder();
	}
}
