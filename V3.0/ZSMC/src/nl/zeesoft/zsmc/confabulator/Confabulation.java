package nl.zeesoft.zsmc.confabulator;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

public class Confabulation {
	public ZStringSymbolParser	contextSymbols	= new ZStringSymbolParser();
	public ZStringSymbolParser	inputSymbols	= new ZStringSymbolParser();
	
	public int					confMs			= 500;
	public int					confSymbols		= 4;

	public ZStringSymbolParser	outputSymbols	= new ZStringSymbolParser();
	public ZStringBuilder		log				= new ZStringBuilder();

	public int					contextMs		= 0;
	public int					prefixMs		= 0;
	public int					symbolMs		= 0;

	public long					startTime		= 0;
	public long					stopTime		= 0;

	protected void appendLog(ZStringBuilder append) {
		log.append((new ZDate()).getDateTimeString());
		log.append(" ");
		log.append(append);
		log.append("\n");
	}
	
	public int getContextMs() {
		if (contextMs<=0) {
			contextMs = confMs / (confSymbols + 2);
		}
		return contextMs;
	}

	public int getPrefixMs() {
		if (prefixMs<=0) {
			prefixMs = confMs / (confSymbols + 2);
		}
		return prefixMs;
	}

	public int getSymbolMs() {
		if (symbolMs<=0) {
			symbolMs = confMs / (confSymbols + 2);
		}
		return symbolMs;
	}
}
