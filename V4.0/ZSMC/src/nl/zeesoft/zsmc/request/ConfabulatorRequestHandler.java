package nl.zeesoft.zsmc.request;

import nl.zeesoft.zsmc.confab.ConfabulationObject;
import nl.zeesoft.zsmc.confab.ContextConfabulation;
import nl.zeesoft.zsmc.confab.CorrectionConfabulation;
import nl.zeesoft.zsmc.confab.ExtensionConfabulation;
import nl.zeesoft.zsmc.db.KnowledgeBaseConfabulator;

public class ConfabulatorRequestHandler {
	private KnowledgeBaseConfabulator	confabulator	= null;
	
	public ConfabulatorRequestHandler(KnowledgeBaseConfabulator conf) {
		confabulator = conf;
	}
	
	public ConfabulatorResponse handleRequest(ConfabulatorRequest request) {
		ConfabulatorResponse response = new ConfabulatorResponse();
		response.request = request;
		if (!request.type.equals(ConfabulatorRequest.CONTEXT)
			&& !request.type.equals(ConfabulatorRequest.CORRECT)
			&& !request.type.equals(ConfabulatorRequest.EXTEND)
			) {
			response.error.append("Request type not supported: " + request.type);
		} else if (request.input.length()==0) {
			response.error.append("Request input is mandatory");
		} else {
			buildResponse(response);
		}
		return response;
	}
	
	protected void buildResponse(ConfabulatorResponse response) {
		if (response.request.type.equals(ConfabulatorRequest.CONTEXT)) {
			ContextConfabulation confab = new ContextConfabulation();
			initializeConfabulationFromRequest(confab,response.request);
			confabulator.getConfabulator().confabulate(confab);
			response.log = confab.log;
			response.contextResults = confab.results;
		} else if (response.request.type.equals(ConfabulatorRequest.CORRECT)) {
			CorrectionConfabulation confab = new CorrectionConfabulation();
			initializeConfabulationFromRequest(confab,response.request);
			confab.contextSymbol = response.request.contextSymbol;
			confab.alphabet = response.request.alphabet;
			confabulator.getConfabulator().confabulate(confab);
			response.log = confab.log;
			response.corrected = confab.corrected;
			response.corrections = confab.corrections;
		} else if (response.request.type.equals(ConfabulatorRequest.EXTEND)) {
			ExtensionConfabulation confab = new ExtensionConfabulation();
			initializeConfabulationFromRequest(confab,response.request);
			confab.contextSymbol = response.request.contextSymbol;
			confab.extend = response.request.extend;
			confabulator.getConfabulator().confabulate(confab);
			response.log = confab.log;
			response.extension = confab.extension;
		}
	}
	
	protected void initializeConfabulationFromRequest(ConfabulationObject confab,ConfabulatorRequest request) {
		confab.appendLog = request.appendLog;
		confab.caseSensitive = request.caseSensitive;
		confab.noise = request.noise;
		confab.input = request.input;
		confab.maxTime = request.maxTime;
		confab.strict = request.strict;
		confab.threshold = request.threshold;
		confab.unknownSymbol = request.unknownSymbol;
	}
}
