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
		SortedMap<Double,List<ModuleSymbol>> map = new TreeMap<Double,List<ModuleSymbol>>();
		for (Entry<String,ModuleSymbol> entry: symbols.entrySet()) {
			if (entry.getValue().excitation>0D) {
				List<ModuleSymbol> syms = map.get(entry.getValue().excitation);
				if (syms==null) {
					syms = new ArrayList<ModuleSymbol>();
					map.put(entry.getValue().excitation,syms);
				}
				syms.add(entry.getValue());
			}
		}
		for (Entry<Double,List<ModuleSymbol>> entry: map.entrySet()) {
			for (ModuleSymbol sym: entry.getValue()) {
				r.add(0,sym);
			}
		}
		return r;
	}

	protected void fireLink(FireLink fl) {
		String symbol = "";
		double excitation = 0D;
		if (fl.forward) {
			symbol = fl.target;
			excitation = fl.excitation * fl.targetWeight;
		} else {
			symbol = fl.source;
			excitation = fl.excitation * fl.sourceWeight;
		}
		symbols.get(symbol).excitation += excitation;
	}
	
	protected void normalize(List<ModuleSymbol> activeSymbols, int B, double p0) {
		if (activeSymbols.size()>1) {
			List<ModuleSymbol> test = new ArrayList<ModuleSymbol>(activeSymbols); 
			double max = activeSymbols.get(0).excitation;
			double min = activeSymbols.get(activeSymbols.size() - 1).excitation;
			for (ModuleSymbol ms: test) {
				if (ms.excitation > (max - B)) {
					ms.excitation = (ms.excitation - min) / (max - min);
					if (ms.excitation<p0) {
						ms.excitation = 0D;
						activeSymbols.remove(ms);
					}
				} else {
					ms.excitation = 0D;
					activeSymbols.remove(ms);
				}
			}
		}
	}
}
