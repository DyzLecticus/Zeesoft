package nl.zeesoft.zevt.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zevt.mod.ModZEVT;
import nl.zeesoft.zevt.trans.TranslatorRequestResponse;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonHandlerObject;

public class JsonZEVTRequestHandler extends JsonHandlerObject {
	public final static String	PATH	= "/request.json"; 
	
	public JsonZEVTRequestHandler(Config config, ModObject mod) {
		super(config,mod,PATH);
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
			ModObject mod = getConfiguration().getModule(ModZEVT.NAME);
			if (mod==null) {
				r = setResponse(response,405,ModZEVT.NAME + " module not found");
			} else {
				ModZEVT zevt = (ModZEVT) mod;
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
