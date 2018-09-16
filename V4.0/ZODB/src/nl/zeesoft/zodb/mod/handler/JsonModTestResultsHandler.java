package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
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
			if (!mod.selfTest) {
				r = setResponse(response,405,getModuleName() + " is not configured to test itself");
			} else if (mod.testers.size()==0) {
				r = setResponse(response,405,getModuleName() + " does not have any self testers");
			} else if (mod.testers.size()>0) {
				JsFile json = new JsFile();
				json.rootElement = new JsElem();
				JsElem tstsElem = new JsElem("testers",true);
				json.rootElement.children.add(tstsElem);
				for (TesterObject tester: mod.testers) {
					JsFile results = tester.getResults();
					if (tester.isTesting()) {
						r = setResponse(response,503,getModuleName() + " is testing itself. Please wait.");
						break;
					} else if (results==null) {
						r = setResponse(response,503,getModuleName() + " tester has not started yet. Please wait.");
						break;
					} else {
						JsElem tstElem = new JsElem();
						tstsElem.children.add(tstElem);
						tstElem.children = results.rootElement.children;
					}
				}
				if (r.length()==0) {
					if (getConfiguration().isDebug()) {
						r = json.toStringBuilderReadFormat();
					} else {
						r = json.toStringBuilder();
					}
				}
			} else {
				r = setResponse(response,405,getModuleName() + " test results are not available");
			}
		}
		return r;
	}
	
	protected String getModuleName() {
		return getModule().name;
	}
}
