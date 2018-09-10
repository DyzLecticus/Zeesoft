package nl.zeesoft.zevt.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zevt.app.AppZEVT;
import nl.zeesoft.zevt.trans.TranslatorRequestResponse;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.handler.JsonHandlerObject;

public class JsonZEVTRequestHandler extends JsonHandlerObject {
	public final static String	PATH	= "/request.json"; 
	
	public JsonZEVTRequestHandler(Config config, AppObject app) {
		super(config,app,PATH);
		setAllowGet(false);
		setAllowPost(true);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		ZStringBuilder r = new ZStringBuilder();
		JsFile json = getPostBodyJson(request, response);
		if (json.rootElement==null) {
			r = setResponse(response,400,"Failed to parse JSON");
		} else {
			AppObject app = getConfiguration().getApplication(AppZEVT.NAME);
			if (app==null) {
				r = setResponse(response,405,"ZEVT application not found");
			} else {
				AppZEVT zevt = (AppZEVT) app;
				if (!zevt.getTranslator().isInitialized()) {
					r = setResponse(response,503,"Entity value translation is not available right now. Please try again later.");
				} else {
					TranslatorRequestResponse req = new TranslatorRequestResponse();
					req.fromJson(json);
					zevt.handleRequest(req);
					json = req.toJson();
					if (getConfiguration().isDebug()) {
						r = json.toStringBuilderReadFormat();
					} else {
						r = json.toStringBuilder();
					}
				}
			}
		}
		return r;
	}
}
