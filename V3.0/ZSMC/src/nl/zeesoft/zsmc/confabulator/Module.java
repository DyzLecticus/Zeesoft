package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class Module {
	private SortedMap<String,ModuleSymbol>	symbols		= new TreeMap<String,ModuleSymbol>();
	private boolean							locked		= false;
	
	protected Module(KnowledgeBases kbs, boolean context) {
		if (context) {
			for (String symbol: kbs.getContext().getLinksBySource().keySet()) {
				ModuleSymbol sym = new ModuleSymbol();
				sym.symbol = symbol;
				symbols.put(symbol,sym);
			}
		} else {
			for (String symbol: kbs.getKnownSymbols().keySet()) {
				ModuleSymbol sym = new ModuleSymbol();
				sym.symbol = symbol;
				symbols.put(symbol,sym);
			}
		}
	}

	protected String getConclusion() {
		String r = null;
		ModuleSymbol sym = null;
		List<ModuleSymbol> sms = getActiveSymbols();
		if (sms.size()>0) {
			sym = sms.get(0);
			if (sms.size()>1 && sms.get(1).excitation==sym.excitation) {
				sym = null;
			}
		}
		if (sym!=null) {
			r = sym.symbol;
		}
		return r;
	}

	protected void setConclusion(String symbol) {
		for (Entry<String,ModuleSymbol> entry: symbols.entrySet()) {
			if (entry.getValue().symbol.equals(symbol)) {
				entry.getValue().excitation = 1.0D;
			} else {
				entry.getValue().excitation = 0.0D;
			}
		}
	}

	protected void setConclusions(List<ModuleSymbol> syms) {
		for (Entry<String,ModuleSymbol> entry: symbols.entrySet()) {
			entry.getValue().excitation = 0.0D;
			for (ModuleSymbol sym: syms) {
				if (entry.getValue().symbol.equals(sym.symbol)) {
					entry.getValue().excitation = sym.excitation;
					break;
				}
			}
		}
	}

	protected boolean isLocked() {
		return locked;
	}

	protected void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	protected List<ModuleSymbol> getActiveSymbols() {
		List<ModuleSymbol> r = new ArrayList<ModuleSymbol>();
		SortedMap<Double,ModuleSymbol> map = new TreeMap<Double,ModuleSymbol>();
		for (Entry<String,ModuleSymbol> entry: symbols.entrySet()) {
			if (entry.getValue().excitation>0D) {
				map.put(entry.getValue().excitation,entry.getValue());
			}
		}
		for (Entry<Double,ModuleSymbol> entry: map.entrySet()) {
			r.add(0,entry.getValue());
		}
		return r;
	}
}
