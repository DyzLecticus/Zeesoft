package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.TesterObject;

public class JsonModTestResultsHandler extends JsonHandlerObject {
	public final static String	PATH	= "/testResults.json"; 
	
	public JsonModTestResultsHandler(Config config, ModObject mod) {
		super(config,mod,PATH);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		ZStringBuilder r = new ZStringBuilder();
		ModObject mod = getConfiguration().getModule(getModuleName());
		if (mod==null) {
			r = setResponse(response,405,getModuleName() + " module not found");
		} else {
			TesterObject tester = mod.tester;
			if (tester==null) {
				r = setResponse(response,405,getModuleName() + " test results are not available");
			} else {
				JsFile results = tester.getResults();
				if (tester.isTesting()) {
					r = setResponse(response,503,getModuleName() + " is testing itself. Please wait.");
				} else if (results==null) {
					if (mod.selfTest) {
						r = setResponse(response,503,getModuleName() + " tester has not started yet. Please wait.");
					} else {
						r = setResponse(response,405,getModuleName() + " test results are not available");
					}
				} else {
					if (getConfiguration().isDebug()) {
						r = results.toStringBuilderReadFormat();
					} else {
						r = results.toStringBuilder();
					}
				}
			}
		}
		return r;
	}
	
	protected String getModuleName() {
		return getModule().name;
	}
}
