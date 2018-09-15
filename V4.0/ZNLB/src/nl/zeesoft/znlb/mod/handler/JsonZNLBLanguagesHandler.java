package nl.zeesoft.znlb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.znlb.ZNLBConfig;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonHandlerObject;

public class JsonZNLBLanguagesHandler extends JsonHandlerObject {
	public final static String	PATH	= "/languages.json"; 
	
	private ZStringBuilder		res		= null;
	
	public JsonZNLBLanguagesHandler(Config config, ModObject mod) {
		super(config,mod,PATH);
		JsFile json = ((ZNLBConfig)config).getLanguages().toJson();
		if (config.isDebug()) {
			res = json.toStringBuilderReadFormat();
		} else {
			res = json.toStringBuilder();
		}
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		return res;
	}
}
