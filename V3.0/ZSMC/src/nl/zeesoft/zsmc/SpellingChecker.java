package nl.zeesoft.zsmc;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsmc.sequence.Analyzer;
import nl.zeesoft.zsmc.sequence.AnalyzerSymbol;

/**
 * A SpellingChecker can be used to correct spelling of words and sentences
 */
public class SpellingChecker extends Analyzer {
	private static final String		ALPHABET	= "abcdefghijklmnopqrstuvwxyz";

	/**
	 * Returns the correction for a certain symbol.
	 * 
	 * @param symbol The symbol to correct
	 * @return The corrected symbol
	 */
	public String correct(String symbol) {
		ZStringBuilder r = new ZStringBuilder(symbol);
		if (!getKnownSymbols().containsKey(symbol)) {
			double highest = 0D;
			List<AnalyzerSymbol> cors = getCorrections(symbol);
			for (AnalyzerSymbol s: cors) {
				if (s.prob>highest) {
					highest = s.prob;
					r = new ZStringBuilder(s.symbol);
				}
			}
		}
		return r.toString();
	}

	/**
	 * Returns the correction for a certain symbol sequence.
	 * 
	 * @param sequence The sequence to correct
	 * @return The corrected sequence
	 */
	public ZStringSymbolParser correct(ZStringSymbolParser sequence) {
		List<String> symbols = sequence.toSymbolsPunctuated();
		List<String> corrected = new ArrayList<String>();
		for (String symbol: symbols) {
			if (symbol.length()== 1 && (ZStringSymbolParser.isLineEndSymbol(symbol) || ZStringSymbolParser.isPunctuationSymbol(symbol))) {
				corrected.add(symbol);
			} else {
				corrected.add(correct(symbol));
			}
		}
		sequence.fromSymbols(corrected,true,true);
		return sequence;
	}

	/**
	 * Returns the analyzer symbol corrections list.
	 * 
	 * @param symbol The symbol to correct
	 * @return a list of possible analyzer symbol corrections.
	 */
	public List<AnalyzerSymbol> getCorrections(String symbol) {
		List<AnalyzerSymbol> r = new ArrayList<AnalyzerSymbol>();
		if (getKnownSymbols().containsKey(symbol)) {
			AnalyzerSymbol s = new AnalyzerSymbol();
			s.symbol = symbol;
			s.prob = 1.0D;
			r.add(s);
		} else {
			List<String> added = new ArrayList<String>();
			List<ZStringBuilder> variations = generateVariations(symbol);
			for (ZStringBuilder var: variations) {
				AnalyzerSymbol s = getKnownSymbols().get(var.toString());
				if (s!=null && !added.contains(s.symbol)) {
					added.add(s.symbol);
					r.add(s);
				}
			}
			if (r.size()==0) {
				variations = addVariations(variations);
				for (ZStringBuilder var: variations) {
					AnalyzerSymbol s = getKnownSymbols().get(var.toString());
					if (s!=null && !added.contains(s.symbol)) {
						added.add(s.symbol);
						r.add(s);
					}
				}
			}
		}
		return r;
	}

	
	/**
	 * Generates primary variations for a certain symbol.
	 * 
	 * @param symbol The symbol
	 * @return The list of primary variations
	 */
	public List<ZStringBuilder> generateVariations(String symbol) {
		List<ZStringBuilder> r = new ArrayList<ZStringBuilder>();
		ZStringBuilder sym = new ZStringBuilder(symbol);
		addAdditions(r,sym,true);
		addDeletes(r,sym,true);
		addSwitches(r,sym,true);
		addReplacements(r,sym,true);
		addCases(r,sym,true);
		return r;
	}

