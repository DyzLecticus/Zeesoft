package nl.zeesoft.zsd.interpret;

import java.util.ArrayList;
import java.util.List;

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
			String end = correctedInput.substring(correctedInput.length() - 1,correctedInput.length());
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
	
	private SequenceClassifierResult getResultForSymbol(String symbol) {
		SequenceClassifierResult r = new SequenceClassifierResult();
		r.symbol = symbol;
		r.prob = 1.0;
		r.probThreshold = 1.0;
		return r;
	}
}
