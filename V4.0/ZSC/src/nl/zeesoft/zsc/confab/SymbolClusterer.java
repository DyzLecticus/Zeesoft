package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.thread.Locker;

public class SymbolClusterer extends Locker {
	private Confabulator					confabulator	= null;
	private Context							context			= null;
	private boolean							caseSensitive	= true;
	
	private List<Integer[]>					vectors			= new ArrayList<Integer[]>();
	private List<Double[]>					differences		= new ArrayList<Double[]>();
	
	public SymbolClusterer(Confabulator confab,String contextSymbol,boolean caseSensitive) {
		super(confab.getConfiguration().getMessenger());
		confabulator = confab;
		context = confab.getContextNoLock(contextSymbol);
		this.caseSensitive = caseSensitive;
	}
	
	public void createVectors() {
		lockMe(this);
		createVectorsNoLock();
		unlockMe(this);
	}

	public List<Integer[]> getVectors() {
		List<Integer[]> r = new ArrayList<Integer[]>(); 
		lockMe(this);
		for (Integer[] vector: vectors) {
			Integer[] vec = new Integer[vector.length];
			for (int i = 0; i < vector.length; i++) {
				vec[i] = vector[i];
			}
			r.add(vec);
		}
		unlockMe(this);
		return r;
	}
	
	public void calculateDifferences() {
		lockMe(this);
		calculateDifferencesNoLock();
		unlockMe(this);
	}

	public List<Double[]> getDifferences() {
		List<Double[]> r = new ArrayList<Double[]>(); 
		lockMe(this);
		for (Double[] difference: differences) {
			Double[] diff = new Double[difference.length];
			for (int i = 0; i < difference.length; i++) {
				diff[i] = difference[i];
			}
			r.add(diff);
		}
		unlockMe(this);
		return r;
	}
	
	protected Integer[] getVectorForSymbolNoLock(String symbol) {
		Integer[] r = null;
		int index = context.knownSymbols.indexOf(symbol);
		if (index>=0 && index<vectors.size()) {
			r = vectors.get(index);
		}
		return r;
	}

	protected Double[] getDifferenceForSymbolNoLock(String symbol) {
		Double[] r = null;
		int index = context.knownSymbols.indexOf(symbol);
		if (index>=0 && index<differences.size()) {
			r = differences.get(index);
		}
		return r;
	}

	protected void createVectorsNoLock() {
		vectors.clear();
		if (context.totalLinks>0) {
			for (String symbolA: context.knownSymbols) {
				Integer[] vector = new Integer[context.knownSymbols.size()];
				int i = 0;
				for (String symbolB: context.knownSymbols) {
					int count = 0;
					if (!symbolA.equals(symbolB)) {
						for (int d = 1; d <= confabulator.getMaxDistance(); d++) {
							List<Link> links = confabulator.getLinksNoLock(symbolA,d,context.contextSymbol,symbolB,caseSensitive);
							for (Link lnk: links) {
								count += lnk.count;
							}
							links = confabulator.getLinksNoLock(symbolB,d,context.contextSymbol,symbolA,caseSensitive);
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

	protected void calculateDifferencesNoLock() {
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
}
