package nl.zeesoft.zspp.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonHandlerObject;
import nl.zeesoft.zspp.mod.ModZSPP;
import nl.zeesoft.zspp.prepro.PreprocessorRequestResponse;

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
				if (!zssp.getPreprocessor().isInitialized()) {
					r = setResponse(response,503,"Preprocessing is not available right now. Please try again later.");
				} else {
					PreprocessorRequestResponse req = new PreprocessorRequestResponse();
					req.fromJson(json);
					zssp.handleRequest(req);
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
