package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.ModZODB;

public class JsonZODBStateHandler extends JsonZODBHandlerObject {
	public final static String	PATH	= "/state.json"; 
	
	public JsonZODBStateHandler(Config config, ModObject mod) {
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
				boolean restart = json.rootElement.getChildBoolean("restart",false);
				if (restart) {
					boolean done = false;
					done = zodb.getDatabase().stop();
					if (!done) {
						r = setResponse(response,503,"Failed to stop the database");
					} else {
						done = zodb.getDatabase().start();
						if (!done) {
							r = setResponse(response,503,"Failed to start the database");
						} else {
							r = setResponse(response,200,"Restarted database");
						}
					}
				}
				JsFile res = new JsFile();
				res.rootElement = new JsElem();
				res.rootElement.children.add(new JsElem("state",zodb.getDatabase().getState(),true));
				r = stringifyJson(res);
			}
		}
		return r;
	}
}
