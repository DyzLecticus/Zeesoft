package nl.zeesoft.zsmc.confab;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class Module extends Locker {
	private boolean								locked			= false;
	private SortedMap<String,ModuleSymbol>	symbols			= new TreeMap<String,ModuleSymbol>();

	public Module(Messenger msgr) {
		super(msgr);
	}

	public Module copy() {
		Module r = new Module(getMessenger());
		lockMe(this);
		r.locked = this.locked;
		for (ModuleSymbol modSym: this.symbols.values()) {
			r.symbols.put(modSym.symbol,modSym.copy());
		}
		unlockMe(this);
		return r;
	}

	public boolean isLocked() {
		boolean r = false;
		lockMe(this);
		r = locked;
		unlockMe(this);
		return r;
	}

	public void setLocked(boolean locked) {
		lockMe(this);
		this.locked = locked;
		unlockMe(this);
	}
	
	public void normalize(boolean locked) {
		lockMe(this);
		if (!locked) {
			List<ModuleSymbol> modSyms = getSymbolsNoLock();
			if (modSyms.size()>0) {
				double highest = modSyms.get(0).prob;
				for (ModuleSymbol modSym: modSyms) {
					modSym.probNormalized = modSym.prob / highest;
				}
			}
		}
		unlockMe(this);
	}
	
	protected List<ModuleSymbol> getSymbolsNoLock() {
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
