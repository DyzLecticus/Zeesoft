package nl.zeesoft.zodb.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.AppZODB;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;

public class JsonZODBRequestHandler extends JsonHandlerObject {
	public final static String	PATH	= "/request.json"; 
	
	public JsonZODBRequestHandler(Config config, AppObject app) {
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
			AppZODB zodb = getConfiguration().getZODB();
			if (zodb==null) {
				r = setResponse(response,405,"ZODB application not found");
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
		return r;
	}
}
