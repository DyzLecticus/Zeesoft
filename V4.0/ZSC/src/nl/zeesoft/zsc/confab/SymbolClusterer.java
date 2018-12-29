package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Config;

public class SymbolClusterer extends Confabulator {
	private String							contextSymbol	= "";
	//private boolean							caseSensitive	= true;
	
	private List<SymbolCluster>				clusters		= new ArrayList<SymbolCluster>();

	//private SortedMap<Integer,List<String>> symbolsByCount	= new TreeMap<Integer,List<String>>();
	private SortedMap<String,Integer> 		countPerSymbol	= new TreeMap<String,Integer>();
	private List<Integer[]>					vectors			= new ArrayList<Integer[]>();
	
	public SymbolClusterer(Config config, String name, int maxDistance) {
		super(config, name, maxDistance);
	}
	
	public void initializeClusterer(String contextSymbol,boolean caseSensitive) {
		lockMe(this);
		
		this.contextSymbol = contextSymbol;
		//this.caseSensitive = caseSensitive;
		
		clusters.clear();
		
		Context ctxt = getContextNoLock(contextSymbol);

		System.out.println("Building symbols by count ...");
		buildSymbolsByCountNoLock(contextSymbol, caseSensitive);
		
		System.out.println("Building vectors ...");
		buildVectorsNoLock(contextSymbol, caseSensitive);
		
		System.out.println("Creating initial cluster ...");
		SymbolCluster cluster = new SymbolCluster();
		for (String symbol: ctxt.knownSymbols) {
			cluster.symbols.add(symbol);
		}
		clusters.add(cluster);
		
		unlockMe(this);
	}

	public List<SymbolCluster> getSymbolClusters() {
		List<SymbolCluster> r = new ArrayList<SymbolCluster>();
		lockMe(this);
		for (SymbolCluster cluster: clusters) {
			r.add(cluster.copy());
		}
		unlockMe(this);
		return r;
	}

	public void splitCluster(SymbolCluster cluster) {
		lockMe(this);
		splitClusterNoLock(cluster);
		unlockMe(this);
	}

	private void splitClusterNoLock(SymbolCluster cluster) {
		String splitSymbol = "";
		
		int totalCount = 0;
		for (String symbol: cluster.symbols) {
			totalCount += countPerSymbol.get(symbol);
		}
		int averageCount = totalCount / cluster.symbols.size();
		int symbolCount = 999999999;
		for (String symbol: cluster.symbols) {
			int count = countPerSymbol.get(symbol);
			if (count>averageCount && count<=symbolCount) {
				symbolCount = count;
				splitSymbol = symbol;
			}
		}
		
		System.out.println("Splitting cluster over symbol: " + splitSymbol + " (Size: " + cluster.symbols.size() + ", total: " + totalCount + ", average: " + averageCount + ", symbol count: " + symbolCount);
		String[] splitSymbols = new String[1];
		splitSymbols[0] = splitSymbol;
		
		splitClusterNoLock(cluster,splitSymbols);
	}

	private void splitClusterNoLock(SymbolCluster cluster,String[] splitSymbols) {
		
		/*
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
		*/
	}

	private String getOppositeSymbolFromClusterNoLock(SymbolCluster cluster,String[] splitSymbols) {
		String r = "";
		
		Context ctxt = getContextNoLock(contextSymbol);
		
		for (String symbol: splitSymbols) {
			int index = ctxt.knownSymbols.indexOf(symbol);
			Integer[] vectorA = vectors.get(index);
			
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
			
		}
		
		/*
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
		*/
		return r;
	}
	
	private void buildSymbolsByCountNoLock(String contextSymbol,boolean caseSensitive) {
		//symbolsByCount.clear();
		countPerSymbol.clear();
		
		Context ctxt = getContextNoLock(contextSymbol);
		if (ctxt.totalLinks>0) {
			for (String symbol: ctxt.knownSymbols) {
				int count = 0;
				for (int i = 1; i<= getMaxDistance(); i++) {
					List<Link> linksFrom = getLinksNoLock(symbol,i,contextSymbol,"",caseSensitive);
					for (Link lnk: linksFrom) {
						count += lnk.count;
					}
					List<Link> linksTo = getLinksNoLock("",i,contextSymbol,symbol,caseSensitive);
					for (Link lnk: linksTo) {
						count += lnk.count;
					}
				}
				/*
				List<String> syms = symbolsByCount.get(count);
				if (syms==null) {
					syms = new ArrayList<String>();
					syms.add(symbol);
				}
				symbolsByCount.put(count,syms);
				*/
				countPerSymbol.put(symbol,count);
			}
		}
		
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
