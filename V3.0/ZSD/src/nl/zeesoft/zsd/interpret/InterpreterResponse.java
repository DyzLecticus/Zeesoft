package nl.zeesoft.zsd.interpret;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class InterpreterResponse {
	public InterpreterRequest				request							= null;
	
	public List<SequenceClassifierResult>	classifiedLanguages				= new ArrayList<SequenceClassifierResult>();
	public ZStringSymbolParser				correctedInput					= new ZStringSymbolParser();
	public ZStringSymbolParser				classificationSequence			= new ZStringSymbolParser();
	public List<SequenceClassifierResult>	classifiedMasterContexts		= new ArrayList<SequenceClassifierResult>();
	public List<SequenceClassifierResult>	classifiedContexts				= new ArrayList<SequenceClassifierResult>();
	public ZStringSymbolParser				entityValueTranslation			= new ZStringSymbolParser();
	public ZStringSymbolParser				entityValueTranslationCorrected	= new ZStringSymbolParser();
	
	public ZStringBuilder					debugLog						= new ZStringBuilder();
	
	public InterpreterResponse(InterpreterRequest r) {
		this.request = r;
	}

	public void addDebugLogLine(ZStringBuilder line) {
		addDebugLogLine(line,new ZStringBuilder());
	}
	
	public void addDebugLogLine(String line) {
		addDebugLogLine(line,new ZStringBuilder());
	}
	
	public void addDebugLogLine(ZStringBuilder line,String value) {
		addDebugLogLine(line,new ZStringBuilder(value));
	}

	public void addDebugLogLine(String line,ZStringBuilder value) {
		addDebugLogLine(new ZStringBuilder(line),value);
	}

	public void addDebugLogLine(String line,String value) {
		addDebugLogLine(new ZStringBuilder(line),new ZStringBuilder(value));
	}
	
	public void addDebugLogLine(ZStringBuilder line,ZStringBuilder value) {
		if (request.appendDebugLog) {
			if (debugLog.length()>0) {
				debugLog.append("\n");
			}
			ZDate ts = new ZDate();
			debugLog.append(ts.getTimeString(true).getStringBuilder());
			debugLog.append(": ");
			debugLog.append(line.getStringBuilder());
			if (value!=null && value.length()>0) {
				debugLog.append(value.getStringBuilder());
			}
		}
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		addResultsToJson(json.rootElement,"classifiedLanguages",classifiedLanguages);
		json.rootElement.children.add(new JsElem("correctedInput",correctedInput,true));
		json.rootElement.children.add(new JsElem("classificationSequence",classificationSequence,true));
		addResultsToJson(json.rootElement,"classifiedMasterContexts",classifiedMasterContexts);
		addResultsToJson(json.rootElement,"classifiedContexts",classifiedContexts);
		json.rootElement.children.add(new JsElem("entityValueTranslation",entityValueTranslation,true));
		json.rootElement.children.add(new JsElem("entityValueTranslationCorrected",entityValueTranslationCorrected,true));
		json.rootElement.children.add(new JsElem("debugLog",debugLog,true));
		return json;
	}
	
	public void fromJson(JsFile json) {
		getResultsFromJson(classifiedLanguages,json.rootElement,"classifiedLanguages");
		correctedInput = json.rootElement.getChildZStringSymbolParser("correctedInput");
		classificationSequence = json.rootElement.getChildZStringSymbolParser("classificationSequence");
		getResultsFromJson(classifiedMasterContexts,json.rootElement,"classifiedMasterContexts");
		getResultsFromJson(classifiedContexts,json.rootElement,"classifiedContexts");
		entityValueTranslation = json.rootElement.getChildZStringSymbolParser("entityValueTranslation");
		entityValueTranslationCorrected = json.rootElement.getChildZStringSymbolParser("entityValueTranslationCorrected");
		debugLog = json.rootElement.getChildZStringBuilder("debugLog");
	}
	
	private void addResultsToJson(JsElem parent,String name, List<SequenceClassifierResult> results) {
		JsElem rsElem = new JsElem(name,true);
		parent.children.add(rsElem);
		for (SequenceClassifierResult res: results) {
			JsElem resElem = new JsElem();
			rsElem.children.add(resElem);
			resElem.children.add(new JsElem("symbol",res.symbol,true));
			resElem.children.add(new JsElem("prob","" + res.prob));
			resElem.children.add(new JsElem("probNormalized","" + res.probNormalized));
		}
	}

	private void getResultsFromJson(List<SequenceClassifierResult> results,JsElem parent,String name) {
		results.clear();
		JsElem rsElem = parent.getChildByName(name);
		if (rsElem!=null) {
			for (JsElem resElem: rsElem.children) {
				SequenceClassifierResult res = new SequenceClassifierResult();
				res.symbol = resElem.getChildString("symbol");
				res.prob = resElem.getChildDouble("prob");
				res.probNormalized = resElem.getChildDouble("probNormalized");
				results.add(res);
			}
		}
	}
}
