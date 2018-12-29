package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;

public class SymbolClusterer {
	private Confabulator					confabulator	= null;
	private Context							context			= null;
	private boolean							caseSensitive	= true;
	
	private List<Integer[]>					vectors			= new ArrayList<Integer[]>();
	private List<Double[]>					differences		= new ArrayList<Double[]>();
	
	public SymbolClusterer(Confabulator confab,String contextSymbol,boolean caseSensitive) {
		confabulator = confab;
		context = confab.getContextNoLock(contextSymbol);
		this.caseSensitive = caseSensitive;
	}
	
	public void createVectors() {
		vectors.clear();
		if (context.totalLinks>0) {
			for (String symbolA: context.knownSymbols) {
				Integer[] vector = new Integer[context.knownSymbols.size()];
				int i = 0;
				for (String symbolB: context.knownSymbols) {
					int count = 0;
					if (!symbolA.equals(symbolB)) {
						for (int d = 1; d <= confabulator.getMaxDistance(); d++) {
							List<Link> links = confabulator.getLinks(symbolA,d,context.contextSymbol,symbolB,caseSensitive);
							for (Link lnk: links) {
								count += lnk.count;
							}
							links = confabulator.getLinks(symbolB,d,context.contextSymbol,symbolA,caseSensitive);
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
	
	public void calculateDifferences() {
		differences.clear();
		if (vectors.size()>0) {
			for (String symbolA: context.knownSymbols) {
				Integer[] vectorA = getVectorForSymbolNoLock(symbolA);
				Double[] difference = new Double[context.knownSymbols.size()];
				int i = 0;
				for (String symbolB: context.knownSymbols) {
					int total = 0;
					int diff = 0;
					if (!symbolA.equals(symbolB)) {
						Integer[] vectorB = getVectorForSymbolNoLock(symbolB);
						for (int v = 0; v < vectorA.length; v++) {
							if (vectorA[v] > vectorB[v]) {
								total += vectorA[v];
								diff += vectorA[v] - vectorB[v];
							} else if (vectorA[v] < vectorB[v]) {
								total += vectorB[v];
								diff += vectorB[v] - vectorA[v];
							} else {
								total += vectorB[v];
							}
						}
					}
					if (diff>0) {
						difference[i] = (double)total / (double)diff;
					} else {
						difference[i] = 0D;
					}
					i++;
				}
				differences.add(difference);
			}
		}
	}

	public List<Integer[]> getVectors() {
		return vectors;
	}

	public List<Double[]> getDifferences() {
		return differences;
	}
	
	protected Integer[] getVectorForSymbolNoLock(String symbol) {
		Integer[] r = null;
		int index = context.knownSymbols.indexOf(symbol);
		if (index>=0 && index<vectors.size()) {
			r = vectors.get(index);
		}
		return r;
	}
}
