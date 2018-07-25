package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonTestDialogRequestHandler extends HandlerObject {
	public JsonTestDialogRequestHandler(AppConfiguration config) {
		super(config,"/testDialogRequest.json");
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
		DialogRequest request = new DialogRequest();
		request.setAllActions(true);
		request.appendDebugLog = getConfiguration().isDebug();
		return request.toJson().toStringBuilderReadFormat();
	}
}
