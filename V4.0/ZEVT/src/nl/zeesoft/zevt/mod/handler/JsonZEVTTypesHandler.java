package nl.zeesoft.zevt.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zevt.mod.ModZEVT;
import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonHandlerObject;

public class JsonZEVTTypesHandler extends JsonHandlerObject {
	public final static String	PATH	= "/types.json";
	
	public ZStringBuilder		res		= null;
	
	public JsonZEVTTypesHandler(Config config, ModObject mod) {
		super(config,mod,PATH);
		Types types = ((ModZEVT) getConfiguration().getModule(ModZEVT.NAME)).getTypes(); 
		JsFile json = types.toJson();
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
