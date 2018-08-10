package nl.zeesoft.zsd.interpret;

public class SequenceInterpreterTestResult {
	public SequenceInterpreterTest	test					= null;
	public InterpreterResponse		response				= null;
	public boolean					success					= false;
	public void determineResult() {
		if (
			(test.expectedLanguage.length()==0 || (response.responseLanguages.size()>0 && response.responseLanguages.get(0).symbol.equals(test.expectedLanguage))) &&
			(response.responseMasterContexts.size()>0 && response.responseMasterContexts.get(0).symbol.equals(test.expectedMasterContext)) &&
			(response.responseContexts.size()>0 && response.responseContexts.get(0).symbol.equals(test.expectedContext))
			) {
			success = true;
		}
	}
}
