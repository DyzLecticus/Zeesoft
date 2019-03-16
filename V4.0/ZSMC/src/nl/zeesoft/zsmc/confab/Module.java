package nl.zeesoft.zsmc.confab;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class Module extends Locker {
	private boolean								locked			= false;
	private SortedMap<String,ModuleSymbol>		symbols			= new TreeMap<String,ModuleSymbol>();

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

	public void setActiveSymbol(String symbol) {
		lockMe(this);
		setActiveSymbolNoLock(symbol);
		locked = true;
		unlockMe(this);
	}
	
	public void exciteSymbol(String symbol,double prob) {
		lockMe(this);
		if (!locked) {
			ModuleSymbol modSym = symbols.get(symbol);
			if (modSym==null) {
				modSym = new ModuleSymbol();
				modSym.symbol = symbol;
				symbols.put(symbol, modSym);
			}
			modSym.prob += prob;
		}
		unlockMe(this);
	}
	
	public ModuleSymbol normalize(boolean lock) {
		ModuleSymbol winner = null;
		lockMe(this);
		if (!locked) {
			List<ModuleSymbol> modSyms = getSymbolsNoLock(false,false);
			if (modSyms.size()>0) {
				double highest = modSyms.get(0).prob;
				List<ModuleSymbol> winners = new ArrayList<ModuleSymbol>();
				for (ModuleSymbol modSym: modSyms) {
					modSym.probNormalized = modSym.prob / highest;
					if (lock && modSym.probNormalized==1D) {
						winners.add(modSym);
					}
				}
				if (winners.size()==1) {
					winner = winners.get(0);
					setActiveSymbolNoLock(winner.symbol);
				}
			}
		}
		unlockMe(this);
		return winner;
	}
	
	public void supressSymbol(String symbol) {
		lockMe(this);
		if (!locked) {
			ModuleSymbol modSym = symbols.remove(symbol);
			if (modSym!=null) {
				modSym.prob = 0D;
				modSym.probNormalized = 0D;
			}
		}
		unlockMe(this);
	}

	public List<ModuleSymbol> getActiveSymbols() {
		lockMe(this);
		List<ModuleSymbol> r = getSymbolsNoLock(true,true);
		unlockMe(this);
		return r;
	}
	
	protected List<ModuleSymbol> getSymbolsNoLock(boolean normalized,boolean copy) {
		List<ModuleSymbol> r = new ArrayList<ModuleSymbol>();
		SortedMap<Double,List<ModuleSymbol>> syms = new TreeMap<Double,List<ModuleSymbol>>();
		for (ModuleSymbol modSym: symbols.values()) {
			double prob = modSym.prob;
			if (normalized) {
				prob = modSym.probNormalized;
			}
			if (prob>0D) {
				List<ModuleSymbol> msl = syms.get(prob);
				if (msl==null) {
					msl = new ArrayList<ModuleSymbol>();
					syms.put(prob,msl);
				}
				msl.add(0,modSym);
			}
		}
		for (List<ModuleSymbol> msl: syms.values()) {
			for (ModuleSymbol modSym: msl) {
				if (copy) {
					r.add(0,modSym.copy());
				} else {
					r.add(0,modSym);
				}
			}
		}
		return r;
	}

	private void setActiveSymbolNoLock(String symbol) {
		if (!locked) {
			symbols.clear();
			ModuleSymbol modSym = new ModuleSymbol();
			modSym.symbol = symbol;
			modSym.prob = 1.0D;
			modSym.probNormalized = 1.0D;
			symbols.put(symbol, modSym);
		}
	}
}
