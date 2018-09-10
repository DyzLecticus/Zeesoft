package nl.zeesoft.zodb.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.ZODBTester;

public class JsonZODBTestResultsHandler extends JsonHandlerObject {
	public final static String	PATH	= "/testResults.json"; 
	
	public JsonZODBTestResultsHandler(Config config, AppObject app) {
		super(config,app,PATH);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		ZStringBuilder r = new ZStringBuilder();
		ZODBTester tester = getConfiguration().getZODB().getTester();
		JsFile results = tester.getResults();
		if (tester.isTesting()) {
			r = setResponse(response,503,"Tester is testing. Please wait.");
		} else if (results==null) {
			if (getConfiguration().getZODB().selfTest) {
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
		return r;
	}
}
