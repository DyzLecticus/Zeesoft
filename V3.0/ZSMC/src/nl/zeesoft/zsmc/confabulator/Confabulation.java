package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

public class Confabulation {
	public ZStringSymbolParser	contextSymbols	= null;
	public ZStringSymbolParser	inputSymbols	= null;
	
	public int					confMsPerSymbol	= 100;
	public int					confSymbols		= 4;

	public List<String>			outputSymbols	= new ArrayList<String>();
	public ZStringBuilder		log				= new ZStringBuilder();

	
}
