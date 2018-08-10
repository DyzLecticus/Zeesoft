package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsds.AppConfiguration;
import nl.zeesoft.zsds.resource.SelfTestHtml;

public class HtmlSelfTestHandler extends HandlerObject {
	public HtmlSelfTestHandler(AppConfiguration config) {
		super(config,"/selfTest.html");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeadersAndStatus(response);
		response.setContentType("text/html");
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
		SelfTestHtml html = new SelfTestHtml();
		return html.toStringBuilder();
	}
}
