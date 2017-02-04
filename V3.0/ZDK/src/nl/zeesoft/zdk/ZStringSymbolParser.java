package nl.zeesoft.zdk;

import java.util.ArrayList;
import java.util.List;

/**
 * The Zeesoft StringSymbolParser can be used to parse symbols (words and punctuation) from the StringBuilder value and merge them back together.
 */
public class ZStringSymbolParser extends ZStringBuilder {
	public static final String[]	LINE_END_SYMBOLS		= {"." , "?" , "!"};
	public static final String		PUNCTUATION_SYMBOLS		= "():;,'\"";

	public ZStringSymbolParser() {
		super();
	}

	public ZStringSymbolParser(String s) {
		super(s);
	}

	public ZStringSymbolParser(StringBuilder sb) {
		super(sb);
	}

	public ZStringSymbolParser(ZStringBuilder zsb) {
		super(zsb);
	}
	
	/**
	 * Parses symbols (words and punctuation) from the StringBuilder value.
	 * 
	 * @return A list of symbols
	 */
	public List<String> toSymbolsPunctuated() {
		return toSymbolsPunctuated(PUNCTUATION_SYMBOLS);
	}

	/**
	 * Parses symbols (words and punctuation) from the StringBuilder value.
	 * 
	 * Separates all punctuation symbols and then calls toSymbols to do the remaining work.
	 * 
	 * @param punctuationSymbols The string containing the punctuation symbols to separate
	 * @return A list of symbols
	 */
	public List<String> toSymbolsPunctuated(String punctuationSymbols) {
		if (getStringBuilder()!=null) {
			for (int i = 0; i < punctuationSymbols.length(); i++) {
				String c = punctuationSymbols.substring(i,i+1);
				replace(c," " + c + " ");
			}
		}
		return toSymbols();
	}

	/**
	 * Parses symbols (words and line endings) from a text.
	 * 
	 * @return A list of symbols
	 */
	public List<String> toSymbols() {
		List<String> symbols = new ArrayList<String>();
		if (getStringBuilder()!=null) {
			replace("     "," ");
			replace("    "," ");
			replace("   "," ");
			replace("  "," ");
			trim();
			List<StringBuilder> syms = split(" ");
	
			for (StringBuilder sym: syms) {
				if (sym.length()>1 && endsWithLineEndSymbol(sym)) {
					ZStringBuilder symbol = new ZStringBuilder(sym.substring(0,sym.length() - 1));
					String lineEnd = sym.substring(sym.length() - 1);
					removeLineEndSymbols(symbol); 
					symbols.add(symbol.toString());
					symbols.add(lineEnd);
				} else {
					symbols.add(sym.toString());
				}
			}
		}
		return symbols;
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
			for (String symbol: symbols) {
				if (r.length()>0 && !isLineEndSymbol(symbol) && 
					(!correctPunctuation || (!symbol.equals(",") && !symbol.equals(":") && !symbol.equals(";")))
					) {
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
			}
			setStringBuilder(r);
		}
		return getStringBuilder();
	}

	private boolean endsWithLineEndSymbol(StringBuilder symbol) {
		boolean r = false;
		if (symbol.length()>1) {
			r = isLineEndSymbol(symbol.substring(symbol.length() - 1));
		} else {
			r = isLineEndSymbol(symbol.toString());
		}
		return r;
	}

	private void removeLineEndSymbols(ZStringBuilder symbol) {
		for (String lineEnd: LINE_END_SYMBOLS) {
			symbol.replace(lineEnd,"");
		}
	}
	
	private boolean isLineEndSymbol(String symbol) {
		boolean r = false;
		for (String lineEnd: LINE_END_SYMBOLS) {
			if (symbol.equals(lineEnd)) {
				r = true;
				break;
			}
		}
		return r;
	}
}
