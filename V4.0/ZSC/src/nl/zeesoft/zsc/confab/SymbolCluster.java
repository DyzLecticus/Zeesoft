package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;

public class SymbolCluster {
	public List<String> symbols = new ArrayList<String>();
	
	public SymbolCluster copy() {
		SymbolCluster r = new SymbolCluster();
		for (String symbol: symbols) {
			r.symbols.add(symbol);
		}
		return r;
	}
}
