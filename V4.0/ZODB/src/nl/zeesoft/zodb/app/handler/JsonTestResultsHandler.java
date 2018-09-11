package nl.zeesoft.zodb.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.TesterObject;

public class JsonTestResultsHandler extends JsonHandlerObject {
	public final static String	PATH	= "/testResults.json"; 
	
	public JsonTestResultsHandler(Config config, AppObject app) {
		super(config,app,PATH);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		ZStringBuilder r = new ZStringBuilder();
		AppObject app = getConfiguration().getApplication(getApplicationName());
		if (app==null) {
			r = setResponse(response,405,getApplicationName() + " application not found");
		} else {
			TesterObject tester = app.tester;
			JsFile results = tester.getResults();
			if (tester.isTesting()) {
				r = setResponse(response,503,"Tester is testing. Please wait.");
			} else if (results==null) {
				if (app.selfTest) {
					r = setResponse(response,503,"Tester has not started yet. Please wait.");
				} else {
					r = setResponse(response,405,"Test results are not available");
				}
			} else {
				if (getConfiguration().isDebug()) {
					r = results.toStringBuilderReadFormat();
				} else {
					r = results.toStringBuilder();
				}
			}
		}
		return r;
	}
	
	protected String getApplicationName() {
		return getApplication().name;
	}
}
