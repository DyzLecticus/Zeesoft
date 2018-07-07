package nl.zeesoft.zsd;

import nl.zeesoft.zsd.interpret.InterpreterConfiguration;
import nl.zeesoft.zsd.interpret.InterpreterRequest;
import nl.zeesoft.zsd.interpret.InterpreterResponse;

public class SequenceInterpreter {
	private InterpreterConfiguration				configuration						= null;
	
	public SequenceInterpreter(InterpreterConfiguration c) {
		configuration = c;
	}
	
	public InterpreterResponse interpretRequest(InterpreterRequest request) {
		InterpreterResponse r = new InterpreterResponse();
		r.request = request;
		r.responseLanguage = request.language;
		r.responseMasterContext = request.masterContext;
		r.responseContext = request.context;
		processRequest(r);
		return r;
	}
	
	private void processRequest(InterpreterResponse response) {
		// Check classifications
		if (response.responseLanguage.length()==0 || response.request.checkLanguage) {
			
		}
		if (response.responseMasterContext.length()==0 || response.request.checkMasterContext) {
			
		}
		if (response.responseContext.length()==0 || response.request.checkContext) {
			
		}
		// Correct input
		if (response.request.correctInput) {
			
		}
		// Translate
		if (response.request.translateEntiyValues) {
			
		}
	}
}
