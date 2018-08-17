package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsds.AppConfiguration;
import nl.zeesoft.zsds.resource.EnvironmentJavaScript;

public class EnvironmentJavaScriptHandler extends HandlerObject {
	public EnvironmentJavaScriptHandler(AppConfiguration config) {
		super(config,"/environment.js");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeadersAndStatus(response);
		response.setContentType("text/javascript");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(getCachedResponse());
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}

	@Override
	protected ZStringBuilder buildResponse() {
		EnvironmentJavaScript js = new EnvironmentJavaScript();
		return js.toStringBuilder();
	}
}
