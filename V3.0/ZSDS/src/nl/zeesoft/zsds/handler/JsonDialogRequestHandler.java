package nl.zeesoft.zsds.handler;

import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.DialogHandler;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonDialogRequestHandler extends JsonBaseHandlerObject {
	public static final String	PATH	= "/dialogRequestHandler.json";
	
	public JsonDialogRequestHandler(AppConfiguration config) {
		super(config,PATH);
		setAllowGet(false);
	}
	
	@Override
	protected ZStringBuilder buildPostResponse(HttpServletResponse response,JsFile json) {
		DialogRequest req = new DialogRequest();
		req.fromJson(json);
		DialogHandler handler = new DialogHandler(getConfiguration().getDialogHandlerConfig());
		DialogResponse res = handler.handleDialogRequest(req);
		res.debugLog.replace("\n","<NEWLINE>");
		res.debugLog.replace("\\\"","<QUOTE>");
		ZStringBuilder r = null;
		if (getConfiguration().isDebug()) {
			r = res.toJson().toStringBuilderReadFormat();
		} else {
			r = res.toJson().toStringBuilder();
		}
		return r;
	}
}
