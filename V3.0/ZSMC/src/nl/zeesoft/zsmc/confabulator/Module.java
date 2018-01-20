package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class Module extends Locker {
	private KnowledgeBases					kbs			= null;
	private ModuleWorker					worker		= null;
	private	List<Module>					modules		= null;
	private int								B			= 0;
	private double							p0			= 0;
	
	private SortedMap<String,ModuleSymbol>	symbols		= new TreeMap<String,ModuleSymbol>();
	
	private int 							symIndex	= 0;
	private List<ModuleSymbol>				syms		= new ArrayList<ModuleSymbol>();
	private boolean							locked		= false;
	
	public Module(Messenger msgr, WorkerUnion uni, KnowledgeBases kbs,List<Module> mods,int B, double p0) {
		super(msgr);
		this.kbs = kbs;
		this.modules = mods;
		this.B = B;
		this.p0 = p0;
		worker = new ModuleWorker(msgr,uni,this);
		for (String symbol: kbs.getKnownSymbols().keySet()) {
			ModuleSymbol sym = new ModuleSymbol();
			sym.symbol = symbol;
			symbols.put(symbol,sym);
		}
	}

	protected String getConclusion() {
		String r = null;
		lockMe(this);
		ModuleSymbol sym = null;
		List<ModuleSymbol> sms = getActiveSymbols();
		if (sms.size()>0) {
			sym = sms.get((sms.size() - 1));
			if (sms.size()>1 && syms.get((sms.size() - 2)).excitation==sym.excitation) {
				sym = null;
			}
		}
		if (sym!=null) {
			r = sym.symbol;
		}
		unlockMe(this);
		return r;
	}

	protected void setConclusion(String symbol) {
		lockMe(this);
		List<ModuleSymbol> sms = getActiveSymbols();
		for (ModuleSymbol sym: sms) {
			if (sym.symbol.equals(symbol)) {
				sym.excitation = 1.0D;
			} else {
				sym.excitation = 0.0D;
			}
		}
		unlockMe(this);
	}

	protected void startConfabulation(int confMs) {
		lockMe(this);
		symIndex = -1;
		worker.startConfabulation(confMs);
		unlockMe(this);
	}

	protected boolean isWorking() {
		return worker.isWorking();
	}

	protected boolean confabulate() {
		boolean done = false;
		
		SortedMap<Module,List<KnowledgeLink>> fireLinksForward = new TreeMap<Module,List<KnowledgeLink>>();
		SortedMap<Module,List<KnowledgeLink>> fireLinksBackward = new TreeMap<Module,List<KnowledgeLink>>();
		
		List<Boolean> locks = new ArrayList<Boolean>();
		for (Module mod: modules) {
			locks.add(mod.isLocked());
		}
		
		lockMe(this);
		if (symIndex<0) {
			syms.clear();
			syms = getActiveSymbols();
			symIndex = (syms.size() - 1);
		}
		if (symIndex>=0) {
			ModuleSymbol sym = syms.get(symIndex);
			int m = 0;
			boolean forward = false;
			for (Module mod: modules) {
				if (this!=mod && !locks.get(m)) {
					if (forward) {
						int distance = (modules.indexOf(mod) - m); 
						List<KnowledgeLink> links = kbs.getKnowledgeBases().get(distance - 1).getLinksBySource().get(sym.symbol);
						fireLinksForward.put(mod,links);
					} else {
						int distance = (m - modules.indexOf(mod)); 
						List<KnowledgeLink> links = kbs.getKnowledgeBases().get(distance - 1).getLinksByTarget().get(sym.symbol);
						fireLinksBackward.put(mod,links);
					}
				} else {
					forward = true;
				}
				m++;
			}
			symIndex--;
		}
		unlockMe(this);
		
		// Fire links forward
		for (Entry<Module,List<KnowledgeLink>> entry: fireLinksForward.entrySet()) {
			entry.getKey().fireLinks(entry.getValue(),true);
		}
		// Fire links backward
		for (Entry<Module,List<KnowledgeLink>> entry: fireLinksBackward.entrySet()) {
			entry.getKey().fireLinks(entry.getValue(),false);
		}
		
		return done;
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

	protected void fireLinks(List<KnowledgeLink> links,boolean forward) {
		if (!isLocked()) {
			lockMe(this);
			double excite = 0D;
			double prob = 0D;
			String symbol = "";
			for (KnowledgeLink link: links) {
				if (forward) {
					symbol = link.target;
				} else {
					symbol = link.source;
				}
				prob = kbs.getKnownSymbols().get(symbol).prob;
				excite = ((link.prob * prob) / prob) / p0;
				System.out.println("Excite 1 = " + excite);
				excite = (Math.log(excite) / Math.log(2.0)) + B;
				System.out.println("Excite 2 = " + excite);
				symbols.get(symbol).excitation += excite;
			}
			unlockMe(this);
		}
	}
	
	private List<ModuleSymbol> getActiveSymbols() {
		List<ModuleSymbol> r = new ArrayList<ModuleSymbol>();
		SortedMap<Double,ModuleSymbol> map = new TreeMap<Double,ModuleSymbol>();
		for (Entry<String,ModuleSymbol> entry: symbols.entrySet()) {
			if (entry.getValue().excitation>0D) {
				map.put(entry.getValue().excitation,entry.getValue());
			}
		}
		for (Entry<Double,ModuleSymbol> entry: map.entrySet()) {
			r.add(entry.getValue());
		}
		return r;
	}
}
