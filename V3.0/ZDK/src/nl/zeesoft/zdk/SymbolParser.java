package nl.zeesoft.zdk;

import java.util.ArrayList;
import java.util.List;

/**
 * Symbol parsing utility.
 */
public final class SymbolParser {
	public static final String[]	LINE_END_SYMBOLS		= {"." , "?" , "!"};
	public static final String		LINE_END_SYMBOLS_STRING	= LINE_END_SYMBOLS[0] + LINE_END_SYMBOLS[1] + LINE_END_SYMBOLS[2];
	public static final String		PUNCTUATION_SYMBOLS		= "():;,'\"";

	private SymbolParser() {
		// Abstract
	}

	/**
	 * Parses symbols (words and punctuation) from a text.
	 * 
	 * Separates all common punctuation symbols in the text and then calls parseSymbols to do the remaining work.
	 * 
	 * @param text The text
	 * @return A list of symbols
	 */
	public static final List<String> parseSymbolsFromText(StringBuilder text) {
		return parseSymbolsFromText(text,PUNCTUATION_SYMBOLS);
	}

	/**
	 * Parses symbols (words and punctuation) from a text.
	 * 
	 * Separates all punctuation symbols in the text and then calls parseSymbols to do the remaining work.
	 * 
	 * @param text The text
	 * @param punctuationSymbols The string containing the punctuation symbols to separate
	 * @return A list of symbols
	 */
	public static final List<String> parseSymbolsFromText(StringBuilder text,String punctuationSymbols) {
		for (int i = 0; i < punctuationSymbols.length(); i++) {
			String c = punctuationSymbols.substring(i,i+1);
			Generic.stringBuilderReplace(text,c," " + c + " ");
		}
		return parseSymbols(text);
	}

	/**
	 * Parses symbols (words and line endings) from a text.
	 * 
	 * @param text The text
	 * @return A list of symbols
	 */
	public static final List<String> parseSymbols(StringBuilder text) {
		List<String> symbols = new ArrayList<String>();
		
		text = Generic.stringBuilderReplace(text,"     "," ");
		text = Generic.stringBuilderReplace(text,"    "," ");
		text = Generic.stringBuilderReplace(text,"   "," ");
		text = Generic.stringBuilderReplace(text,"  "," ");
		
		List<StringBuilder> syms = Generic.stringBuilderSplit(Generic.stringBuilderTrim(text)," ");

		for (StringBuilder sym: syms) {
			if (sym.length()>1 && endsWithLineEndSymbol(sym)) {
				StringBuilder symbol = new StringBuilder(sym.substring(0,sym.length() - 1));
				String lineEnd = sym.substring(sym.length() - 1);
				symbol = removeLineEndSymbols(symbol); 
				symbols.add(symbol.toString());
				symbols.add(lineEnd);
			} else {
				symbols.add(sym.toString());
			}
		}
		
		return symbols;
	}
	
	/**
	 * Merges parsed symbols back into a nice text.
	 * Can optionally correct case and merge non line ending punctuation.
	 * 
	 * @param symbols The list of symbols to merge
	 * @param correctCase Indicates case is to be corrected
	 * @param correctPunctuation Indicates the punctuation is to be corrected
	 * @return The merged symbols
	 */
	public final static StringBuilder textFromSymbols(List<String> symbols, boolean correctCase, boolean correctPunctuation) {
		StringBuilder r = new StringBuilder();
		boolean upperCaseFirstNext = correctCase;
		for (String symbol: symbols) {
			if (r.length()>0 && !SymbolParser.isLineEndSymbol(symbol) && 
				(!correctPunctuation || (!symbol.equals(",") && !symbol.equals(":") && !symbol.equals(";")))
				) {
				r.append(" ");
			}
			if (upperCaseFirstNext) {
				symbol = symbol.substring(0,1).toUpperCase() + symbol.substring(1);
				upperCaseFirstNext = false;
			}
			r.append(symbol);
			if (correctCase && SymbolParser.isLineEndSymbol(symbol)) {
				upperCaseFirstNext = true;
			}
		}
		return r;
	}

	public static final boolean endsWithLineEndSymbol(StringBuilder symbol) {
		boolean r = false;
		if (symbol.length()>1) {
			r = isLineEndSymbol(symbol.substring(symbol.length() - 1));
		} else {
			r = isLineEndSymbol(symbol.toString());
		}
		return r;
	}

	public static final StringBuilder removeLineEndSymbols(StringBuilder symbol) {
		for (String lineEnd: LINE_END_SYMBOLS) {
			symbol = Generic.stringBuilderReplace(symbol,lineEnd,"");
		}
		return symbol;
	}
	
	public static final boolean isLineEndSymbol(String symbol) {
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
