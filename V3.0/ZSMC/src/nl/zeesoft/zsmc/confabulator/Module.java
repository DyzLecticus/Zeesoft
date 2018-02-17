package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class Module extends Locker {
	private int								B				= 0;
	private double							p0				= 0D;
	private	String							name			= "";
	private boolean							context			= false;
	
	private SortedMap<String,ModuleSymbol>	symbols			= new TreeMap<String,ModuleSymbol>();
	private SortedMap<String,ModuleSymbol>	activeSymbols	= new TreeMap<String,ModuleSymbol>();
	private boolean							locked			= false;
	
	protected Module(Messenger msgr, KnowledgeBases kbs, String name, boolean context) {
		super(msgr);
		this.name = name;
		this.context = context;
		B = kbs.getB();
		p0 = kbs.getP0();
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
	
	protected String getName() {
		return name;
	}

	protected boolean isContext() {
		return context;
	}

	protected void setConclusion(String symbol) {
		lockMe(this);
		activeSymbols.clear();
		for (Entry<String,ModuleSymbol> entry: symbols.entrySet()) {
			if (entry.getValue().symbol.equals(symbol)) {
				entry.getValue().excitation = 1.0D;
				activeSymbols.put(entry.getKey(),entry.getValue().getCopy());
			} else {
				entry.getValue().excitation = 0.0D;
			}
		}
		unlockMe(this);
	}

	protected void setConclusions(List<ModuleSymbol> syms) {
		lockMe(this);
		activeSymbols.clear();
		for (Entry<String,ModuleSymbol> entry: symbols.entrySet()) {
			entry.getValue().excitation = 0.0D;
			for (ModuleSymbol sym: syms) {
				if (entry.getKey().equals(sym.symbol)) {
					entry.getValue().excitation = sym.excitation;
					activeSymbols.put(entry.getKey(),entry.getValue().getCopy());
					break;
				}
			}
		}
		unlockMe(this);
	}

	protected boolean isLocked() {
		boolean r = false;
		lockMe(this);
		r = locked;
		unlockMe(this);
		return r;
	}

	protected void setLocked(boolean locked) {
		lockMe(this);
		this.locked = locked;
		unlockMe(this);
	}
	
	protected List<ModuleSymbol> getActiveSymbols() {
		List<ModuleSymbol> r = new ArrayList<ModuleSymbol>();

		lockMe(this);
		SortedMap<Double,List<ModuleSymbol>> map = new TreeMap<Double,List<ModuleSymbol>>();
		for (Entry<String,ModuleSymbol> entry: activeSymbols.entrySet()) {
			List<ModuleSymbol> syms = map.get(entry.getValue().excitation);
			if (syms==null) {
				syms = new ArrayList<ModuleSymbol>();
				map.put(entry.getValue().excitation,syms);
			}
			syms.add(entry.getValue());
		}
		for (Entry<Double,List<ModuleSymbol>> entry: map.entrySet()) {
			for (ModuleSymbol sym: entry.getValue()) {
				r.add(0,sym);
			}
		}
		unlockMe(this);
		
		return r;
	}

	protected int fireLinks(List<FireLink> fireLinks) {
		int firedLinks = 0;
		lockMe(this);
		if (!locked) {
			for (FireLink fl: fireLinks) {
				fireLinkNoLock(fl);
			}
			//normalizeNoLock();
			firedLinks = fireLinks.size();
		}
		unlockMe(this);
		return firedLinks;
	}
	
	protected void contract() {
		lockMe(this);
		normalizeNoLock();
		unlockMe(this);
	}

	private void fireLinkNoLock(FireLink fl) {
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
		activeSymbols.put(symbol,symbols.get(symbol).getCopy());
	}
	
	private void normalizeNoLock() {
		if (activeSymbols.size()>1) {
			List<String> test = new ArrayList<String>(activeSymbols.keySet()); 
			double max = 0D;
			double min = 0D;
			for (Entry<String,ModuleSymbol> entry: activeSymbols.entrySet()) {
				if (entry.getValue().excitation>max) {
					max = entry.getValue().excitation;
				}
			}
			min = max;
			for (Entry<String,ModuleSymbol> entry: activeSymbols.entrySet()) {
				if (entry.getValue().excitation<min) {
					min = entry.getValue().excitation;
				}
				if (min < (max - B)) {
					min = (max - B);
					break;
				}
			}
			for (String sym: test) {
				ModuleSymbol ms = symbols.get(sym);
				if (ms.excitation > (max - B)) {
					ms.excitation = (ms.excitation - min) / (max - min);
					if (ms.excitation<p0) {
						ms.excitation = 0D;
						activeSymbols.remove(sym);
					} else {
						activeSymbols.put(sym,ms.getCopy());
					}
				} else {
					ms.excitation = 0D;
					activeSymbols.remove(sym);
					
				}
			}
			if (activeSymbols.size()==1) {
				locked = true;
			}
		}
	}
}
