package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsc.confab.confabs.ConfabulationObject;
import nl.zeesoft.zsc.confab.confabs.ContextConfabulation;
import nl.zeesoft.zsc.confab.confabs.ContextResult;
import nl.zeesoft.zsc.confab.confabs.Correction;
import nl.zeesoft.zsc.confab.confabs.CorrectionConfabulation;
import nl.zeesoft.zsc.confab.confabs.ExtensionConfabulation;

public class Confabulator extends Locker {
	private SymbolCorrector					corrector			= new SymbolCorrector();
	private ZIntegerGenerator				generator			= new ZIntegerGenerator(1,100);
	
	private Config							configuration		= null;
	private String							name				= "";
	private int								maxDistance			= 0;

	private SortedMap<String,Context>		contexts			= new TreeMap<String,Context>();
	private SortedMap<String,Symbol>		symbols				= new TreeMap<String,Symbol>();
	private SortedMap<String,Link>			links				= new TreeMap<String,Link>();

	private SortedMap<String,List<Symbol>>	symbolsNoContext	= new TreeMap<String,List<Symbol>>();
	private SortedMap<String,List<Link>>	linksNoContext		= new TreeMap<String,List<Link>>();
	private SortedMap<String,List<Symbol>>	symbolsNoContextUC	= new TreeMap<String,List<Symbol>>();
	private SortedMap<String,List<Link>>	linksNoContextUC	= new TreeMap<String,List<Link>>();
	
	public Confabulator(Config config,String name,int maxDistance) {
		super(config.getMessenger());
		this.configuration = config;
		this.name = name;
		this.maxDistance = maxDistance;
		learnContextNoLock("");
	}
	
	public Config getConfiguration() {
		return configuration;
	}

	public String getName() {
		return name;
	}
	
	public int getMaxDistance() {
		return maxDistance;
	}
	
	public void learnSequence(String sequence) {
		learnSequence(sequence,"");
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
			
			int end = s + (maxDistance + 1);
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
		Context def = getDefaultContextNoLock();
		for (Context ctxt: contexts.values()) {
			ctxt.symbolBandwidth = ((ctxt.symbolMaxProb - ctxt.symbolMinProb) / 2D);
			ctxt.linkBandwidth = ((ctxt.linkMaxProb - ctxt.linkMinProb) / 2D);
			if (ctxt.symbolBandwidth==0D && ctxt!=def) {
				ctxt.symbolBandwidth = def.symbolBandwidth;
			}
			if (ctxt.linkBandwidth==0D && ctxt!=def) {
				ctxt.linkBandwidth = def.linkBandwidth;
			}
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
		} else if (confab instanceof ContextConfabulation) {
			lockMe(this);
			confabulateContextNoLock((ContextConfabulation) confab);
			unlockMe(this);
		} else if (confab instanceof ExtensionConfabulation) {
			lockMe(this);
			confabulateExtensionNoLock((ExtensionConfabulation) confab);
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
	
	public List<Link> getLinks(String symbolFrom,int distance,String contextSymbol,String symbolTo,boolean caseSensitive) {
		List<Link> r = null;
		lockMe(this);
		r = getLinksNoLock(symbolFrom,distance,contextSymbol,symbolTo,caseSensitive);
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
				ctxt.addSymbol(sym);
				
				List<Symbol> syms = null;
				syms = symbolsNoContext.get(sym.symbol);
				if (syms==null) {
					syms = new ArrayList<Symbol>();
					symbolsNoContext.put(sym.symbol,syms);
				}
				syms.add(sym);
				syms = symbolsNoContextUC.get(sym.symbol.toUpperCase());
				if (syms==null) {
					syms = new ArrayList<Symbol>();
					symbolsNoContextUC.put(sym.symbol.toUpperCase(),syms);
				}
				syms.add(sym);
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
				ctxt.addLink(lnk);

				List<Link> lst = null;
				String key = "";
				key = lnk.symbolFrom + "|" + lnk.distance + "|" + lnk.symbolTo;
				lst = linksNoContext.get(key);
				if (lst==null) {
					lst = new ArrayList<Link>();
					linksNoContext.put(key,lst);
				}
				lst.add(lnk);
				key = lnk.symbolFrom.toUpperCase() + "|" + lnk.distance + "|" + lnk.symbolTo.toUpperCase();
				lst = linksNoContextUC.get(key);
				if (lst==null) {
					lst = new ArrayList<Link>();
					linksNoContextUC.put(key,lst);
				}
				lst.add(lnk);
			}
			lnk.count++;
		}
	}
	
