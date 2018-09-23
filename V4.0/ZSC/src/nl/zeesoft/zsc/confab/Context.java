package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Context {
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
	
	public SortedMap<String,List<Link>>		linksByFrom					= new TreeMap<String,List<Link>>();
	public SortedMap<String,List<Link>>		linksByTo					= new TreeMap<String,List<Link>>();

	public SortedMap<String,List<Symbol>>	symbolsUC					= new TreeMap<String,List<Symbol>>();
	public SortedMap<String,List<Link>>		linksUC						= new TreeMap<String,List<Link>>();
	public SortedMap<String,List<Link>>		linksUCByFrom				= new TreeMap<String,List<Link>>();
	public SortedMap<String,List<Link>>		linksUCByTo					= new TreeMap<String,List<Link>>();
	
	public Context copy() {
		Context r = new Context();
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
	
	public void addSymbol(Symbol sym) {
		List<Symbol> syms = symbolsUC.get(sym.symbol.toUpperCase());
		if (syms==null) {
			syms = new ArrayList<Symbol>();
			symbolsUC.put(sym.symbol.toUpperCase(),syms);
		}
		syms.add(sym);
	}
	
	public void addLink(Link lnk) {
		List<Link> lst = null;
		String key = "";
		
		key = lnk.symbolFrom + "|" + lnk.distance;
		lst = linksByFrom.get(key);
		if (lst==null) {
			lst = new ArrayList<Link>();
			linksByFrom.put(key,lst);
		}
		lst.add(lnk);
		
		key = lnk.symbolTo + "|" + lnk.distance;
		lst = linksByTo.get(key);
		if (lst==null) {
			lst = new ArrayList<Link>();
			linksByTo.put(key,lst);
		}
		lst.add(lnk);

		key = lnk.symbolFrom.toUpperCase() + "|" + lnk.distance + "|" + lnk.symbolTo.toUpperCase();
		lst = linksUC.get(key);
		if (lst==null) {
			lst = new ArrayList<Link>();
			linksUC.put(key,lst);
		}
		lst.add(lnk);
		
		key = lnk.symbolFrom.toUpperCase() + "|" + lnk.distance;
		lst = linksUCByFrom.get(key);
		if (lst==null) {
			lst = new ArrayList<Link>();
			linksUCByFrom.put(key,lst);
		}
		lst.add(lnk);
		
		key = lnk.symbolTo.toUpperCase() + "|" + lnk.distance;
		lst = linksUCByTo.get(key);
		if (lst==null) {
			lst = new ArrayList<Link>();
			linksUCByTo.put(key,lst);
		}
		lst.add(lnk);
	}
}
