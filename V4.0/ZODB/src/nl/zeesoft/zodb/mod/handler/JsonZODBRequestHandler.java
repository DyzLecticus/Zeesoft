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

public class JsonZODBRequestHandler extends JsonZODBHandlerObject {
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
			ModZODB zodb = getZODB(request,response);
			if (zodb!=null) {
				DatabaseRequest req = new DatabaseRequest();
				req.fromJson(json);
				DatabaseResponse res = zodb.handleRequest(req);
				response.setStatus(res.statusCode);
				r = stringifyJson(res.toJson());
			}
		}
		return r;
	}
}
