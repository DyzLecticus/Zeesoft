package nl.zeesoft.zsd;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zsd.interpret.InterpreterRequest;
import nl.zeesoft.zsd.interpret.InterpreterResponse;

public class SequenceIntepreter {
	private SequenceClassifier						languageClassifier					= null;
	private SortedMap<String,SequenceClassifier>	languageMasterContextClassifiers	= new TreeMap<String,SequenceClassifier>();
	private SortedMap<String,SequenceClassifier>	languageContextClassifiers			= new TreeMap<String,SequenceClassifier>();
	private EntityValueTranslator					translator							= null;
	private SortedMap<String,SymbolCorrector>		languageCorrectors					= new TreeMap<String,SymbolCorrector>();

	public void setLanguageClassifier(SequenceClassifier sc) {
		languageClassifier = sc;
	}

	public void addLanguageMasterContextClassifier(String language,SequenceClassifier sc) {
		languageMasterContextClassifiers.put(language,sc);
	}

	public void addLanguageContextClassifier(String language,String context,SequenceClassifier sc) {
		String key = language + "[]" + context;
		languageContextClassifiers.put(key,sc);
	}
	
	public void setTranslator(EntityValueTranslator evt) {
		translator = evt;
	}
	
	public void addLanguageCorrector(String language,SymbolCorrector sc) {
		languageCorrectors.put(language,sc);
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
