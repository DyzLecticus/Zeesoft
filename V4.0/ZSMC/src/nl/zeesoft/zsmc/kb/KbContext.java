package nl.zeesoft.zsmc.kb;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class KbContext {
	public String							contextSymbol				= "";
		
	public int								symbolTotalCount			= 0;
	public int								totalSymbols				= 0;
	public double							symbolMaxProb				= 0D;
	public double							symbolMinProb				= 0D;
	
	public int								linkTotalCount				= 0;
	public int								totalLinks					= 0;
	public double							linkMaxProb					= 0D;
	public double							linkMinProb					= 0D;
	
	public double							symbolBandwidth				= 0D;
	public double							linkBandwidth				= 0D;
	public double							symbolToLinkBandwidthFactor	= 0D;
	
	public List<String>						knownSymbols				= new ArrayList<String>();
	
	public SortedMap<String,List<KbLink>>	linksByFrom					= new TreeMap<String,List<KbLink>>();
	public SortedMap<String,List<KbLink>>	linksByTo					= new TreeMap<String,List<KbLink>>();

	public SortedMap<String,List<KbSymbol>>	symbolsUC					= new TreeMap<String,List<KbSymbol>>();
	public SortedMap<String,List<KbLink>>	linksUC						= new TreeMap<String,List<KbLink>>();
	public SortedMap<String,List<KbLink>>	linksUCByFrom				= new TreeMap<String,List<KbLink>>();
	public SortedMap<String,List<KbLink>>	linksUCByTo					= new TreeMap<String,List<KbLink>>();
	
	public KbContext copy() {
		KbContext r = new KbContext();
		r.contextSymbol = this.contextSymbol;
		r.symbolTotalCount = this.symbolTotalCount;
		r.totalSymbols = this.totalSymbols;
		r.symbolMaxProb = this.symbolMaxProb;
		r.symbolMinProb = this.symbolMinProb;
		r.linkTotalCount = this.linkTotalCount;
		r.totalLinks = this.totalLinks;
		r.linkMaxProb = this.linkMaxProb;
		r.linkMinProb = this.linkMinProb;
		r.symbolBandwidth = this.symbolBandwidth;
		r.linkBandwidth = this.linkBandwidth;
		r.symbolToLinkBandwidthFactor = this.symbolToLinkBandwidthFactor;
		r.knownSymbols = new ArrayList<String>(this.knownSymbols);
		return r;
	}
	
	protected void addSymbol(KbSymbol sym) {
		addSymbolToMap(symbolsUC,sym,sym.symbol.toUpperCase());
	}
	
	protected void addLink(KbLink lnk) {
		addLinkToMap(linksByFrom,lnk,lnk.symbolFrom + "|" + lnk.distance);
		addLinkToMap(linksByTo,lnk,lnk.symbolTo + "|" + lnk.distance);
		addLinkToMap(linksUC,lnk,lnk.symbolFrom.toUpperCase() + "|" + lnk.distance + "|" + lnk.symbolTo.toUpperCase());
		addLinkToMap(linksUCByFrom,lnk,lnk.symbolFrom.toUpperCase() + "|" + lnk.distance);
		addLinkToMap(linksUCByTo,lnk,lnk.symbolTo.toUpperCase() + "|" + lnk.distance);
	}
	
	private void addSymbolToMap(SortedMap<String,List<KbSymbol>> map, KbSymbol sym,String id) {
		List<KbSymbol> syms = map.get(id);
		if (syms==null) {
			syms = new ArrayList<KbSymbol>();
			map.put(id,syms);
		}
		if (!syms.contains(sym)) {
			syms.add(sym);
		}
	}

	private void addLinkToMap(SortedMap<String,List<KbLink>> map, KbLink lnk,String id) {
		List<KbLink> lnks = map.get(id);
		if (lnks==null) {
			lnks = new ArrayList<KbLink>();
			map.put(id,lnks);
		}
		if (!lnks.contains(lnk)) {
			lnks.add(lnk);
		}
	}
}
