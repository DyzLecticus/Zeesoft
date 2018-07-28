package nl.zeesoft.zdk;

import java.util.ArrayList;
import java.util.List;

/**
 * The Zeesoft StringSymbolParser can be used to parse symbols (words and punctuation) from the StringBuilder value and merge them back together.
 */
public class ZStringSymbolParser extends ZStringBuilder {
	private List<String>	lineEndSymbols		= new ArrayList<String>();
	private List<String>	punctuationSymbols	= new ArrayList<String>();
	
	public ZStringSymbolParser() {
		super();
		if (lineEndSymbols.size()==0) {
			initializeDefaultConfiguration();
		}
	}

	public ZStringSymbolParser(String s) {
		super(s);
		if (lineEndSymbols.size()==0) {
			initializeDefaultConfiguration();
		}
	}

	public ZStringSymbolParser(StringBuilder sb) {
		super(sb);
		if (lineEndSymbols.size()==0) {
			initializeDefaultConfiguration();
		}
	}

	public ZStringSymbolParser(ZStringBuilder zsb) {
		super(zsb);
		if (lineEndSymbols.size()==0) {
			initializeDefaultConfiguration();
		}
	}

	@Override
	public ZStringSymbolParser getCopy() {
		return new ZStringSymbolParser(getStringBuilder());
	}

	/**
	 * Parses symbols (words and punctuation) from the StringBuilder value.
	 * 
	 * Separates all punctuation symbols and then calls toSymbols to do the remaining work.
	 * Uses the configured line ending and punctuation symbols.
	 * 
	 * @return A list of symbols
	 */
	public List<String> toSymbolsPunctuated() {
		return toSymbolsPunctuated(lineEndSymbols,punctuationSymbols);
	}

	/**
	 * Parses symbols (words and punctuation) from the StringBuilder value.
	 * 
	 * Separates all punctuation symbols and then calls toSymbols to do the remaining work.
	 * 
	 * @param lineEnds A list of line end symbols
	 * @param punctuations A list of punctuation symbols to separate
	 * @return A list of symbols
	 */
	public List<String> toSymbolsPunctuated(List<String> lineEnds,List<String> punctuations) {
		return toSymbolsPunctuated(lineEnds, punctuations, false);
	}

	/**
	 * Parses symbols (words and line endings) from a text.
	 * 
	 * Uses the configured line ending symbols.
	 * 
	 * @return A list of symbols
	 */
	public List<String> toSymbols() {
		return toSymbols(lineEndSymbols);
	}

	/**
	 * Parses symbols (words and line endings) from a text.
	 * 
	 * @param lineEnds A list of line end symbols
	 * @return A list of symbols
	 */
	public List<String> toSymbols(List<String> lineEnds) {
		return toSymbols(lineEnds, false);
	}
	
	/**
	 * Merges parsed symbols back into formatted sentences and returns the new StringBuilder value.
	 * 
	 * Can optionally correct case and merge non line ending punctuation.
	 * 
	 * @param symbols The list of symbols to merge
	 * @param correctCase Indicates case is to be corrected
	 * @param correctPunctuation Indicates the punctuation is to be corrected
	 * @return The StringBuilder value 
	 */
	public StringBuilder fromSymbols(List<String> symbols, boolean correctCase, boolean correctPunctuation) {
		if (getStringBuilder()!=null) {
			StringBuilder r = new StringBuilder();
			boolean upperCaseFirstNext = correctCase;
			boolean inDoubleQuote = false;
			boolean inQuote = false;
			boolean inRoundBracket = false;
			boolean appendSpace = false;
			String pSymbol = "";
			for (String symbol: symbols) {
				if (r.length()>0 && !isLineEndSymbol(symbol) && 
					(!correctPunctuation || (!symbol.equals(",") && !symbol.equals(":") && !symbol.equals(";")))
					) {
					appendSpace = true;
				} else {
					appendSpace = false;
				}
				if (pSymbol.equals("\"") && !inDoubleQuote) {
					inDoubleQuote = true;
					appendSpace = false;
				} else if (symbol.equals("\"") && inDoubleQuote) {
					inDoubleQuote = false;
					appendSpace = false;
				}
				if (pSymbol.equals("'") && !inQuote) {
					inQuote = true;
					appendSpace = false;
				} else if (symbol.equals("'") && inQuote) {
					inQuote = false;
					appendSpace = false;
				}
				if (pSymbol.equals("(") && !inRoundBracket) {
					inRoundBracket = true;
					appendSpace = false;
				} else if (symbol.equals(")") && inRoundBracket) {
					inRoundBracket = false;
					appendSpace = false;
				}
				if (appendSpace) {
					r.append(" ");
				}
				if (upperCaseFirstNext) {
					symbol = symbol.substring(0,1).toUpperCase() + symbol.substring(1);
					upperCaseFirstNext = false;
				}
				r.append(symbol);
				if (correctCase && isLineEndSymbol(symbol)) {
					upperCaseFirstNext = true;
				}
				pSymbol = symbol;
			}
			setStringBuilder(r);
		}
		return getStringBuilder();
	}
	
	/**
	 * Initializes the default global configuration for the parser.
	 */
	public void initializeDefaultConfiguration() {
		lineEndSymbols.clear();
		lineEndSymbols.add(".");
		lineEndSymbols.add("?");
		lineEndSymbols.add("!");
		punctuationSymbols.clear();
		punctuationSymbols.add("(");
		punctuationSymbols.add(")");
		punctuationSymbols.add(":");
		punctuationSymbols.add(";");
		punctuationSymbols.add(",");
		punctuationSymbols.add("\"");
		punctuationSymbols.add("'");
	}

