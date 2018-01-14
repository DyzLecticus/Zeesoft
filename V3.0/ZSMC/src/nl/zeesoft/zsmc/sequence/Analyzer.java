package nl.zeesoft.zsmc.sequence;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;

/**
 * An Analyzer can be used to parse sequences into a list of AnalyzerSymbols in order to obtain statistical information about the set of symbols.
 */
public class Analyzer {
	private SortedMap<String,AnalyzerSymbol> 		knownSymbols	= new TreeMap<String,AnalyzerSymbol>();
	private int										totalSymbols	= 0;
	
	/**
	 * Initializes the known symbols based on an input file.
	 * 
	 * @param fileName The full name of the file
	 * @return An error message if applicable
	 */
	public String initialize(String fileName) {
		knownSymbols.clear();
		totalSymbols = 0;
		ZStringSymbolParser parser = new ZStringSymbolParser();
		String err = parser.fromFile(fileName);
		if (err.length()==0) {
			addSequence(parser);
			calculateProb();
		}
		return err;
	}

	/**
	 * Initializes the known symbols based on an input file.
	 * 
	 * @param sequence The symbol parser containing the sequence
	 */
	public void initialize(ZStringSymbolParser sequence) {
		if (sequence.length()>0) {
			addSequence(sequence);
			calculateProb();
		}
	}

	/**
	 * Adds a sequence the known symbols.
	 * 
	 * Symbol probabilities must be recalculated after calling this method.
	 * 
	 * @param sequence The symbol parser containing the sequence
	 */
	public void addSequence(ZStringSymbolParser sequence) {
		if (sequence.length()>0) {
			sequence.toCase(true);
			addSymbols(sequence.toSymbolsPunctuated());
		}
	}

	/**
	 * Adds a list of symbols to the known symbols.
	 * 
	 * Symbol probabilities must be recalculated after calling this method.
	 * 
	 * @param symbols The list of symbols
	 */
	public void addSymbols(List<String> symbols) {
		if (symbols.size()>0) {
			for (String symbol: symbols) {
				AnalyzerSymbol count = knownSymbols.get(symbol);
				if (count==null) {
					count = new AnalyzerSymbol();
					knownSymbols.put(symbol,count);
				}
				count.count++;
			}
			totalSymbols += symbols.size();
		}
	}

	/**
	 * Calculates the known word probabilities.
	 */
	public void calculateProb() {
		for (Entry<String,AnalyzerSymbol> entry: knownSymbols.entrySet()) {
			entry.getValue().prob = ((double)entry.getValue().count / (double)totalSymbols);
		}
	}
	
	public SortedMap<String,AnalyzerSymbol> getKnownSymbols() {
		return knownSymbols;
	}

	public void setKnownSymbols(TreeMap<String,AnalyzerSymbol> knownSymbols) {
		this.knownSymbols = knownSymbols;
	}
	
	public int getTotalSymbols() {
		return totalSymbols;
	}
	
	public void setTotalSymbols(int totalSymbols) {
		this.totalSymbols = totalSymbols;
	}
}