	/**
	 * Adds secondary variations to a list of primary variations.
	 * 
	 * @param variations The primary variations
	 * @return The list of primary and secondary variations
	 */
	public List<ZStringBuilder> addVariations(List<ZStringBuilder> variations) {
		List<ZStringBuilder> currentVariations = new ArrayList<ZStringBuilder>(variations);
		int i = 0;
		for (ZStringBuilder sym: currentVariations) {
			addAdditions(variations,sym,false);
			addDeletes(variations,sym,false);
			addSwitches(variations,sym,false);
			addReplacements(variations,sym,false);
			addCases(variations,sym,false);
			if (!getKnownSymbols().containsKey(sym.toString())) {
				variations.remove(i);
			} else {
				i++;
			}
		}
		return variations;
	}

	private void addCases(List<ZStringBuilder> variations,ZStringBuilder symbol, boolean force) {
		ZStringBuilder var = null;
		var = new ZStringBuilder(symbol);
		var.toCase(true);
		if (!var.equals(symbol) && (force || getKnownSymbols().containsKey(var.toString()))) {
			variations.add(var);
		}
		var = new ZStringBuilder(symbol);
		var.toCase(false);
		if (!var.equals(symbol) && (force || getKnownSymbols().containsKey(var.toString()))) {
			variations.add(var);
		}
		var = new ZStringBuilder(symbol.substring(0,1).toUpperCase());
		if (symbol.length()>1) {
			var.append(symbol.substring(1));
		}
		if (!var.equals(symbol) && (force || getKnownSymbols().containsKey(var.toString()))) {
			variations.add(var);
		}
	}
	
	private void addAdditions(List<ZStringBuilder> variations,ZStringBuilder symbol, boolean force) {
		for (int i = 0; i<symbol.length(); i++) {
			for (int i2 = 0; i2<ALPHABET.length(); i2++) {
				ZStringBuilder var = new ZStringBuilder();
				if (i==0) {
					var.append(ALPHABET.substring(i2,i2+1));
					var.append(symbol);
					if (force || getKnownSymbols().containsKey(var.toString())) {
						variations.add(var);
					}
				}
				var = new ZStringBuilder();
				var.append(symbol.substring(0,i+1));
				var.append(ALPHABET.substring(i2,i2+1));
				if (symbol.length()>1) {
					var.append(symbol.substring(i+1));
				}
				if (force || getKnownSymbols().containsKey(var.toString())) {
					variations.add(var);
				}
			}
		}
	}

	private void addDeletes(List<ZStringBuilder> variations,ZStringBuilder symbol, boolean force) {
		if (symbol.length()>1) {
			for (int i = 0; i<symbol.length(); i++) {
				ZStringBuilder var = new ZStringBuilder();
				if (i==0) {
					var.append(symbol.substring(i+1));
				} else {
					var.append(symbol.substring(0,i));
					var.append(symbol.substring(i+1));
				}
				if (var.length()>0 && (force || getKnownSymbols().containsKey(var.toString()))) {
					variations.add(var);
				}
			}
		}
	}

	private void addSwitches(List<ZStringBuilder> variations,ZStringBuilder symbol, boolean force) {
		if (symbol.length()>1) {
			for (int i = 0; i<(symbol.length() - 1); i++) {
				ZStringBuilder var = new ZStringBuilder();
				if (i>0){
					var.append(symbol.substring(0,i));
				}
				var.append(symbol.substring(i+1,i+2));
				var.append(symbol.substring(i,i+1));
				var.append(symbol.substring(i+2));
				if (var.length()>0 && (force || getKnownSymbols().containsKey(var.toString()))) {
					variations.add(var);
				}
			}
		}
	}
	
	private void addReplacements(List<ZStringBuilder> variations,ZStringBuilder symbol, boolean force) {
		for (int i = 0; i<symbol.length(); i++) {
			for (int i2 = 0; i2<ALPHABET.length(); i2++) {
				ZStringBuilder var = new ZStringBuilder();
				var.append(symbol.substring(0,i));
				var.append(ALPHABET.substring(i2,i2+1));
				if (symbol.length()>1) {
					var.append(symbol.substring(i+1));
				}
				if (force || getKnownSymbols().containsKey(var.toString())) {
					variations.add(var);
				}
			}
		}
	}

}