	protected Context getDefaultContextNoLock() {
		return getContextNoLock("");
	}
	
	protected Context getContextNoLock(String contextSymbol) {
		return contexts.get(contextSymbol);
	}
	
	protected Symbol getSymbolNoLock(String symbol,String contextSymbol) {
		return symbols.get(Symbol.getId(symbol,contextSymbol));
	}
	
	protected List<Symbol> getSymbolsNoLock(String symbol,boolean caseSensitive) {
		return getSymbolsNoLock(symbol,null,caseSensitive);
	}
	
	protected List<Symbol> getSymbolsNoLock(String symbol,String contextSymbol,boolean caseSensitive) {
		List<Symbol> r = null;
		if (contextSymbol!=null) {
			if (caseSensitive) {
				Symbol sym = symbols.get(Symbol.getId(symbol,contextSymbol));
				if (sym!=null) {
					r = new ArrayList<Symbol>();
					r.add(sym);
				}
			} else {
				Context ctxt = contexts.get(contextSymbol);
				if (ctxt!=null) {
					r = ctxt.symbolsUC.get(symbol.toUpperCase());
				}
			}
		} else {
			if (caseSensitive) {
				r = symbolsNoContext.get(symbol);
			} else {
				r = symbolsNoContextUC.get(symbol.toUpperCase());
			}
		}
		if (r==null) {
			r = new ArrayList<Symbol>();
		}
		return r;
	}

	protected Link getLinkNoLock(String symbolFrom,int distance,String contextSymbol,String symbolTo) {
		return links.get(Link.getId(symbolFrom,distance,contextSymbol,symbolTo));
	}
	
	protected List<Link> getLinksNoLock(String symbolFrom,int distance,String symbolTo,boolean caseSensitive) {
		return getLinksNoLock(symbolFrom,distance,null,symbolTo,caseSensitive);
	}

	protected List<Link> getLinksNoLock(String symbolFrom,int distance,String contextSymbol,String symbolTo,boolean caseSensitive) {
		List<Link> r = null;
		if (contextSymbol!=null) {
			Context ctxt = contexts.get(contextSymbol);
			if (caseSensitive) {
				if (symbolFrom.length()==0) {
					if (ctxt!=null) {
						String key = symbolTo + "|" + distance;
						r = ctxt.linksByTo.get(key);
					}
				} else if (symbolTo.length()==0) {
					if (ctxt!=null) {
						String key = symbolFrom + "|" + distance;
						r = ctxt.linksByFrom.get(key);
					}
				} else if (symbolFrom.length()>0 && symbolTo.length()>0) {
					Link lnk = links.get(Link.getId(symbolFrom,distance,contextSymbol,symbolTo));
					if (lnk!=null) {
						r = new ArrayList<Link>();
						r.add(lnk);
					}
				}
			} else {
				if (symbolFrom.length()==0) {
					if (ctxt!=null) {
						String key = symbolTo.toUpperCase() + "|" + distance;
						r = ctxt.linksUCByTo.get(key);
					}
				} else if (symbolTo.length()==0) {
					if (ctxt!=null) {
						String key = symbolFrom.toUpperCase() + "|" + distance;
						r = ctxt.linksUCByFrom.get(key);
					}
				} else if (symbolFrom.length()>0 && symbolTo.length()>0) {
					if (ctxt!=null) {
						String key = symbolFrom.toUpperCase() + "|" + distance + "|" + symbolTo.toUpperCase();
						r = ctxt.linksUC.get(key);
					}
				}
			}
		} else {
			if (caseSensitive) {
				String key = symbolFrom + "|" + distance + "|" + symbolTo;
				r = linksNoContext.get(key);
			} else {
				String key = symbolFrom.toUpperCase() + "|" + distance + "|" + symbolTo.toUpperCase();
				r = linksNoContextUC.get(key);
			}
		}
		if (r==null) {
			r = new ArrayList<Link>();
		}
		return r;
	}

