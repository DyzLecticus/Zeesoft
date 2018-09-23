package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;

public class Confabulator extends Locker {
	private Config							configuration		= null;
	private String							name				= "";
	private int								modules				= 0;

	private SortedMap<String,Context>		contexts			= new TreeMap<String,Context>();
	private SortedMap<String,Symbol>		symbols				= new TreeMap<String,Symbol>();
	private SortedMap<String,Link>			links				= new TreeMap<String,Link>();
	private SortedMap<String,List<Link>>	linksByFrom			= new TreeMap<String,List<Link>>();
	private SortedMap<String,List<Link>>	linksByTo			= new TreeMap<String,List<Link>>();

	private SymbolCorrector					corrector			= new SymbolCorrector();
		
	public Confabulator(Config config,String name,int modules) {
		super(config.getMessenger());
		this.configuration = config;
		this.name = name;
		this.modules = modules;
	}
	
	public Config getConfiguration() {
		return configuration;
	}

	public String getName() {
		return name;
	}
	
	public int getModules() {
		return modules;
	}
	
	public void learnSequence(String sequence,String context) {
		learnSequence(new ZStringSymbolParser(sequence),new ZStringSymbolParser(context));
	}
	
	public void learnSequence(ZStringSymbolParser sequence,ZStringSymbolParser context) {
		List<String> symbols = sequence.toSymbolsPunctuated();
		List<String> contextSymbols = context.toSymbols();
		if (!contextSymbols.contains("")) {
			contextSymbols.add("");
		}
		lockMe(this);
		for (String symbol: contextSymbols) {
			learnContextNoLock(symbol);
		}
		for (int s = 0; s < symbols.size(); s++) {
			String symbolFrom = symbols.get(s);
			learnSymbolNoLock(symbolFrom,contextSymbols);
			
			int end = s + modules;
			if (end > symbols.size()) {
				end = symbols.size();
			}
			for (int n = s + 1; n < end; n++) {
				int distance = n - s;
				String symbolTo = symbols.get(n);
				learnLinkNoLock(symbolFrom,distance,symbolTo,contextSymbols);
			}
		}
		unlockMe(this);
	}
	
	public void calculateProbabilities() {
		lockMe(this);
		for (Context ctxt: contexts.values()) {
			ctxt.symbolMinProb = 1D;
			ctxt.symbolMaxProb = 0D;
			ctxt.linkMinProb = 1D;
			ctxt.linkMaxProb = 0D;
		}
		for (Symbol sym: symbols.values()) {
			Context ctxt = contexts.get(sym.context);
			sym.prob = ((double)sym.count / (double)ctxt.symbolTotalCount);
			if (sym.prob<ctxt.symbolMinProb) {
				ctxt.symbolMinProb = sym.prob;
			}
			if (sym.prob>ctxt.symbolMaxProb) {
				ctxt.symbolMaxProb = sym.prob;
			}
		}
		for (Link lnk: links.values()) {
			Context ctxt = contexts.get(lnk.context);
			lnk.prob = ((double)lnk.count / (double)ctxt.linkTotalCount);
			if (lnk.prob<ctxt.linkMinProb) {
				ctxt.linkMinProb = lnk.prob;
			}
			if (lnk.prob>ctxt.linkMaxProb) {
				ctxt.linkMaxProb = lnk.prob;
			}
		}
		for (Context ctxt: contexts.values()) {
			ctxt.symbolBandwidth = ((ctxt.symbolMaxProb - ctxt.symbolMinProb) / 2D);
			ctxt.linkBandwidth = ((ctxt.linkMaxProb - ctxt.linkMinProb) / 2D);
			ctxt.symbolToLinkBandwidthFactor = ctxt.linkBandwidth / ctxt.symbolBandwidth;
		}
		unlockMe(this);
	}
	
	public void confabulate(ConfabulationObject confab) {
		confab.initialize();
		if (confab instanceof CorrectionConfabulation) {
			lockMe(this);
			confabulateCorrectionNoLock((CorrectionConfabulation) confab);
			unlockMe(this);
		}
	}
	
