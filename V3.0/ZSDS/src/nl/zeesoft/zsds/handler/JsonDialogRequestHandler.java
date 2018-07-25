package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.DialogHandler;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonDialogRequestHandler extends JsonBaseHandlerObject {
	public JsonDialogRequestHandler(AppConfiguration config) {
		super(config,"/dialogRequestHandler.json");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(setErrorResponse(response,405,"GET is not supported for this resource"));
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}
	
	@Override
	protected ZStringBuilder buildPostResponse(JsFile json) {
		DialogRequest request = new DialogRequest();
		request.fromJson(json);
		DialogHandler handler = new DialogHandler(getConfiguration().getDialogHandlerConfig());
		DialogResponse response = handler.handleDialogRequest(request);
		response.debugLog.replace("\n","<NEWLINE>");
		response.debugLog.replace("\\\"","<QUOTE>");
		ZStringBuilder r = null;
		if (getConfiguration().isDebug()) {
			r = response.toJson().toStringBuilderReadFormat();
		} else {
			r = response.toJson().toStringBuilder();
		}
		return r;
	}
}