	protected void confabulateContextNoLock(ContextConfabulation confab) {
		if (contexts.size()>1) {
			Context def = getDefaultContextNoLock();
			Module mod = confab.modules.get(0);
			double maxProb = 0D;
			boolean[] linked = new boolean[confab.symbols.size()];
			for (int s = 0; s < confab.symbols.size(); s++) {
				linked[s] = false;
			}
			for (int s = 0; s < confab.symbols.size(); s++) {
				String symbolFrom = confab.symbols.get(s);
				int start = (s + 1);
				int end = start + maxDistance;
				if (end > confab.symbols.size()) {
					end = confab.symbols.size();
				}
				for (int n = start; n < end; n++) {
					String symbolTo = confab.symbols.get(n);
					int distance = ((n - start) + 1);
					List<Link> lnks = getLinksNoLock(symbolFrom,distance,symbolTo,confab.caseSensitive);
					for (Link lnk: lnks) {
						if (lnk.context.length()>0) {
							linked[s] = true;
							linked[n] = true;
							Link defLink = getLinkNoLock(lnk.symbolFrom,lnk.distance,"",lnk.symbolTo);
							double prob = def.linkBandwidth + (def.linkMaxProb - defLink.prob);
							if (prob>0D) {
								ModuleSymbol modSym = fireModuleSymbolNoLock(confab,mod,lnk.context,prob,null);
								if (modSym.prob>maxProb) {
									maxProb = modSym.prob;
								}
							}
						}
					}
				}
			}
			for (int s = 0; s < confab.symbols.size(); s++) {
				if (!linked[s]) {
					String symbol = confab.symbols.get(s);
					List<Symbol> syms = getSymbolsNoLock(symbol,confab.caseSensitive);
					for (Symbol sym: syms) {
						if (sym.context.length()>0) {
							Symbol defSymbol = getSymbolNoLock(sym.symbol,"");
							double prob = (def.symbolBandwidth + (def.symbolMaxProb - defSymbol.prob)) * def.symbolToLinkBandwidthFactor;
							if (prob>0D) {
								ModuleSymbol modSym = fireModuleSymbolNoLock(confab,mod,sym.context,prob,null);
								if (modSym.prob>maxProb) {
									maxProb = modSym.prob;
								}
							}
						}
					}
				}
			}
			if (mod.symbols.size()>0) {
				List<ModuleSymbol> syms = mod.getSymbols();
				for (ModuleSymbol mSym: syms) {
					ContextResult result = new ContextResult();
					result.contextSymbol = mSym.symbol;
					result.prob = mSym.prob;
					result.probNormalized = result.prob / maxProb;
					confab.results.add(result);
				}
			}
			logModuleStateNoLock(confab,"Confabulated context");
		} else {
			addLogLineNoLock(confab,"No contexts to confabulate");
		}
	}

