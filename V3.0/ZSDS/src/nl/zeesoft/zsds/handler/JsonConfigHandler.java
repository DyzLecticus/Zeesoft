package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonConfigHandler extends HandlerObject {
	public JsonConfigHandler(AppConfiguration config) {
		super(config,"/config.json");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
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
		return getConfiguration().getBaseConfig().toJson().toStringBuilderReadFormat();
	}
}