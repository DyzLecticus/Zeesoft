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
		ZStringSymbolParser parser = new ZStringSymbolParser();
		String err = parser.fromFile(fileName);
		if (err.length()==0) {
			initialize(parser);
		}
		return err;
	}

	/**
	 * Initializes the known symbols based on an symbol parser.
	 * 
	 * @param parser The symbol parser
	 */
	public void initialize(ZStringSymbolParser parser) {
		if (parser.length()>0) {
			List<String> symbols = parser.toSymbolsPunctuated();
			parser.toCase(true);
			for (String symbol: symbols) {
				AnalyzerSymbol count = knownSymbols.get(symbol);
				if (count==null) {
					count = new AnalyzerSymbol();
					knownSymbols.put(symbol,count);
				}
				count.count++;
			}
			totalSymbols = symbols.size();
			for (Entry<String,AnalyzerSymbol> entry: knownSymbols.entrySet()) {
				entry.getValue().prob = (symbols.size() / entry.getValue().count);
			}
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
