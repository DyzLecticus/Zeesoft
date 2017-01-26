package nl.zeesoft.zsc.confabulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Used by confabulators to calculate symbol levels.
 */
public final class Module {
	private SortedMap<String,Long>	symbolLevels	= new TreeMap<String,Long>();
	private List<String>			activeSymbols	= new ArrayList<String>();
	
	protected Module() {
		// Limit instantiation
	}
	
	protected void setSymbolLevel(String symbol, long level) {
		if (activeSymbols.size()==0 || activeSymbols.contains(symbol)) {
			symbolLevels.put(symbol,level);
		}
	}

	protected long getSymbolLevel(String symbol) {
		long r = 0;
		if (symbolLevels.containsKey(symbol)) {
			r = symbolLevels.get(symbol);
		}
		return r;
	}
	
	protected List<String> getSymbolsByLevel(long minLevel) {
		List<String> r = new ArrayList<String>();
		SortedMap<Long,List<String>> symbolsByLevel = new TreeMap<Long,List<String>>();
		for (Entry<String,Long> entry: symbolLevels.entrySet()) {
			if (entry.getValue()>=minLevel) {
				List<String> symbols = symbolsByLevel.get(entry.getValue());
				if (symbols==null) {
					symbols = new ArrayList<String>();
					symbolsByLevel.put(entry.getValue(),symbols);
				}
				symbols.add(entry.getKey());
			}
		}
		for (Entry<Long,List<String>> entry: symbolsByLevel.entrySet()) {
			for (String symbol: entry.getValue()) {
				r.add(0,symbol);
			}
		}
		return r;
	}

	protected String getWinningModuleSymbol() {
		String r = "";
		List<String> syms = getSymbolsByLevel(1);
		if (syms.size()==1) {
			r = syms.get(0);
		} else if (syms.size()>1) {
			long l1 = getSymbolLevel(syms.get(0));
			long l2 = getSymbolLevel(syms.get(1));
			if (l1>l2) {
				r = syms.get(0);
			}
		}
		return r;
	}

	protected void setConclusion(String winner) {
		symbolLevels.clear();
		activeSymbols.clear();
		activeSymbols.add(winner);
		setSymbolLevel(winner,1);
	}
	
	protected List<String> getActiveSymbols() {
		return activeSymbols;
	}
}
