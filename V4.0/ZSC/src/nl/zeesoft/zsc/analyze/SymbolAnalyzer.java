package nl.zeesoft.zsc.analyze;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zsc.confab.Confabulator;
import nl.zeesoft.zsc.confab.Link;

public class SymbolAnalyzer extends Locker {
	private Confabulator					confabulator	= null;
	private String							contextSymbol	= "";
	private boolean							caseSensitive	= true;
	private List<String>					knownSymbols	= new ArrayList<String>();
	
	private SortedMap<String,Vector>		vectors			= new TreeMap<String,Vector>();
	
	public SymbolAnalyzer(Confabulator confab,String contextSymbol,boolean caseSensitive) {
		super(confab.getConfiguration().getMessenger());
		this.confabulator = confab;
		this.contextSymbol = contextSymbol;
		this.caseSensitive = caseSensitive;
		knownSymbols = confab.getContext(contextSymbol).knownSymbols;
	}

	public Confabulator getConfabulator() {
		return confabulator;
	}
	
	public List<String> getKnownSymbols() {
		return knownSymbols;
	}

	public int getVectors() {
		int r = 0;
		lockMe(this);
		r = vectors.size();
		unlockMe(this);
		return r;
	}

	public SortedMap<Double,String> getDifferencesForSymbolByDifference(String symbol) {
		SortedMap<Double,String> r = new TreeMap<Double,String>();
		double differences[] = getDifferencesForSymbol(symbol);
		for (int i = 0; i < differences.length; i++) {
			String symbolB = knownSymbols.get(i);
			if (!symbolB.equals(symbol)) {
				r.put(differences[i],symbolB);
			}
		}
		return r;
	}
	
	public double[] getDifferencesForSymbol(String symbol) {
		double[] r = new double[knownSymbols.size()];
		int i = 0;
		for (String symbolB: knownSymbols) {
			r[i] = compareSymbols(symbol,symbolB);
			i++;
		}
		return r;
	}
	
	public double compareSymbols(String symbolA, String symbolB) {
		double r = 0.0D;
		if (!symbolA.equals(symbolB)) {
			Vector vectorA = getOrCreateVectorForSymbol(symbolA);
			Vector vectorB = getOrCreateVectorForSymbol(symbolB);
			for (int d = 0; d < confabulator.getMaxDistance(); d++) {
				for (int c = 0; c < knownSymbols.size(); c++) {
					int countA = vectorA.getIntegerValue(d,c);
					int countB = vectorB.getIntegerValue(d,c);
					if (countA>countB) {
						r += (1.0D - ((double)countB / (double)countA));
					} else if (countA<countB) {
						r += (1.0D - ((double)countA / (double)countB));
					}
				}
			}
		}
		return r;
	}
	
	public Vector getVectorForSymbol(String symbol) {
		return getOrCreateVectorForSymbol(symbol);
	}
	
	public void clearVectors() {
		lockMe(this);
		for (Vector vector: vectors.values()) {
			vector.values.clear();
		}
		vectors.clear();
		unlockMe(this);
	}
	
	private Vector getOrCreateVectorForSymbol(String symbol) {
		lockMe(this);
		Vector r = vectors.get(symbol);
		unlockMe(this);
		if (r==null) {
			r = createVectorForSymbolNoLock(symbol);
			lockMe(this);
			vectors.put(symbol,r);
			unlockMe(this);
		}
		return r;
	}
	
	private Vector createVectorForSymbolNoLock(String symbol) {
		Vector r = new Vector();
		for (String symbolA: knownSymbols) {
			if (symbolA.equals(symbol) || (!caseSensitive && symbolA.equalsIgnoreCase(symbol))) {
				int i = 0;
				for (String symbolB: knownSymbols) {
					int count = 0;
					if (!symbolA.equals(symbolB)) {
						for (int d = 1; d <= confabulator.getMaxDistance(); d++) {
							List<Link> links = confabulator.getLinks(symbolA,d,contextSymbol,symbolB,caseSensitive);
							for (Link lnk: links) {
								count += lnk.count;
							}
							links = confabulator.getLinks(symbolB,d,contextSymbol,symbolA,caseSensitive);
							for (Link lnk: links) {
								count += lnk.count;
							}
							if (count>0) {
								r.setValue((d - 1),i,count);
							}
						}
					}
					i++;
				}
			}
		}
		return r;
	}
}
