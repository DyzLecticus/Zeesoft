package nl.zeesoft.zsmc.confabulator;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

public class Confabulation {
	public ZStringSymbolParser	contextSymbols	= new ZStringSymbolParser();
	public ZStringSymbolParser	inputSymbols	= new ZStringSymbolParser();
	
	public int					confMsPerSymbol	= 100;
	public int					confSymbols		= 4;

	public ZStringSymbolParser	outputSymbols	= new ZStringSymbolParser();
	public ZStringBuilder		log				= new ZStringBuilder();
		
	protected void appendLog(String line) {
		log.append((new ZDate()).getDateTimeString());
		log.append(" ");
		log.append(line);
		log.append("\n");
	}
}
