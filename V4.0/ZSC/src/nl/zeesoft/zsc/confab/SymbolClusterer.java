package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;

public class SymbolClusterer extends Confabulator {
	private List<SymbolCluster>			clusters	= new ArrayList<SymbolCluster>();
	private List<Integer[]>				vectors		= new ArrayList<Integer[]>();
	
	public SymbolClusterer(Config config, String name, int maxDistance) {
		super(config, name, maxDistance);
	}
	
	public void clusterSymbols(String contextSymbol,boolean caseSensitive) {
		lockMe(this);
		
		clusters.clear();
		
		Context ctxt = getContextNoLock(contextSymbol);
		
		System.out.println("Building vectors ...");
		buildVectorsNoLock(contextSymbol, caseSensitive);
		System.out.println("Built");
		
		for (Integer[] vectorA: vectors) {
			int closestSymNum = -1;
			int minDiff = 999999999;
			int symNum = 0;
			for (Integer[] vectorB: vectors) {
				if (vectorA!=vectorB) {
					int diff = 0;
					for (int i = 0; i < vectorA.length; i++) {
						if (vectorA[i]>vectorB[i]) {
							diff += vectorA[i] - vectorB[i];
						} else if (vectorA[i]<vectorB[i]) {
							diff += vectorB[i] - vectorA[i];
						}
					}
					if (diff<minDiff) {
						minDiff = diff;
						closestSymNum = symNum;
					}
				}
				symNum++;
			}
			System.out.println(ctxt.knownSymbols.get(0) + " - closest: " + ctxt.knownSymbols.get(closestSymNum) + " - diff: " + minDiff);
			
			break;
		}
		
		if (ctxt.totalLinks>0) {
			SortedMap<String,List<Link>> linksPerSymbol = new TreeMap<String,List<Link>>();
			SortedMap<Integer,List<String>> symbolsByCount = new TreeMap<Integer,List<String>>();
			int totalCount = 0;
			for (String symbol: ctxt.knownSymbols) {
				List<Link> links = new ArrayList<Link>();
				int count = 0;
				for (int i = 1; i<= getMaxDistance(); i++) {
					List<Link> linksFrom = getLinksNoLock(symbol,i,contextSymbol,"",caseSensitive);
					for (Link lnk: linksFrom) {
						links.add(lnk);
						count += lnk.count;
					}
					List<Link> linksTo = getLinksNoLock("",i,contextSymbol,symbol,caseSensitive);
					for (Link lnk: linksTo) {
						links.add(lnk);
						count += lnk.count;
					}
				}
				linksPerSymbol.put(symbol,links);
				List<String> syms = symbolsByCount.get(count);
				if (syms==null) {
					syms = new ArrayList<String>();
					syms.add(symbol);
				}
				symbolsByCount.put(count,syms);
				totalCount += count;
			}
			
			int average = totalCount / ctxt.knownSymbols.size();
			String aboveAverageSymbol = "";
			for (Entry<Integer,List<String>> entry: symbolsByCount.entrySet()) {
				ZStringBuilder syms = new ZStringBuilder();
				for (String sym: entry.getValue()) {
					if (syms.length()>0) {
						syms.append(", ");
					}
					syms.append(sym);
					if (entry.getKey()>average && aboveAverageSymbol.length()==0) {
						aboveAverageSymbol = sym;
					}
				}
				System.out.println(syms + ": " + entry.getKey());
			}
			System.out.println("Average: " + average + ", above average symbol: " + aboveAverageSymbol);
		}
		
		unlockMe(this);
	}
	
	private void buildVectorsNoLock(String contextSymbol,boolean caseSensitive) {
		vectors.clear();
		
		Context ctxt = getContextNoLock(contextSymbol);
		if (ctxt.totalLinks>0) {
			for (String symbolA: ctxt.knownSymbols) {
				Integer[] vector = new Integer[ctxt.knownSymbols.size()];
				int i = 0;
				for (String symbolB: ctxt.knownSymbols) {
					int count = 0;
					if (!symbolA.equals(symbolB)) {
						for (int d = 1; d <= getMaxDistance(); d++) {
							List<Link> links = getLinksNoLock(symbolA,d,contextSymbol,symbolB,caseSensitive);
							for (Link lnk: links) {
								count += lnk.count;
							}
							links = getLinksNoLock(symbolB,d,contextSymbol,symbolA,caseSensitive);
							for (Link lnk: links) {
								count += lnk.count;
							}
						}
					}
					vector[i] = count;
					i++;
				}
				vectors.add(vector);
			}
		}
		
	}
}