	/**
	 * Initializes a specific global configuration for the parser.
	 * 
	 * @param lineEndings A list of line end symbols
	 * @param punctuations A list of punctuation symbols
	 */
	public void initializeConfiguration(List<String> lineEndings,List<String> punctuations) {
		if (lineEndings!=null) {
			lineEndSymbols.clear();
			for (String symbol: lineEndings) {
				lineEndSymbols.add(symbol);
			}
		}
		if (punctuations!=null) {
			punctuationSymbols.clear();
			for (String symbol: punctuations) {
				punctuationSymbols.add(symbol);
			}
		}
	}

	/**
	 * Removes all line ending symbols from the StringBuilder value.
	 * 
	 * Uses the configured line ending symbols.
	 */
	public void removeLineEndSymbols() {
		removeLineEndSymbols(lineEndSymbols);
	}

	/**
	 * Removes all line ending symbols from the StringBuilder value.
	 * 
	 * @param lineEnds The line end symbols
	 */
	public void removeLineEndSymbols(List<String> lineEnds) {
		for (String symbol: lineEnds) {
			replace(symbol,"");
		}
	}

	/**
	 * Removes all punctuation symbols from the StringBuilder value.
	 * 
	 * Uses the configured punctuation symbols.
	 */
	public void removePunctuationSymbols() {
		removePunctuationSymbols(punctuationSymbols);
	}

	/**
	 * Removes all punctuation symbols from the StringBuilder value.
	 * 
	 * @param punctuations The punctuation symbols
	 */
	public void removePunctuationSymbols(List<String> punctuations) {
		for (String symbol: punctuations) {
			replace(symbol,"");
		}
	}

	public List<String> getLineEndSymbols() {
		return lineEndSymbols;
	}

	public List<String> getPunctuationSymbols() {
		return punctuationSymbols;
	}

	public static boolean isLineEndSymbol(String symbol) {
		ZStringSymbolParser parser = new ZStringSymbolParser();
		return parser.getLineEndSymbols().contains(symbol);
	}

	public static boolean endsWithLineEndSymbol(ZStringBuilder symbol) {
		ZStringSymbolParser parser = new ZStringSymbolParser();
		return endsWithLineEndSymbol(symbol,parser.getLineEndSymbols());
	}

	public static boolean endsWithLineEndSymbol(ZStringBuilder symbol,List<String> lineEnds) {
		boolean r = false;
		if (symbol.length()>1) {
			r = lineEnds.contains(symbol.substring(symbol.length() - 1).toString());
		} else {
			r = lineEnds.contains(symbol.toString());
		}
		return r;
	}

	public static boolean isPunctuationSymbol(String symbol) {
		ZStringSymbolParser parser = new ZStringSymbolParser();
		return parser.getPunctuationSymbols().contains(symbol);
	}

	protected List<String> toSymbolsPunctuated(List<String> lineEnds,List<String> punctuations,boolean isCopy) {
		List<String> symbols = new ArrayList<String>();
		if (getStringBuilder()!=null) {
			if (isCopy) {
				if (punctuations.contains("'")) {
					if (startsWith("'")) {
						insert(1," ");
					}
					if (endsWith("'")) {
						insert(length() - 2," ");
					}
					replace("'.","' .");
					replace("'!","' !");
					replace("'?","' ?");
					replace("('","( '");
					replace("')","' )");
					replace(",'",", '");
					replace("',","' ,");
					replace(":'",": '");
					replace("':","' :");
					replace(" '"," ' ");
					replace("' "," ' ");
				}
				for (String symbol: punctuations) {
					if (!symbol.equals("'")) {
						if (symbol.equals(":") || symbol.equals(";")) {
							replace(symbol + " "," " + symbol + " ");
							replace(" " + symbol," " + symbol + " ");
						} else {
							replace(symbol," " + symbol + " ");
						}
					}
				}
				symbols = toSymbols(lineEnds,isCopy);
			} else {
				symbols = copy().toSymbolsPunctuated(lineEnds, punctuations, true);
			}
		}
		return symbols;
	}

	protected List<String> toSymbols(List<String> lineEnds,boolean isCopy) {
		List<String> symbols = new ArrayList<String>();
		if (getStringBuilder()!=null) {
			if (isCopy) {
				replace("     "," ");
				replace("    "," ");
				replace("   "," ");
				replace("  "," ");
				trim();
				
				List<ZStringBuilder> syms = split(" ");
				for (ZStringBuilder sym: syms) {
					if (sym.length()>1 && endsWithLineEndSymbol(sym,lineEnds)) {
						ZStringSymbolParser symbol = new ZStringSymbolParser(sym.substring(0,sym.length() - 1));
						String lineEnd = sym.substring(sym.length() - 1).toString();
						symbol.removeLineEndSymbols();
						symbols.add(symbol.toString());
						symbols.add(lineEnd);
					} else {
						symbols.add(sym.toString());
					}
				}
			} else {
				symbols = copy().toSymbols(lineEnds,true);
			}
		}
		return symbols;
	}
	
	protected ZStringSymbolParser copy() {
		return new ZStringSymbolParser(this);
	}
}
