package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;

public class SymbolCluster {
	public List<String> symbols			= new ArrayList<String>();
	public String		splitSymbolA	= "";
	public String		splitSymbolB	= "";
	
	public SymbolCluster copy() {
		SymbolCluster r = new SymbolCluster();
		for (String symbol: symbols) {
			r.symbols.add(symbol);
		}
		r.splitSymbolA = splitSymbolA;
		r.splitSymbolB = splitSymbolB;
		return r;
	}
}
