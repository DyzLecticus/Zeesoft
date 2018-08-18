package nl.zeesoft.zsd.interpret;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class SequenceInterpreterTestResult {
	public SequenceInterpreterTest	test					= null;
	public InterpreterResponse		response				= null;
	public String					error					= "";
	public long						time					= 0;

	public void determineError() {
		if (response.request.classifyLanguage && response.classifiedLanguages.size()==0) {
			error = "Failed to classify language";
		} else if (response.classifiedMasterContexts.size()==0) {
			error = "Failed to classify master context";
		} else if (response.classifiedContexts.size()==0) {
			error = "Failed to classify context";
		} else if (response.request.classifyLanguage && response.classifiedLanguages.size()>0 && !response.classifiedLanguages.get(0).symbol.equals(test.expectedLanguage)) {
			SequenceClassifierResult res = response.classifiedLanguages.get(0);
			SequenceClassifierResult found = getResultBySymbol(response.classifiedLanguages,test.expectedLanguage);
			ZStringBuilder err = getMismatchError("language",res.symbol,res.probNormalized,test.expectedLanguage,found);
			error = err.toString();
		} else if (response.classifiedMasterContexts.size()>0 && !response.classifiedMasterContexts.get(0).symbol.equals(test.expectedMasterContext)) {
			SequenceClassifierResult res = response.classifiedMasterContexts.get(0);
			SequenceClassifierResult found = getResultBySymbol(response.classifiedMasterContexts,test.expectedMasterContext);
			ZStringBuilder err = getMismatchError("master context",res.symbol,res.probNormalized,test.expectedMasterContext,found);
			error = err.toString();
		} else if (response.classifiedContexts.size()>0 && !response.classifiedContexts.get(0).symbol.equals(test.expectedContext)) {
			SequenceClassifierResult res = response.classifiedContexts.get(0);
			SequenceClassifierResult found = getResultBySymbol(response.classifiedContexts,test.expectedContext);
			ZStringBuilder err = getMismatchError("context",res.symbol,res.probNormalized,test.expectedContext,found);
			error = err.toString();
		}
	}

	public void addErrorJsonToParent(JsElem parent) {
		parent.children.add(new JsElem("dialog",test.dialogId,true));
		parent.children.add(new JsElem("input",test.input,true));
		parent.children.add(new JsElem("error",error,true));
	}
	
	private ZStringBuilder getMismatchError(String property, String symbol, double prob, String expected,SequenceClassifierResult expectedFound) {
		ZStringBuilder err = new ZStringBuilder();
		err.append("Response ");
		err.append(property);
		err.append(" does not match expected ");
		err.append(property);
		err.append(": ");
		err.append(symbol);
		err.append(" (");
		err.append("" + prob);
		err.append(") <> ");
		err.append(expected);
		err.append(" (");
		if (expectedFound!=null) {
			err.append("" + expectedFound.probNormalized);
		} else {
			err.append("?");
		}
		err.append(")");
		return err;
	}
	
	private SequenceClassifierResult getResultBySymbol(List<SequenceClassifierResult> results,String symbol) {
		SequenceClassifierResult r = null;
		for (SequenceClassifierResult res: results) {
			if (res.symbol.equals(symbol)) {
				r = res;
				break;
			}
		}
		return r;
	}
}
