package nl.zeesoft.zsmc.confab;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zsmc.confab.confabs.ConfabulationObject;
import nl.zeesoft.zsmc.kb.KbContext;

public class Module extends Locker {
	private boolean								locked			= false;
	private SortedMap<String,ModuleSymbol>		symbols			= new TreeMap<String,ModuleSymbol>();

	public Module(Messenger msgr) {
		super(msgr);
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
	
	public ModuleSymbol normalize(double threshold) {
		return normalize(false,null,null,null,threshold);
	}
	
	public ModuleSymbol normalize(boolean checkLock,Module nextModule,ConfabulationObject confab,KbContext context,double threshold) {
		ModuleSymbol winner = null;
		lockMe(this);
		if (!locked) {
			List<ModuleSymbol> modSyms = getSymbolsNoLock(false,false);
			if (modSyms.size()>0) {
				double highest = modSyms.get(0).prob;
				List<ModuleSymbol> winners = new ArrayList<ModuleSymbol>();
				for (ModuleSymbol modSym: modSyms) {
					modSym.probNormalized = modSym.prob / highest;
					if (modSym.probNormalized<threshold) {
						symbols.remove(modSym.symbol);
					} else if (checkLock && modSym.probNormalized==1D) {
						winners.add(modSym);
					}
				}
				if (winners.size()==1) {
					winner = winners.get(0);
					setActiveSymbolNoLock(winner.symbol);
					locked = true;
				}
			}
			if (winner!=null && nextModule!=null && confab!=null && context!=null && confab.strict && !nextModule.isLocked()) {
				confab.limitLinksInModule(nextModule,winner,1,context);
			}
		}
		unlockMe(this);
		return winner;
	}

	public void supressSymbolsExcept(List<String> exceptions) {
		lockMe(this);
		if (!locked) {
			List<ModuleSymbol> syms = new ArrayList<ModuleSymbol>(symbols.values());
			for (ModuleSymbol mSym: syms) {
				if (!exceptions.contains(mSym.symbol)) {
					ModuleSymbol modSym = symbols.remove(mSym.symbol);
					if (modSym!=null) {
						modSym.prob = 0D;
						modSym.probNormalized = 0D;
					}
				}
			}
		}
		unlockMe(this);
	}

	public List<ModuleSymbol> getActiveSymbolsNormalized() {
		lockMe(this);
		List<ModuleSymbol> r = getSymbolsNoLock(true,true);
		unlockMe(this);
		return r;
	}

	public List<ModuleSymbol> getActiveSymbols() {
		lockMe(this);
		List<ModuleSymbol> r = getSymbolsNoLock(false,true);
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
