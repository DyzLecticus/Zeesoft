package nl.zeesoft.zspp.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonHandlerObject;
import nl.zeesoft.zspp.mod.ModZSPP;

public class JsonZSPPRequestHandler extends JsonHandlerObject {
	public final static String	PATH	= "/request.json"; 
	
	public JsonZSPPRequestHandler(Config config, ModObject mod) {
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
			ModObject mod = getConfiguration().getModule(ModZSPP.NAME);
			if (mod==null) {
				r = setResponse(response,405,ModZSPP.NAME + " module not found");
			} else {
				ModZSPP zssp = (ModZSPP) mod;
				/*
				 * TODO Implement
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
				*/
			}
		}
		return r;
	}
}
