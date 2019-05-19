package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.ModZODB;

public class JsonZODBRequestHandler extends JsonHandlerObject {
	public final static String	PATH	= "/request.json"; 
	
	public JsonZODBRequestHandler(Config config, ModObject mod) {
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
			ModZODB zodb = getConfiguration().getZODB();
			if (zodb==null) {
				r = setResponse(response,405,"ZODB module not found");
			} else {
				if (!zodb.getWhiteList().isAllowed(request.getRemoteAddr())) {
					r = setResponse(response,403,"ZODB module does not allow requests from " + request.getRemoteAddr());
				} else {
					DatabaseRequest req = new DatabaseRequest();
					req.fromJson(json);
					DatabaseResponse res = zodb.handleRequest(req);
					response.setStatus(res.statusCode);
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
