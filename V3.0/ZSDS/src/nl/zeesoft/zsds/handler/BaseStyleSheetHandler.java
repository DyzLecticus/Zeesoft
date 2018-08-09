package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsds.AppConfiguration;
import nl.zeesoft.zsds.resource.BaseStyleSheet;

public class BaseStyleSheetHandler extends HandlerObject {
	public BaseStyleSheetHandler(AppConfiguration config) {
		super(config,"/zsds.css");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeadersAndStatus(response);
		response.setContentType("text/css");
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
		BaseStyleSheet css = new BaseStyleSheet();
		return css.toStringBuilder();
	}
}
