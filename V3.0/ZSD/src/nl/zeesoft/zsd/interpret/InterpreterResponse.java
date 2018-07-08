package nl.zeesoft.zsd.interpret;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class InterpreterResponse {
	public InterpreterRequest				request					= null;
	public int								numInputSymbols			= 0;
	
	public List<SequenceClassifierResult>	responseLanguages		= new ArrayList<SequenceClassifierResult>();
	public ZStringSymbolParser				correctedInput			= new ZStringSymbolParser();
	public List<SequenceClassifierResult>	responseMasterContexts	= new ArrayList<SequenceClassifierResult>();
	public List<SequenceClassifierResult>	responseContexts		= new ArrayList<SequenceClassifierResult>();
	public ZStringSymbolParser				entityValueTranslation	= new ZStringSymbolParser();
	
	public ZStringBuilder					debugLog				= new ZStringBuilder();
	
	public InterpreterResponse(InterpreterRequest r) {
		this.request = r;
		if (request.language.length()>0) {
			responseLanguages.add(getResultForSymbol(request.language));
		}
		correctedInput = new ZStringSymbolParser(request.input);
		correctedInput.trim();
		if (correctedInput.length()>0) {
			List<String> symbols = correctedInput.toSymbolsPunctuated();
			numInputSymbols = symbols.size();
			correctedInput.fromSymbols(symbols,true,true);
			String end = correctedInput.substring(correctedInput.length() - 1,correctedInput.length()).toString();
			if (!ZStringSymbolParser.isLineEndSymbol(end)) {
				correctedInput.append(".");
				numInputSymbols++;
			}
		}
		if (request.masterContext.length()>0) {
			responseMasterContexts.add(getResultForSymbol(request.masterContext));
		}
		if (request.context.length()>0) {
			responseContexts.add(getResultForSymbol(request.context));
		}
	}

	public void addDebugLogLine(ZStringBuilder line) {
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
			if (value.length()>0) {
				debugLog.append(value.getStringBuilder());
			}
		}
	}
	
	private SequenceClassifierResult getResultForSymbol(String symbol) {
		SequenceClassifierResult r = new SequenceClassifierResult();
		r.symbol = symbol;
		r.prob = 1.0;
		r.probThreshold = 1.0;
		return r;
	}
}
