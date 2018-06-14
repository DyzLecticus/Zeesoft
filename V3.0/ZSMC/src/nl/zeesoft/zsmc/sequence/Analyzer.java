package nl.zeesoft.zsmc.sequence;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

/**
 * An Analyzer can be used to parse sequences into a list of AnalyzerSymbols in order to obtain statistical information about the set of symbols.
 */
public class Analyzer {
	private SortedMap<String,AnalyzerSymbol> 		knownSymbols	= new TreeMap<String,AnalyzerSymbol>();
	private int										totalSymbols	= 0;
	private double									maxSymbolProb	= 0.0D;
	
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
	 * Initializes the known symbols based on a space separated input sequence.
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
			if (sequence.containsOneOfCharacters("\n")) {
				List<ZStringBuilder> lines = sequence.split("\n");
				int l = 0;
				for (ZStringBuilder line: lines) {
					if (l>0) {
						if (line.containsOneOfCharacters("\t")) {
							List<ZStringBuilder> sequences = line.split("\t");
							if (sequences.size()>1) {
								ZStringSymbolParser seq = new ZStringSymbolParser();
								seq.append(sequences.get(0));
								seq.append(" ");
								seq.append(sequences.get(1));
								if (sequences.size()>2) {
									handleContextSymbol(sequences.get(2));
								}
								addSymbols(seq.toSymbolsPunctuated());
							}
						} else {
							addSymbols((new ZStringSymbolParser(line)).toSymbolsPunctuated());
						}
					}
					l++;
				}
			} else {
				addSymbols(sequence.toSymbolsPunctuated());
			}
		}
	}

	protected void handleContextSymbol(ZStringBuilder contextSymbol) {
		// Override to implement
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
					count.symbol = symbol;
					knownSymbols.put(symbol,count);
				}
				count.count++;
			}
			totalSymbols += symbols.size();
		}
	}

	/**
	 * Calculates the known symbol probabilities.
	 */
	public void calculateProb() {
		for (Entry<String,AnalyzerSymbol> entry: knownSymbols.entrySet()) {
			entry.getValue().prob = ((double)entry.getValue().count / (double)totalSymbols);
			if (entry.getValue().prob>maxSymbolProb) {
				maxSymbolProb = entry.getValue().prob;
			}
		}
	}
	
	public SortedMap<String,AnalyzerSymbol> getKnownSymbols() {
		return knownSymbols;
	}

	public void setKnownSymbols(SortedMap<String,AnalyzerSymbol> knownSymbols) {
		this.knownSymbols = knownSymbols;
	}
	
	public int getTotalSymbols() {
		return totalSymbols;
	}
	
	public void setTotalSymbols(int totalSymbols) {
		this.totalSymbols = totalSymbols;
	}
	
	public double getMaxSymbolProb() {
		return maxSymbolProb;
	}

	public void setMaxSymbolProb(double maxSymbolProb) {
		this.maxSymbolProb = maxSymbolProb;
	}
}