	protected void confabulateCorrectionNoLock(CorrectionConfabulation confab) {
		Context ctxt = contexts.get(confab.contextSymbol);
		if (ctxt==null) {
			ctxt = contexts.get("");
		}
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
			List<Module> copyModules = null;
			if (confab.parallel) {
				copyModules = confab.copyModules();
			}
			confabulateNoLock(confab,ctxt,false,copyModules);
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

	protected void confabulateExtensionNoLock(ExtensionConfabulation confab) {
		Context ctxt = contexts.get(confab.contextSymbol);
		if (ctxt==null) {
			ctxt = contexts.get("");
		}
		for (int s = 0; s < confab.symbols.size(); s++) {
			String symbol = confab.symbols.get(s);
			Module mod = confab.modules.get(s);
			Symbol sym = getSymbolNoLock(symbol,confab.contextSymbol);
			if (sym!=null) {
				ModuleSymbol modSym = new ModuleSymbol();
				modSym.symbol = symbol;
				modSym.prob = 1D;
				mod.symbols.put(symbol,modSym);
			}
			mod.locked = true;
		}
		logModuleStateNoLock(confab,"Initialized module symbols");

		List<Module> copyModules = null;
		if (confab.parallel) {
			copyModules = confab.copyModules();
		}
		confabulateNoLock(confab,ctxt,false,copyModules);
		logModuleStateNoLock(confab,"Initialized module symbol expectations");
		
		while(!confabulateReturnDoneNoLock(confab,ctxt)) {
			if ((new Date()).getTime() > (confab.started.getTime() + confab.maxTime)) {
				break;
			}
		}
		
		List<String> extSyms = new ArrayList<String>();
		for (int m = 0; m < confab.modules.size(); m++) {
			if (m>=confab.symbols.size()) {
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
					extSyms.add(mod.symbols.firstKey());
				} else {
					break;
				}
			}
		}
		confab.extension.fromSymbols(extSyms,false,true);
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
		confabulateNoLock(confab,ctxt,true,copyModules);
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

	protected void confabulateNoLock(ConfabulationObject confab,Context ctxt,boolean strict,List<Module> sourceModules) {
		if (sourceModules==null) {
			sourceModules = confab.modules;
		}
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
					int start = (m - maxDistance);
					int end = (m + maxDistance);
					if (start < 0) {
						start = 0;
					}
					if (end > confab.modules.size()) {
						end = confab.modules.size();
					}
					for (int c = start; c < end; c++) {
						if (c!=m) {
							Module oMod = sourceModules.get(c);
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
								List<Link> lnks = getLinksNoLock(symbolFrom,distance,ctxt.contextSymbol,symbolTo,confab.caseSensitive);
								fireLinksInModuleNoLock(confab,ctxt,mod,lnks,(c<m),fired);
							}
						}
					}
				}
				if (strict) {
					if (fired.size()==0 && mod.symbols.size()>1) {
						fireSymbolsInModuleNoLock(confab,ctxt,mod,fired);
					}
					
					for (ModuleSymbol modSym: fired) {
						if (modSym.prob>maxProb) {
							maxProb = modSym.prob;
						}
					}
					
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

	protected void fireLinksInModuleNoLock(ConfabulationObject confab,Context ctxt,Module mod,List<Link> lnks,boolean forward,List<ModuleSymbol> fired) {
		for (Link lnk: lnks) {
			fireLinkInModuleNoLock(confab,ctxt,mod,lnk,forward,fired);
		}
	}

	protected void fireLinkInModuleNoLock(ConfabulationObject confab,Context ctxt,Module mod,Link lnk, boolean forward,List<ModuleSymbol> fired) {
		String symbol = "";
		double prob = 0D;
		if (forward) {
			symbol = lnk.symbolTo;
		} else {
			symbol = lnk.symbolFrom;
		}
		prob += ctxt.linkBandwidth + (ctxt.linkMaxProb - lnk.prob);
		fireModuleSymbolNoLock(confab,mod,symbol,prob,fired);
	}
	
	protected void fireSymbolsInModuleNoLock(ConfabulationObject confab,Context ctxt,Module mod,List<ModuleSymbol> fired) {
		for (ModuleSymbol modSym: mod.symbols.values()) {
			List<Symbol> syms = getSymbolsNoLock(modSym.symbol,ctxt.contextSymbol,confab.caseSensitive);
			for (Symbol sym: syms) {
				double prob = ((ctxt.symbolBandwidth + (ctxt.symbolMaxProb - sym.prob)) * ctxt.symbolToLinkBandwidthFactor);
				fireModuleSymbolNoLock(confab,modSym,prob,fired);
			}
		}
	}

	protected ModuleSymbol fireModuleSymbolNoLock(ConfabulationObject confab,Module mod,String symbol,double prob,List<ModuleSymbol> fired) {
		ModuleSymbol modSym = null;
		if (prob>0D) {
			modSym = mod.symbols.get(symbol);
			if (modSym==null) {
				modSym = new ModuleSymbol();
				modSym.symbol = symbol;
				mod.symbols.put(symbol,modSym);
			}
			fireModuleSymbolNoLock(confab,modSym,prob,fired);
		}
		return modSym;
	}

	protected void fireModuleSymbolNoLock(ConfabulationObject confab,ModuleSymbol modSym,double prob,List<ModuleSymbol> fired) {
		if (prob>0D) {
			if (confab.noise>0D) {
				int random = generator.getNewInteger();
				double noise = (confab.noise / 100D) * (double)random;
				prob += (prob * noise);
			}
			modSym.prob += prob;
			if (fired!=null && !fired.contains(modSym)) {
				fired.add(modSym);
			}
		}
	}
	
	protected void addLogLineNoLock(ConfabulationObject confab,String logLine) {
		if (confab.appendLog) {
			confab.addLogLine(logLine);
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
