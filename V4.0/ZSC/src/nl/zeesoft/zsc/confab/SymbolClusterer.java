package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zodb.Config;

public class SymbolClusterer extends Confabulator {
	private List<SymbolCluster>	clusters	= new ArrayList<SymbolCluster>();
	
	public SymbolClusterer(Config config, String name, int maxDistance) {
		super(config, name, maxDistance);
	}
	
	public void clusterSymbols(String contextSymbol,boolean caseSensitive) {
		lockMe(this);
		clusters.clear();
		
		Context ctxt = getContextNoLock(contextSymbol);
		if (ctxt.totalLinks>0) {
			SortedMap<String,List<Link>> linksPerSymbol = new TreeMap<String,List<Link>>();
			SortedMap<Integer,List<String>> symbolsByCount = new TreeMap<Integer,List<String>>();
			for (String symbol: ctxt.knownSymbols) {
				if (!ZStringSymbolParser.isLineEndSymbol(symbol) &&
					!ZStringSymbolParser.isPunctuationSymbol(symbol)
					) {
					List<Link> links = new ArrayList<Link>();
					int count = 0;
					for (int i = 1; i<= getMaxDistance(); i++) {
						List<Link> linksFrom = getLinksNoLock(symbol,i,contextSymbol,"",caseSensitive);
						for (Link lnk: linksFrom) {
							if (lnk.count>1 && 
								!ZStringSymbolParser.isLineEndSymbol(lnk.symbolTo) &&
								!ZStringSymbolParser.isPunctuationSymbol(lnk.symbolTo)
								) {
								links.add(lnk);
								count += lnk.count;
							}
						}
						List<Link> linksTo = getLinksNoLock("",i,contextSymbol,symbol,caseSensitive);
						for (Link lnk: linksTo) {
							if (lnk.count>1 && 
								!ZStringSymbolParser.isLineEndSymbol(lnk.symbolFrom) &&
								!ZStringSymbolParser.isPunctuationSymbol(lnk.symbolFrom)
								) {
								links.add(lnk);
								count += lnk.count;
							}
						}
					}
					if (links.size()>0) {
						linksPerSymbol.put(symbol,links);
						List<String> syms = symbolsByCount.get(count);
						if (syms==null) {
							syms = new ArrayList<String>();
							syms.add(symbol);
						}
						symbolsByCount.put(count,syms);
					}
				}
			}
			for (Entry<Integer,List<String>> entry: symbolsByCount.entrySet()) {
				ZStringBuilder syms = new ZStringBuilder();
				for (String sym: entry.getValue()) {
					if (syms.length()>0) {
						syms.append(", ");
					}
					syms.append(sym);
				}
				System.out.println(syms + ": " + entry.getKey());
			}

			List<Link> links = getContextNoLock(contextSymbol).getAllLinks(caseSensitive);
			SortedMap<Integer,List<Link>> linksByCount = new TreeMap<Integer,List<Link>>();
			for (Link lnk: links) {
				if (lnk.count>1 &&
					lnk.distance==1 &&
					!ZStringSymbolParser.isLineEndSymbol(lnk.symbolFrom) &&
					!ZStringSymbolParser.isLineEndSymbol(lnk.symbolTo) &&
					!ZStringSymbolParser.isPunctuationSymbol(lnk.symbolFrom) &&
					!ZStringSymbolParser.isPunctuationSymbol(lnk.symbolTo)
					) {
					List<Link> countLinks = linksByCount.get(lnk.count);
					if (countLinks==null) {
						countLinks = new ArrayList<Link>();
						linksByCount.put(lnk.count,countLinks);
					}
					countLinks.add(lnk);
				}
			}
			if (linksByCount.size()>0) {
				System.out.println(linksByCount.size());
				System.out.println("Lowest: " + linksByCount.firstKey());
				System.out.println("Highest: " + linksByCount.lastKey());
				
				for (Entry<Integer,List<Link>> entry: linksByCount.entrySet()) {
					System.out.println(entry.getKey());
					for (Link lnk: entry.getValue()) {
						System.out.println("  " + lnk.getId());
					}
				}
			}

			/*
			List<Link> links = getContextNoLock(contextSymbol).getAllLinks(caseSensitive);
			
			SortedMap<Integer,List<Link>> linksByCount = new TreeMap<Integer,List<Link>>();
			for (Link lnk: links) {
				if (lnk.count>1 && 
					!ZStringSymbolParser.isLineEndSymbol(lnk.symbolFrom) &&
					!ZStringSymbolParser.isLineEndSymbol(lnk.symbolTo) &&
					!ZStringSymbolParser.isPunctuationSymbol(lnk.symbolFrom) &&
					!ZStringSymbolParser.isPunctuationSymbol(lnk.symbolTo)
					) {
					List<Link> countLinks = linksByCount.get(lnk.count);
					if (countLinks==null) {
						countLinks = new ArrayList<Link>();
						linksByCount.put(lnk.count,countLinks);
					}
					countLinks.add(lnk);
				}
			}
			
			if (linksByCount.size()>0) {
				System.out.println(linksByCount.size());
				System.out.println("Lowest: " + linksByCount.firstKey());
				System.out.println("Highest: " + linksByCount.lastKey());
				
				for (Entry<Integer,List<Link>> entry: linksByCount.entrySet()) {
					System.out.println(entry.getKey());
					for (Link lnk: entry.getValue()) {
						System.out.println("  " + lnk.getId());
					}
				}
			}
			*/
		}
		
		unlockMe(this);
	}
}