	public Context getContext(String contextSymbol) {
		Context r = null;
		lockMe(this);
		if (contexts.containsKey(contextSymbol)) {
			r = contexts.get(contextSymbol).copy();
		}
		unlockMe(this);
		return r;
	}
	
	protected void learnContextNoLock(String contextSymbol) {
		if (contexts.get(contextSymbol)==null) {
			Context ctxt = new Context();
			ctxt.contextSymbol = contextSymbol;
			contexts.put(contextSymbol,ctxt);
		}
	}
	
	protected void learnSymbolNoLock(String symbol,List<String> contextSymbols) {
		for (String contextSymbol: contextSymbols) {
			Context ctxt = contexts.get(contextSymbol);
			ctxt.symbolTotalCount++;
			Symbol sym = getSymbolNoLock(symbol,contextSymbol);
			if (sym==null) {
				sym = new Symbol();
				sym.symbol = symbol;
				sym.context = contextSymbol;
				symbols.put(sym.getId(),sym);
				ctxt.totalSymbols++;
				ctxt.knownSymbols.add(symbol);
			}
			sym.count++;
		}
	}
	
	protected void learnLinkNoLock(String symbolFrom,int distance,String symbolTo,List<String> contextSymbols) {
		for (String contextSymbol: contextSymbols) {
			Context ctxt = contexts.get(contextSymbol);
			ctxt.linkTotalCount++;
			Link lnk = getLinkNoLock(symbolFrom,distance,contextSymbol,symbolTo);
			if (lnk==null) {
				lnk = new Link();
				lnk.symbolFrom = symbolFrom;
				lnk.symbolTo = symbolTo;
				lnk.distance = distance;
				lnk.context = contextSymbol;
				links.put(lnk.getId(),lnk);
				ctxt.totalLinks++;
				
				List<Link> lst = null;
				String key = "";
				
				key = lnk.symbolFrom + "|" + lnk.distance + "|" + lnk.context;
				lst = linksByFrom.get(key);
				if (lst==null) {
					lst = new ArrayList<Link>();
					linksByFrom.put(key,lst);
				}
				lst.add(lnk);
				
				key = lnk.symbolTo + "|" + lnk.distance + "|" + lnk.context;
				lst = linksByTo.get(key);
				if (lst==null) {
					lst = new ArrayList<Link>();
					linksByTo.put(key,lst);
				}
				lst.add(lnk);
			}
			lnk.count++;
		}
	}
	
	protected Symbol getSymbolNoLock(String symbol,String contextSymbol) {
		return symbols.get(Symbol.getId(symbol,contextSymbol));
	}

	protected Link getLinkNoLock(String symbolFrom,int distance,String contextSymbol,String symbolTo) {
		return links.get(Link.getId(symbolFrom,distance,contextSymbol,symbolTo));
	}

	protected List<Link> getLinksNoLock(String symbolFrom,int distance,String contextSymbol,String symbolTo) {
		List<Link> r = null;
		if (symbolFrom.length()==0) {
			String key = symbolTo + "|" + distance + "|" + contextSymbol;
			r = linksByTo.get(key);
		} else if (symbolTo.length()==0) {
			String key = symbolFrom + "|" + distance + "|" + contextSymbol;
			r = linksByFrom.get(key);
		} else if (symbolFrom.length()>0 && symbolTo.length()>0) {
			r = new ArrayList<Link>();
			Link lnk = links.get(Link.getId(symbolFrom,distance,contextSymbol,symbolTo));
			if (lnk!=null) {
				r.add(lnk);
			}
		}
		if (r==null) {
			r = new ArrayList<Link>();
		}
		return r;
	}

