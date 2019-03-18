package nl.zeesoft.zsmc.kb;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class KnowledgeBase extends Locker {
	private int									maxDistance			= 0;

	private SortedMap<String,KbContext>			contexts			= new TreeMap<String,KbContext>();
	private SortedMap<String,KbSymbol>			symbols				= new TreeMap<String,KbSymbol>();
	private SortedMap<String,KbLink>			links				= new TreeMap<String,KbLink>();

	private SortedMap<String,List<KbSymbol>>	symbolsNoContext	= new TreeMap<String,List<KbSymbol>>();
	private SortedMap<String,List<KbLink>>		linksNoContext		= new TreeMap<String,List<KbLink>>();
	private SortedMap<String,List<KbSymbol>>	symbolsNoContextUC	= new TreeMap<String,List<KbSymbol>>();
	private SortedMap<String,List<KbLink>>		linksNoContextUC	= new TreeMap<String,List<KbLink>>();
	
	public KnowledgeBase(Messenger msgr,int maxDistance) {
		super(msgr);
		this.maxDistance = maxDistance;
		learnContextNoLock("");
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
				if (!symbolFrom.equals(symbolTo)) {
					learnLinkNoLock(symbolFrom,distance,symbolTo,contextSymbols);
				}
			}
		}
		unlockMe(this);
	}
	
	public void calculateProbabilities() {
		lockMe(this);
		for (KbContext ctxt: contexts.values()) {
			ctxt.symbolMinProb = 1D;
			ctxt.symbolMaxProb = 0D;
			ctxt.linkMinProb = 1D;
			ctxt.linkMaxProb = 0D;
		}
		for (KbSymbol sym: symbols.values()) {
			KbContext ctxt = contexts.get(sym.context);
			sym.prob = ((double)sym.count / (double)ctxt.symbolTotalCount);
			if (sym.prob<ctxt.symbolMinProb) {
				ctxt.symbolMinProb = sym.prob;
			}
			if (sym.prob>ctxt.symbolMaxProb) {
				ctxt.symbolMaxProb = sym.prob;
			}
		}
		for (KbLink lnk: links.values()) {
			KbContext ctxt = contexts.get(lnk.context);
			lnk.prob = ((double)lnk.count / (double)ctxt.linkTotalCount);
			if (lnk.prob<ctxt.linkMinProb) {
				ctxt.linkMinProb = lnk.prob;
			}
			if (lnk.prob>ctxt.linkMaxProb) {
				ctxt.linkMaxProb = lnk.prob;
			}
		}
		KbContext def = getDefaultContextNoLock();
		for (KbContext ctxt: contexts.values()) {
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
	
	public KbContext getContext(String contextSymbol) {
		KbContext r = null;
		lockMe(this);
		if (contexts.containsKey(contextSymbol)) {
			r = contexts.get(contextSymbol).copy();
		}
		unlockMe(this);
		return r;
	}

	public SortedMap<String,KbContext> getContexts() {
		SortedMap<String,KbContext> r = new TreeMap<String,KbContext>();
		lockMe(this);
		for (KbContext ctxt: contexts.values()) {
			r.put(ctxt.contextSymbol,ctxt.copy());
		}
		unlockMe(this);
		return r;
	}

	public List<KbLink> getLinks(String symbolFrom,int distance,String contextSymbol,String symbolTo,boolean caseSensitive) {
		List<KbLink> r = new ArrayList<KbLink>();
		lockMe(this);
		List<KbLink> internal = getLinksNoLock(symbolFrom,distance,contextSymbol,symbolTo,caseSensitive);
		for (KbLink link: internal) {
			r.add(link.copy());
		}
		unlockMe(this);
		return r;
	}
	
	protected void learnContextNoLock(String contextSymbol) {
		if (contexts.get(contextSymbol)==null) {
			KbContext ctxt = new KbContext();
			ctxt.contextSymbol = contextSymbol;
			contexts.put(contextSymbol,ctxt);
		}
	}
	
	protected void learnSymbolNoLock(String symbol,List<String> contextSymbols) {
		for (String contextSymbol: contextSymbols) {
			KbContext ctxt = contexts.get(contextSymbol);
			ctxt.symbolTotalCount++;
			KbSymbol sym = getSymbolNoLock(symbol,contextSymbol);
			if (sym==null) {
				sym = new KbSymbol();
				sym.symbol = symbol;
				sym.context = contextSymbol;
				symbols.put(sym.getId(),sym);
				ctxt.totalSymbols++;
				ctxt.knownSymbols.add(symbol);
				ctxt.addSymbol(sym);
				
				List<KbSymbol> syms = null;
				syms = symbolsNoContext.get(sym.symbol);
				if (syms==null) {
					syms = new ArrayList<KbSymbol>();
					symbolsNoContext.put(sym.symbol,syms);
				}
				syms.add(sym);
				syms = symbolsNoContextUC.get(sym.symbol.toUpperCase());
				if (syms==null) {
					syms = new ArrayList<KbSymbol>();
					symbolsNoContextUC.put(sym.symbol.toUpperCase(),syms);
				}
				syms.add(sym);
			}
			sym.count++;
		}
	}
	
	protected void learnLinkNoLock(String symbolFrom,int distance,String symbolTo,List<String> contextSymbols) {
		for (String contextSymbol: contextSymbols) {
			KbContext ctxt = contexts.get(contextSymbol);
			ctxt.linkTotalCount++;
			KbLink lnk = getLinkNoLock(symbolFrom,distance,contextSymbol,symbolTo);
			if (lnk==null) {
				lnk = new KbLink();
				lnk.symbolFrom = symbolFrom;
				lnk.symbolTo = symbolTo;
				lnk.distance = distance;
				lnk.context = contextSymbol;
				links.put(lnk.getId(),lnk);
				ctxt.totalLinks++;
				ctxt.addLink(lnk);

				List<KbLink> lst = null;
				String key = "";
				key = lnk.symbolFrom + "|" + lnk.distance + "|" + lnk.symbolTo;
				lst = linksNoContext.get(key);
				if (lst==null) {
					lst = new ArrayList<KbLink>();
					linksNoContext.put(key,lst);
				}
				lst.add(lnk);
				key = lnk.symbolFrom.toUpperCase() + "|" + lnk.distance + "|" + lnk.symbolTo.toUpperCase();
				lst = linksNoContextUC.get(key);
				if (lst==null) {
					lst = new ArrayList<KbLink>();
					linksNoContextUC.put(key,lst);
				}
				lst.add(lnk);
			}
			lnk.count++;
		}
	}
	
	protected KbContext getDefaultContextNoLock() {
		return getContextNoLock("");
	}
	
	protected KbContext getContextNoLock(String contextSymbol) {
		return contexts.get(contextSymbol);
	}
	
	protected KbSymbol getSymbolNoLock(String symbol,String contextSymbol) {
		return symbols.get(KbSymbol.getId(symbol,contextSymbol));
	}
	
	protected List<KbSymbol> getSymbolsNoLock(String symbol,boolean caseSensitive) {
		return getSymbolsNoLock(symbol,null,caseSensitive);
	}
	
	protected List<KbSymbol> getSymbolsNoLock(String symbol,String contextSymbol,boolean caseSensitive) {
		List<KbSymbol> r = null;
		if (contextSymbol!=null) {
			if (caseSensitive) {
				KbSymbol sym = symbols.get(KbSymbol.getId(symbol,contextSymbol));
				if (sym!=null) {
					r = new ArrayList<KbSymbol>();
					r.add(sym);
				}
			} else {
				KbContext ctxt = contexts.get(contextSymbol);
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
			r = new ArrayList<KbSymbol>();
		}
		return r;
	}

	protected KbLink getLinkNoLock(String symbolFrom,int distance,String contextSymbol,String symbolTo) {
		return links.get(KbLink.getId(symbolFrom,distance,contextSymbol,symbolTo));
	}
	
	protected List<KbLink> getLinksNoLock(String symbolFrom,int distance,String symbolTo,boolean caseSensitive) {
		return getLinksNoLock(symbolFrom,distance,null,symbolTo,caseSensitive);
	}

	protected List<KbLink> getLinksNoLock(String symbolFrom,int distance,String contextSymbol,String symbolTo,boolean caseSensitive) {
		List<KbLink> r = null;
		if (contextSymbol!=null) {
			KbContext ctxt = contexts.get(contextSymbol);
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
					KbLink lnk = links.get(KbLink.getId(symbolFrom,distance,contextSymbol,symbolTo));
					if (lnk!=null) {
						r = new ArrayList<KbLink>();
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
			r = new ArrayList<KbLink>();
		}
		return r;
	}
}
