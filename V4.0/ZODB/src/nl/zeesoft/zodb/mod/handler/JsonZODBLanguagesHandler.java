package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;

public class JsonZODBLanguagesHandler extends JsonHandlerObject {
	public final static String	PATH	= "/languages.json"; 
	
	private ZStringBuilder		res		= null;
	
	public JsonZODBLanguagesHandler(Config config, ModObject mod) {
		super(config,mod,PATH);
		JsFile json = config.getLanguages().toJson();
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