	protected void confabulateCorrectionNoLock(CorrectionConfabulation confab) {
		Context ctxt = contexts.get(confab.contextSymbol);
		int unknownSymbols = 0;
		for (String symbol: confab.symbols) {
			Symbol sym = getSymbolNoLock(symbol,confab.contextSymbol);
			if (sym==null) {
				unknownSymbols++;
			}
		}
		for (int s = 0; s < confab.symbols.size(); s++) {
			String symbol = confab.symbols.get(s);
			Module mod = confab.modules.get(s);
			Symbol sym = getSymbolNoLock(symbol,confab.contextSymbol);
			if (sym!=null) {
				ModuleSymbol modSym = new ModuleSymbol();
				modSym.symbol = symbol;
				modSym.prob = (ctxt.linkMaxProb - ctxt.linkMinProb);
				mod.symbols.put(symbol,modSym);
			} else {
				long stopAfterMs = (confab.maxTime / (long) unknownSymbols) / 2;
				List<String> vars = corrector.getVariations(symbol,ctxt.knownSymbols,confab.started,stopAfterMs,confab.alphabet);
				for (String var: vars) {
					sym = getSymbolNoLock(var,confab.contextSymbol);
					ModuleSymbol modSym = new ModuleSymbol();
					modSym.symbol = var;
					modSym.prob = ctxt.linkBandwidth;
					mod.symbols.put(var,modSym);
				}
			}
		}
		logModuleStateNoLock(confab,"Initialized module symbol variations");

		if (confab.validate) {
			confabulateNoLock(confab,ctxt,false);
			logModuleStateNoLock(confab,"Initialized module symbol expectations");
		}
		
		if (confab.validate || !checkDoneNoLock(confab,ctxt)) {
			while(!confabulateReturnDoneNoLock(confab,ctxt)) {
				if ((new Date()).getTime() > (confab.started.getTime() + confab.maxTime)) {
					break;
				}
			}
		}
		
		List<String> newSyms = new ArrayList<String>();
		for (int m = 0; m < confab.modules.size(); m++) {
			String symbol = confab.symbols.get(m);
			String correction = "";
			Module mod = confab.modules.get(m);
			if (mod.symbols.size()>1) {
				List<ModuleSymbol> syms = mod.getSymbols();
				if (syms.get(1).prob < syms.get(0).prob) {
					syms.get(0).prob = 1D;
					mod.symbols.clear();
					mod.symbols.put(syms.get(0).symbol,syms.get(0));
					mod.locked = true;
				}
			}
			if (mod.locked && mod.symbols.size()==1) {
				correction = mod.symbols.firstKey();
			}
			if (correction.length()>0 && !correction.equals(symbol)) {
				Correction cor = new Correction();
				cor.index = m;
				cor.symbol = symbol;
				cor.correction = correction;
				confab.corrections.add(cor);
				newSyms.add(correction);
			} else {
				newSyms.add(symbol);
			}
		}
		confab.corrected.fromSymbols(newSyms,true,true);
	}

	protected boolean checkDoneNoLock(ConfabulationObject confab,Context ctxt) {
		boolean done = false;
		int locked = 0;
		for (int m = 0; m < confab.modules.size(); m++) {
			Module mod = confab.modules.get(m);
			if (mod.symbols.size()>1) {
				List<ModuleSymbol> syms = mod.getSymbols();
				if (syms.get(1).prob < syms.get(0).prob) {
					syms.get(0).prob = 1D;
					mod.symbols.clear();
					mod.symbols.put(syms.get(0).symbol,syms.get(0));
					mod.locked = true;
				}
			} else if (mod.symbols.size()==1) {
				mod.symbols.get(mod.symbols.firstKey()).prob = 1D;
				mod.locked = true;
			}
			if (mod.locked) {
				locked++;
			}
		}
		if (locked==confab.modules.size()) {
			done = true;
		}
		return done;
	}

	protected boolean confabulateReturnDoneNoLock(ConfabulationObject confab,Context ctxt) {
		boolean done = false;
		boolean changed = false;
		List<Module> copyModules = confab.copyModules();
		confabulateNoLock(confab,ctxt,true);
		logModuleStateNoLock(confab,"Confabulated module symbols");
		int locked = 0;
		for (int m = 0; m < confab.modules.size(); m++) {
			Module mod = confab.modules.get(m);
			Module cMod = copyModules.get(m);
			if (!changed && !mod.getSymbolState().equals(cMod.getSymbolState())) {
				changed = true;
			}
			if (mod.locked) {
				locked++;
			}
		}
		if (!changed || locked==confab.modules.size()) {
			done = true;
		}
		return done;
	}

