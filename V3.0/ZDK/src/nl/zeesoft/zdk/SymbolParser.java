package nl.zeesoft.zdk;

import java.util.ArrayList;
import java.util.List;

/**
 * Symbol parsing utility.
 */
public final class SymbolParser {
	public static final String[]	LINE_END_SYMBOLS		= {"." , "?" , "!"};
	public static final String		LINE_END_SYMBOLS_STRING	= LINE_END_SYMBOLS[0] + LINE_END_SYMBOLS[1] + LINE_END_SYMBOLS[2];

	private SymbolParser() {
		// Abstract
	}
	
	/**
	 * Parses symbols (words and punctuation) from a text
	 * 
	 * @param text The text
	 * @return A list of symbols
	 */
	public static final List<String> parseSymbolsFromText(StringBuilder text) {
		List<String> symbols = new ArrayList<String>();
		
		text = Generic.stringBuilderReplace(text,"("," ( ");
		text = Generic.stringBuilderReplace(text,")"," ) ");
		text = Generic.stringBuilderReplace(text,":"," : ");
		text = Generic.stringBuilderReplace(text,";"," ; ");
		text = Generic.stringBuilderReplace(text,","," , ");
		text = Generic.stringBuilderReplace(text,"'"," ' ");
		text = Generic.stringBuilderReplace(text,"\""," \" ");

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
		return isSymbol(symbol,LINE_END_SYMBOLS);
	}

	public static final boolean isSymbol(String symbol,String[] symbols) {
		boolean r = false;
		for (int i = 0; i < symbols.length; i++) {
			if (symbol.equals(symbols[i])) {
				r = true;
				break;
			}
		}
		return r;
	}
}
