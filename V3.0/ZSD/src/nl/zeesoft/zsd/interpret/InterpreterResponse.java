package nl.zeesoft.zsd.interpret;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class InterpreterResponse {
	public InterpreterRequest				request							= null;
	
	public List<SequenceClassifierResult>	responseLanguages				= new ArrayList<SequenceClassifierResult>();
	public ZStringSymbolParser				correctedInput					= new ZStringSymbolParser();
	public List<SequenceClassifierResult>	responseMasterContexts			= new ArrayList<SequenceClassifierResult>();
	public List<SequenceClassifierResult>	responseContexts				= new ArrayList<SequenceClassifierResult>();
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
}
