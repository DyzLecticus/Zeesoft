package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
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
	
	protected Module(Messenger msgr, WorkerUnion uni, KnowledgeBases kbs,List<Module> mods,int B, double p0) {
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
			if (sms.size()>1 && sms.get((sms.size() - 2)).excitation==sym.excitation) {
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
		for (Entry<String,ModuleSymbol> entry: symbols.entrySet()) {
			if (entry.getValue().symbol.equals(symbol)) {
				entry.getValue().excitation = 1.0D;
			} else {
				entry.getValue().excitation = 0.0D;
			}
		}
		unlockMe(this);
	}

	protected void startConfabulation(int confMs,int contMs) {
		lockMe(this);
		symIndex = -1;
		worker.startConfabulation(confMs,contMs);
		unlockMe(this);
	}

	protected boolean isDone() {
		return worker.isDone();
	}

	protected boolean confabulate() {
		boolean done = false;

		SortedMap<Integer,Boolean> moduleLocks = getModuleLocks();
		SortedMap<Integer,List<KnowledgeLink>> fireLinksForward = new TreeMap<Integer,List<KnowledgeLink>>();
		SortedMap<Integer,List<KnowledgeLink>> fireLinksBackward = new TreeMap<Integer,List<KnowledgeLink>>();
	
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
				if (this!=mod && !moduleLocks.get(m)) {
					if (forward) {
						int distance = (m - modules.indexOf(this)); 
						List<KnowledgeLink> links = kbs.getKnowledgeBases().get(distance - 1).getLinksBySource().get(sym.symbol);
						if (links!=null) {
							fireLinksForward.put(m,links);
						}
					} else {
						int distance = (modules.indexOf(this) - m); 
						List<KnowledgeLink> links = kbs.getKnowledgeBases().get(distance - 1).getLinksByTarget().get(sym.symbol);
						if (links!=null) {
							fireLinksBackward.put(m,links);
						}
					}
				}
				if (this==mod) {
					forward = true;
				}
				m++;
			}
			symIndex--;
		}
		unlockMe(this);
		
		// Fire links forward
		for (Entry<Integer,List<KnowledgeLink>> entry: fireLinksForward.entrySet()) {
			modules.get(entry.getKey()).fireLinks(entry.getValue(),true);
		}
		// Fire links backward
		for (Entry<Integer,List<KnowledgeLink>> entry: fireLinksBackward.entrySet()) {
			modules.get(entry.getKey()).fireLinks(entry.getValue(),false);
		}
		
		return done;
	}

	protected void contract() {
		// TODO: Contract
		lockMe(this);
		if (syms.size()>1) {
			//double min = (syms.get(syms.size()-1).excitation / 2D);
			double min = (syms.get(syms.size()-1).excitation - (double) B);
			System.out.println("Contract!" + syms.get(syms.size()-1).excitation + " / " + min);
			List<ModuleSymbol> test = new ArrayList<ModuleSymbol>(syms);
			for (ModuleSymbol sym: test) {
				if (sym.excitation<min) {
					sym.excitation = 0;
					syms.remove(sym);
					symIndex--;
				}
			}
			System.out.println("syms: " + syms.size() + " test:" + test.size());
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

	protected void fireLinks(List<KnowledgeLink> links,boolean forward) {
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
			//System.out.println("Excite 1 = " + excite);
			excite = (Math.log(excite) / Math.log(2.0)) + B;
			//System.out.println("Excite 2 = " + excite);
			symbols.get(symbol).excitation += excite;
		}
		unlockMe(this);
	}
	
	protected ZStringBuilder getActiveSymbolsList() {
		ZStringBuilder r = new ZStringBuilder();
		List<ModuleSymbol> sms = getActiveSymbols();
		lockMe(this);
		int added = 0;
		for (int i = (sms.size() - 1); i>=0; i--) {
			ModuleSymbol sym = sms.get(i);
			if (added<=3) {
				if (r.length()>0) {
					r.append(", ");
				}
				r.append(sym.symbol);
				r.append("(");
				r.append("" + sym.excitation);
			} else {
				r.append(" [, ... ");
				r.append("" + ((sms.size() - 1) - added));
				r.append("]");
				break;
			}
			r.append(")");
			added++;
		}
		unlockMe(this);
		return r;
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

	private SortedMap<Integer,Boolean> getModuleLocks() {
		SortedMap<Integer,Boolean> moduleLocks = new TreeMap<Integer,Boolean>();
		int m = 0;
		for (Module mod: modules) {
			moduleLocks.put(m,mod.isLocked());
			m++;
		}
		return moduleLocks;
		
	}
}