	protected void confabulateNoLock(ConfabulationObject confab,Context ctxt,boolean strict) {
		for (int m = 0; m < confab.modules.size(); m++) {
			Module mod = confab.modules.get(m);
			if (!mod.locked) {
				List<ModuleSymbol> modSymbols = mod.getSymbols();
				if (modSymbols.size()==0) {
					modSymbols.add(new ModuleSymbol());
				}
				List<ModuleSymbol> fired = new ArrayList<ModuleSymbol>();
				double maxProb = 0D;
				for (ModuleSymbol modSym: modSymbols) {
					int start = (m - modules);
					int end = (m + modules);
					if (start < 0) {
						start = 0;
					}
					if (end > confab.modules.size()) {
						end = confab.modules.size();
					}
					for (int c = start; c < end; c++) {
						if (c!=m) {
							Module oMod = confab.modules.get(c);
							List<ModuleSymbol> oModSymbols = oMod.getSymbols();
							for (ModuleSymbol oModSym: oModSymbols) {
							
								String symbolFrom = "";
								String symbolTo = "";
								int distance = 0;
								if (c<m) {
									distance = m - c;
									symbolFrom = oModSym.symbol;
									if (strict) {
										symbolTo = modSym.symbol;
									}
								} else {
									distance = c - m;
									if (strict) {
										symbolFrom = modSym.symbol;
									}
									symbolTo = oModSym.symbol;
								}
								
								List<Link> lnks = getLinksNoLock(symbolFrom, distance, ctxt.contextSymbol, symbolTo);
								for (Link lnk: lnks) {
									String symbol = "";
									double prob = 0D;
									if (c<m) {
										symbol = lnk.symbolTo;
									} else {
										symbol = lnk.symbolFrom;
									}
									prob += ctxt.linkBandwidth + (ctxt.linkMaxProb - lnk.prob);
									
									modSym = mod.symbols.get(symbol);
									if (modSym==null) {
										modSym = new ModuleSymbol();
										modSym.symbol = symbol;
										mod.symbols.put(symbol,modSym);
									}
									modSym.prob += prob;
									if (!fired.contains(modSym)) {
										fired.add(modSym);
									}
									if (modSym.prob>maxProb) {
										maxProb = modSym.prob;
									}
								}
							}
						}
					}
				}
				if (strict) {
					List<ModuleSymbol> tModSymbols = new ArrayList<ModuleSymbol>(mod.symbols.values());
					for (ModuleSymbol tModSymbol: tModSymbols) {
						if (fired.contains(tModSymbol)) {
							if (tModSymbol.prob<(maxProb - ctxt.linkBandwidth)) {
								mod.symbols.remove(tModSymbol.symbol);
							}
						} else {
							mod.symbols.remove(tModSymbol.symbol);
						}
					}
					if (mod.symbols.size()==1) {
						mod.symbols.get(mod.symbols.firstKey()).prob = 1D;
						mod.locked = true;
					}
				}
			}
		}
	}
	
	protected void logModuleStateNoLock(ConfabulationObject confab,String logLine) {
		if (confab.appendLog) {
			ZStringBuilder line = new ZStringBuilder(logLine);
			line.append(";\n");
			line.append(getDebugModuleSymbolsNoLock(confab.modules));
			confab.addLogLine(line);
		}
	}
	
	protected ZStringBuilder getDebugModuleSymbolsNoLock(List<Module> modules) {
		ZStringBuilder r = new ZStringBuilder("");
		int m = 0;
		for (Module mod: modules) {
			m++;
			r.append("  ");
			r.append(String.format("%02d",m) + ":");
			List<ModuleSymbol> syms = mod.getSymbols();
			int s = 0;
			for (ModuleSymbol modSym: syms) {
				r.append(" ");
				r.append(modSym.symbol);
				r.append(":");
				r.append("" + modSym.prob);
				s++;
				if (s>=4) {
					if (syms.size()>s) {
						r.append(" [");
						r.append("" + (syms.size() - s));
						r.append("]");
					}
					break;
				}
			}
			r.append("\n");
		}
		return r;
	}
}
