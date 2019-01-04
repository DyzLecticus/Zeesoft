package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;

public class Module {
	public boolean							locked			= false;
	public List<String>						suppressSymbols	= new ArrayList<String>();
	public SortedMap<String,ModuleSymbol>	symbols			= new TreeMap<String,ModuleSymbol>();
	
	public Module copy() {
		Module r = new Module();
		r.locked = this.locked;
		r.suppressSymbols = new ArrayList<String>(suppressSymbols);
		for (ModuleSymbol modSym: this.symbols.values()) {
			r.symbols.put(modSym.symbol,modSym.copy());
		}
		return r;
	}
	
	public ZStringBuilder getSymbolState() {
		return getSymbolState(null);
	}

	public ZStringBuilder getSymbolState(List<ModuleSymbol> symbols) {
		if (symbols==null) {
			symbols = getSymbols();
		}
		ZStringBuilder r = new ZStringBuilder();
		for (ModuleSymbol modSym: symbols) {
			if (r.length()>0) {
				r.append(",");
			}
			r.append(modSym.symbol);
		}
		return r;
	}

	public List<ModuleSymbol> getSymbols() {
		List<ModuleSymbol> r = new ArrayList<ModuleSymbol>();
		SortedMap<Double,List<ModuleSymbol>> syms = new TreeMap<Double,List<ModuleSymbol>>();
		for (ModuleSymbol modSym: symbols.values()) {
			if (modSym.prob>0D) {
				List<ModuleSymbol> msl = syms.get(modSym.prob);
				if (msl==null) {
					msl = new ArrayList<ModuleSymbol>();
					syms.put(modSym.prob,msl);
				}
				msl.add(0,modSym);
			}
		}
		for (List<ModuleSymbol> msl: syms.values()) {
			for (ModuleSymbol modSym: msl) {
				r.add(0,modSym);
			}
		}
		return r;
	}
}
