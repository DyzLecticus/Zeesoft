package nl.zeesoft.znlb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.znlb.mod.ModZNLB;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonHandlerObject;

public class JsonZNLBContextConfigurationHandler extends JsonHandlerObject {
	public final static String	PATH	= "/contextConfiguration.json"; 
	
	public JsonZNLBContextConfigurationHandler(Config config, ModObject mod) {
		super(config,mod,PATH);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		ZStringBuilder r = null;
		ModZNLB znlb = ((ModZNLB) getConfiguration().getModule(ModZNLB.NAME));
		if (!znlb.getContextConfiguration().isInitialized()) {
			r = setResponse(response,503,"Context configuration is not initialized yet. Please try again later.");
		} else {
			JsFile json = znlb.getContextConfiguration().toJson();
			if (getConfiguration().isDebug()) {
				r = json.toStringBuilderReadFormat();
			} else {
				r = json.toStringBuilder();
			}
		}
		return r;
	}
}
