package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsds.AppConfiguration;
import nl.zeesoft.zsds.resource.BaseJavaScript;

public class BaseJavaScriptHandler extends HandlerObject {
	public BaseJavaScriptHandler(AppConfiguration config) {
		super(config,"/zsds.js");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
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
		BaseJavaScript js = new BaseJavaScript();
		return js.toStringBuilder();
	}
}
