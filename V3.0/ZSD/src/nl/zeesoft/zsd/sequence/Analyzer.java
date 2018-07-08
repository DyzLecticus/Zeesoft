package nl.zeesoft.zsd.sequence;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.initialize.Initializable;

/**
 * An Analyzer can be used to parse sequences into a list of AnalyzerSymbols in order to obtain statistical information about the set of symbols.
 */
public class Analyzer implements Initializable {
	private String									ioSeparator		= "[OUTPUT]";	

	private SortedMap<String,AnalyzerSymbol> 		knownSymbols	= new TreeMap<String,AnalyzerSymbol>();
	private int										symbolCount		= 0;
	private double									symbolMaxProb	= 0.0D;
	private double									symbolMinProb	= 1.0D;

	@Override
	public void initialize(ZStringBuilder data) {
		if (data!=null && data.length()>0) {
			initialize(new ZStringSymbolParser(data));
		}
	}

	/**
	 * Returns the input/output separator symbol.
	 * 
	 * @return The input/output separator symbol
	 */
	public String getIoSeparator() {
		return ioSeparator;
	}

	/**
	 * Sets the input/output separator symbol.
	 * 
	 * @param ioSeparator The input/output separator
	 */
	public void setIoSeparator(String ioSeparator) {
		this.ioSeparator = ioSeparator;
	}
	
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
	 * This method accepts a lot of different formats including but not limited to TSV and JSON.
	 * 
	 * Symbol probabilities must be recalculated after calling this method.
	 * 
	 * @param sequence The symbol parser containing the sequence
	 */
	public void addSequence(ZStringSymbolParser sequence) {
		if (sequence.length()>0) {
			if (sequence.startsWith("{") && sequence.endsWith("}")) {
				JsFile json = new JsFile();
				json.fromStringBuilder(sequence);
				if (json.rootElement!=null) {
					for (JsElem seqElem: json.rootElement.children) {
						ZStringBuilder input = seqElem.getChildValueByName("input");
						ZStringBuilder output = seqElem.getChildValueByName("output");
						ZStringBuilder context = seqElem.getChildValueByName("context");
						ZStringSymbolParser seq = new ZStringSymbolParser();
						if (input!=null) {
							seq.append(input);
							seq.append(" ");
							if (output!=null) {
								seq.append(ioSeparator);
								seq.append(" ");
								seq.append(output);
							}
							if (context!=null) {
								handleContextSymbol(context);
							} else {
								handleContextSymbol(new ZStringBuilder());
							}
							addSymbols(seq.toSymbolsPunctuated());
						}
					}
				}
			} else if (sequence.containsOneOfCharacters("\n")) {
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
								seq.append(ioSeparator);
								seq.append(" ");
								seq.append(sequences.get(1));
								if (sequences.size()>2) {
									handleContextSymbol(sequences.get(2));
								} else {
									handleContextSymbol(new ZStringBuilder());
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
		int size = symbols.size();
		for (String symbol: symbols) {
			if (!symbol.equals(ioSeparator)) {
				AnalyzerSymbol count = knownSymbols.get(symbol);
				if (count==null) {
					count = new AnalyzerSymbol();
					count.symbol = symbol;
					knownSymbols.put(symbol,count);
				}
				count.count++;
			} else {
				size--;
			}
		}
		symbolCount += size;
	}

	/**
	 * Calculates the known symbol probabilities.
	 */
	public void calculateProb() {
		for (Entry<String,AnalyzerSymbol> entry: knownSymbols.entrySet()) {
			entry.getValue().prob = ((double)entry.getValue().count / (double)symbolCount);
			if (entry.getValue().prob>symbolMaxProb) {
				symbolMaxProb = entry.getValue().prob;
			}
			if (entry.getValue().prob>symbolMinProb) {
				symbolMinProb = entry.getValue().prob;
			}
		}
	}
	
	public SortedMap<String,AnalyzerSymbol> getKnownSymbols() {
		return knownSymbols;
	}

	public void setKnownSymbols(SortedMap<String,AnalyzerSymbol> knownSymbols) {
		this.knownSymbols = knownSymbols;
	}
	
	public int getSymbolCount() {
		return symbolCount;
	}
	
	public void setSymbolCount(int symbolCount) {
		this.symbolCount = symbolCount;
	}
	
	public double getSymbolMaxProb() {
		return symbolMaxProb;
	}

	public void setSymbolMaxProb(double maxSymbolProb) {
		this.symbolMaxProb = maxSymbolProb;
	}
	
	public double getSymbolMinProb() {
		return symbolMinProb;
	}

	public void setSymbolMinProb(double symbolMinProb) {
		this.symbolMinProb = symbolMinProb;
	}
}
