package nl.zeesoft.zsc.confab;

import nl.zeesoft.zsc.confab.confabs.ConfabulationObject;
import nl.zeesoft.zsc.confab.confabs.ContextConfabulation;
import nl.zeesoft.zsc.confab.confabs.CorrectionConfabulation;
import nl.zeesoft.zsc.confab.confabs.ExtensionConfabulation;
import nl.zeesoft.zsc.confab.confabs.SynonymConfabulation;

public class ConfabulatorRequestHandler {
	private Confabulator	confabulator	= null;
	
	public ConfabulatorRequestHandler(Confabulator conf) {
		confabulator = conf;
	}
	
	public ConfabulatorResponse handleRequest(ConfabulatorRequest request) {
		ConfabulatorResponse response = new ConfabulatorResponse();
		response.request = request;
		if (!request.type.equals(ConfabulatorRequest.CONTEXT)
			&& !request.type.equals(ConfabulatorRequest.CORRECT)
			&& !request.type.equals(ConfabulatorRequest.EXTEND)
			&& !request.type.equals(ConfabulatorRequest.SYNONYMIZE)
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
			confabulator.confabulate(confab);
			response.log = confab.log;
			response.contextResults = confab.results;
		} else if (response.request.type.equals(ConfabulatorRequest.CORRECT)) {
			CorrectionConfabulation confab = new CorrectionConfabulation();
			initializeConfabulationFromRequest(confab,response.request);
			confab.contextSymbol = response.request.contextSymbol;
			confab.validate = response.request.validate;
			confab.parallel = response.request.parallel;
			confab.alphabet = response.request.alphabet;
			confabulator.confabulate(confab);
			response.log = confab.log;
			response.corrected = confab.corrected;
			response.corrections = confab.corrections;
		} else if (response.request.type.equals(ConfabulatorRequest.EXTEND)) {
			ExtensionConfabulation confab = new ExtensionConfabulation();
			initializeConfabulationFromRequest(confab,response.request);
			confab.contextSymbol = response.request.contextSymbol;
			confab.parallel = response.request.parallel;
			confab.extend = response.request.extend;
			confabulator.confabulate(confab);
			response.log = confab.log;
			response.extension = confab.extension;
		} else if (response.request.type.equals(ConfabulatorRequest.SYNONYMIZE)) {
			SynonymConfabulation confab = new SynonymConfabulation();
			initializeConfabulationFromRequest(confab,response.request);
			confab.contextSymbol = response.request.contextSymbol;
			confab.parallel = response.request.parallel;
			confab.width = response.request.width;
			confabulator.confabulate(confab);
			response.log = confab.log;
			response.synonymResults = confab.results;
		}
	}
	
	protected void initializeConfabulationFromRequest(ConfabulationObject confab,ConfabulatorRequest request) {
		confab.appendLog = request.appendLog;
		confab.caseSensitive = request.caseSensitive;
		confab.noise = request.noise;
		confab.input = request.input;
		confab.maxTime = request.maxTime;
	}
}
