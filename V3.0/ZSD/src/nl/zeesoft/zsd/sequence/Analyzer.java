package nl.zeesoft.zsd.sequence;

import java.util.ArrayList;
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
	private String									ioSeparator				= "[OUTPUT]";	

	private SortedMap<String,AnalyzerSymbol>		knownSymbols			= new TreeMap<String,AnalyzerSymbol>();
	private List<String>							knownContexts			= new ArrayList<String>();
	private SortedMap<String,Integer>				symbolContextCounts		= new TreeMap<String,Integer>();
	private SortedMap<String,Double>				symbolContextMaxProbs	= new TreeMap<String,Double>();
	private SortedMap<String,Double>				symbolContextMinProbs	= new TreeMap<String,Double>();
	
	@Override
	public void initialize(List<ZStringBuilder> data) {
		if (data!=null && data.size()>0) {
			for (ZStringBuilder content: data) {
				initialize(new ZStringSymbolParser(content));
			}
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
	public ZStringBuilder initialize(String fileName) {
		ZStringSymbolParser parser = new ZStringSymbolParser();
		ZStringBuilder err = parser.fromFile(fileName);
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
				if (json.rootElement!=null && json.rootElement.children.size()>0) {
					for (JsElem seqElem: json.rootElement.children.get(0).children) {
						ZStringBuilder input = seqElem.getChildValueByName("input");
						ZStringBuilder output = seqElem.getChildValueByName("output");
						ZStringSymbolParser context = new ZStringSymbolParser(seqElem.getChildValueByName("context"));
						ZStringSymbolParser seq = new ZStringSymbolParser();
						if (input!=null) {
							seq.append(input);
							seq.append(" ");
							if (output!=null) {
								seq.append(ioSeparator);
								seq.append(" ");
								seq.append(output);
							}
							addSymbols(seq.toSymbolsPunctuated(),getContextSymbols(context));
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
								ZStringSymbolParser context = null;
								if (sequences.size()>2) {
									context = new ZStringSymbolParser(sequences.get(2));
								}
								addSymbols(seq.toSymbolsPunctuated(),getContextSymbols(context));
							}
						} else {
							addSymbols((new ZStringSymbolParser(line)).toSymbolsPunctuated(),getContextSymbols(null));
						}
					}
					l++;
				}
			} else {
				addSymbols(sequence.toSymbolsPunctuated(),getContextSymbols(null));
			}
		}
	}
	
	public String getSymbolId(String symbol,String context) {
		return context + "[]" + symbol;
	}
	
	/**
	 * Adds a list of symbols to the known symbols.
	 * 
	 * Symbol probabilities must be recalculated after calling this method.
	 * 
	 * @param symbols The list of symbols
	 * @param contextSymbols The list of context symbols
	 */
	public void addSymbols(List<String> symbols,List<String> contextSymbols) {
		int size = symbols.size();
		for (String context: contextSymbols) {
			for (String symbol: symbols) {
				if (!symbol.equals(ioSeparator)) {
					String id = getSymbolId(symbol,context);
					AnalyzerSymbol as = knownSymbols.get(id);
					if (as==null) {
						as = new AnalyzerSymbol();
						as.symbol = symbol;
						as.context = context;
						knownSymbols.put(id,as);
						if (!knownContexts.contains(context)) {
							knownContexts.add(context);
						}
					}
					as.count++;
				} else {
					size--;
				}
			}
			Integer count = symbolContextCounts.get(context);
			if (count==null) {
				count = new Integer(0);
			}
			count += size;
			symbolContextCounts.put(context,count);
		}
	}

	/**
	 * Calculates the known symbol probabilities.
	 */
	public void calculateProb() {
		symbolContextMaxProbs.clear();
		symbolContextMinProbs.clear();
		for (Entry<String,AnalyzerSymbol> entry: knownSymbols.entrySet()) {
			entry.getValue().prob = ((double)entry.getValue().count / (double)symbolContextCounts.get(entry.getValue().context));
			Double maxProb = symbolContextMaxProbs.get(entry.getValue().context);
			Double minProb = symbolContextMinProbs.get(entry.getValue().context);
			if (maxProb==null){
				maxProb = new Double(0);
			}
			if (minProb==null){
				minProb = new Double(1);
			}
			if (entry.getValue().prob>maxProb) {
				maxProb = entry.getValue().prob;
				symbolContextMaxProbs.put(entry.getValue().context,maxProb);
			}
			if (entry.getValue().prob<minProb) {
				minProb = entry.getValue().prob;
				symbolContextMinProbs.put(entry.getValue().context,minProb);
			}
		}
	}
	
	public List<String> getCaseVariations(String symbol) {
		List<String> r = new ArrayList<String>();
		r.add(symbol);
		String cased = symbol.toLowerCase();
		if (!r.contains(cased)) {
			r.add(cased);
		}
		cased = symbol.toUpperCase();
		if (!r.contains(cased)) {
			r.add(cased);
		}
		cased = upperCaseFirst(symbol);
		if (!r.contains(cased)) {
			r.add(cased);
		}
		return r;
	}
	
	/**
	 * Returns the string with the first character converted to upper case.
	 * 
	 * @param str The string
	 * @return The string with the first character converted to upper case
	 */
	public static String upperCaseFirst(String str) {
		String r = str.substring(0,1).toUpperCase();
		if (str.length()>1) {
			r += str.substring(1).toLowerCase();
		}
		return r;
	}

	public List<AnalyzerSymbol> getKnownSymbols(String symbol,String context,boolean caseInsensitive) {
		List<AnalyzerSymbol> r = new ArrayList<AnalyzerSymbol>();
		List<AnalyzerSymbol> asl = getKnownSymbols(symbol,caseInsensitive);
		for (AnalyzerSymbol as: asl) {
			if (as.context.equals(context)) {
				r.add(as);
			}
		}
		return r;
	}

	public List<AnalyzerSymbol> getKnownSymbols(String symbol,boolean caseInsensitive) {
		List<AnalyzerSymbol> r = new ArrayList<AnalyzerSymbol>();
		if (caseInsensitive) {
			for (String cased: getCaseVariations(symbol)) {
				for (String c: knownContexts) {
					AnalyzerSymbol as = knownSymbols.get(getSymbolId(cased,c));
					if (as!=null) {
						r.add(as);
					}
				}
			}
		} else {
			for (String c: knownContexts) {
				AnalyzerSymbol as = knownSymbols.get(getSymbolId(symbol,c));
				if (as!=null) {
					r.add(as);
				}
			}
		}
		return r;
	}

	public boolean knownSymbolsContains(String symbol,String context) {
		return knownSymbols.containsKey(getSymbolId(symbol,context));
	}

	public AnalyzerSymbol getKnownSymbol(String symbol,String context) {
		return knownSymbols.get(getSymbolId(symbol,context));
	}

	public SortedMap<String,AnalyzerSymbol> getKnownSymbols() {
		return knownSymbols;
	}

	public void setKnownSymbols(SortedMap<String,AnalyzerSymbol> knownSymbols) {
		this.knownSymbols = knownSymbols;
	}

	public List<String> getKnownContexts() {
		return knownContexts;
	}

	public void setKnownContexts(List<String> knownContexts) {
		this.knownContexts = knownContexts;
	}
	
	public SortedMap<String, Integer> getSymbolContextCounts() {
		return symbolContextCounts;
	}

	public void setSymbolContextCounts(SortedMap<String, Integer> symbolContextCounts) {
		this.symbolContextCounts = symbolContextCounts;
	}

	public SortedMap<String, Double> getSymbolContextMaxProbs() {
		return symbolContextMaxProbs;
	}

	public void setSymbolContextMaxProbs(SortedMap<String, Double> symbolContextMaxProbs) {
		this.symbolContextMaxProbs = symbolContextMaxProbs;
	}

	public SortedMap<String, Double> getSymbolContextMinProbs() {
		return symbolContextMinProbs;
	}

	public void setSymbolContextMinProbs(SortedMap<String, Double> symbolContextMinProbs) {
		this.symbolContextMinProbs = symbolContextMinProbs;
	}
	
	private List<String> getContextSymbols(ZStringSymbolParser context) {
		List<String> r = new ArrayList<String>();
		if (context!=null && context.trim().length()>0) {
			r = context.toSymbolsPunctuated();
		}
		if (!r.contains("")) {
			r.add("");
		}
		return r;
	}
}
