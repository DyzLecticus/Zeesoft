package nl.zeesoft.zac.module.confabulate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConSymbolLevels {
	private SortedMap<String,Long>	symbolLevels	= new TreeMap<String,Long>();
	private List<String>			activeSymbols	= new ArrayList<String>();
	
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

	/**
	 * @return the activeSymbols
	 */
	public List<String> getActiveSymbols() {
		return activeSymbols;
	}
}
