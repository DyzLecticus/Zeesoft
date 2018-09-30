package nl.zeesoft.zevt.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zevt.mod.ModZEVT;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonHandlerObject;

public class JsonZEVTEntitiesHandler extends JsonHandlerObject {
	public final static String	PATH	= "/entities.json";
	
	public JsonZEVTEntitiesHandler(Config config, ModObject mod) {
		super(config,mod,PATH);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		ZStringBuilder r = new ZStringBuilder();
		ModObject mod = getConfiguration().getModule(ModZEVT.NAME);
		if (mod==null) {
			r = setResponse(response,405,ModZEVT.NAME + " module not found");
		} else {
			ModZEVT zevt = (ModZEVT) mod;
			if (!zevt.getTranslator().isInitialized()) {
				r = setResponse(response,503,"Entity value translator has not been initialized yet. Please try again later.");
			} else {
				ZStringBuilder str = zevt.getTranslator().getEntitiesJson();
				if (str==null) {
					r = setResponse(response,503,"Entities JSON has not been generated yet. Please try again later.");
				} else {
					r = str;
				}
			}
		}
		return r;
	}
}
