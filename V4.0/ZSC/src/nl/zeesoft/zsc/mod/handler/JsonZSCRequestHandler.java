package nl.zeesoft.zsc.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonHandlerObject;
import nl.zeesoft.zsc.confab.ConfabulatorRequest;
import nl.zeesoft.zsc.confab.ConfabulatorResponse;
import nl.zeesoft.zsc.mod.ModZSC;

public class JsonZSCRequestHandler extends JsonHandlerObject {
	public final static String	PATH	= "/request.json"; 
	
	public JsonZSCRequestHandler(Config config, ModObject mod) {
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
			ModObject mod = getConfiguration().getModule(ModZSC.NAME);
			if (mod==null) {
				r = setResponse(response,405,ModZSC.NAME + " module not found");
			} else {
				ModZSC zsc = (ModZSC) mod;
				if (!zsc.getConfabulatorSetLoader().isInitialized() || !zsc.getConfabulatorManager().isInitialized()) {
					r = setResponse(response,503,"Confabulator set is not initialized. Please try again later.");
				} else {
					ConfabulatorRequest req = new ConfabulatorRequest();
					req.fromJson(json);
					ConfabulatorResponse res = zsc.handleRequest(req);
					if (res.log.length()>0) {
						res.log.replace("\n","<NEWLINE>");
						res.log.replace("\"","<QUOTE>");
					}
					json = res.toJson();
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