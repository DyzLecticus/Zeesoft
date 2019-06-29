package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.ModZODB;

public abstract class JsonZODBHandlerObject extends JsonHandlerObject {
	public JsonZODBHandlerObject(Config config, ModObject mod,String path) {
		super(config,mod,path);
	}

	protected ModZODB getZODB(HttpServletRequest request,HttpServletResponse response) {
		ModZODB r = null;
		r = getConfiguration().getZODB();
		if (r==null) {
			setResponse(response,405,"ZODB module not found");
		} else {
			if (!r.getWhiteList().isAllowed(request.getRemoteAddr())) {
				setResponse(response,403,"ZODB module does not allow requests from " + request.getRemoteAddr());
				r = null;
			}
		}
		return r;
	}
}
