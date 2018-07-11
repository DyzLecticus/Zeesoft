package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.entity.UniversalNumeric;
import nl.zeesoft.zsd.sequence.AnalyzerSymbol;
import nl.zeesoft.zsd.sequence.SequenceAnalyzer;
import nl.zeesoft.zsd.sequence.SequenceAnalyzerSymbolLink;

/**
 * A SymbolCorrector can be used to correct the spelling of symbols and sequences.
 */
public class SymbolCorrector extends SequenceAnalyzer {
	private static final int		MAX_SYMBOL_LENGTH		= 40;
	private static final int		MAX_SECONDARY_LENGTH	= 32;
	
	private static final String		ALPHABET				= "abcdefghijklmnopqrstuvwxyz";

	/**
	 * Returns the symbol bandwidth.
	 * 
	 * @param context The optional context symbol
	 * @return The symbol bandwidth
	 */
	public double getSymbolContextBandwidth(String context) {
		double r = 1D;
		Double maxProb = getSymbolContextMaxProbs().get(context);
		if (maxProb!=null) {
			r = (maxProb - getSymbolContextMinProbs().get(context) / 2D);
		}
		return r;
	}

	/**
	 * Returns the link context bandwidth.
	 * 
	 * @param context The optional context symbol
	 * @return The link context bandwidth
	 */
	public double getLinkContextBandwidth(String context) {
		double r = getSymbolContextBandwidth(context);
		Double maxProb = getLinkContextMaxProbs().get(context);
		if (maxProb!=null) {
			r = (maxProb - getLinkContextMinProbs().get(context) / 2D);
		}
		return r;
	}

	/**
	 * Returns the correction for a certain symbol.
	 * 
	 * @param symbol The symbol to correct
	 * @return The corrected symbol
	 */
	public String correct(String symbol) {
		return correct("",symbol,"","",getLinkContextBandwidth(""),null,0);
	}
	
	/**
	 * Returns the correction for a certain symbol.
	 * 
	 * @param before The optional symbol before the symbol to correct
	 * @param symbol The symbol to correct
	 * @param after The optional symbol after the symbol to correct
	 * @param context The optional context symbol to limit symbol link usage
	 * @param bandwidth The bandwidth used as a minimal symbol link probability addition
	 * @param started The optional correction start time
	 * @param stopAfterMs The optional amount of milliseconds relative to the start time after which the function will stop correcting
	 * @return The corrected symbol
	 */
	public String correct(String before,String symbol,String after,String context,double bandwidth,Date started,long stopAfterMs) {
		ZStringBuilder r = new ZStringBuilder(symbol);
		if (!knownSymbolsContains(symbol,context)) {
			double highest = 0D;
			List<AnalyzerSymbol> cors = getCorrections(before,symbol,after,context,bandwidth,started,stopAfterMs);
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
		return correct(sequence,"",getLinkContextBandwidth(""),0);
	}
	
	/**
	 * Returns the correction for a certain symbol sequence.
	 * 
	 * @param sequence The sequence to correct
	 * @param context The optional context symbol to limit symbol link usage
	 * @return The corrected sequence
	 */
	public ZStringSymbolParser correct(ZStringSymbolParser sequence,String context) {
		return correct(sequence,context,getLinkContextBandwidth(context),0);
	}

	/**
	 * Returns the correction for a certain symbol sequence.
	 * 
	 * @param sequence The sequence to correct
	 * @param context The optional context symbol to limit symbol link usage
	 * @param stopAfterMs The optional amount of milliseconds after which the function will stop correcting
	 * @return The corrected sequence
	 */
	public ZStringSymbolParser correct(ZStringSymbolParser sequence,String context,long stopAfterMs) {
		return correct(sequence,context,getLinkContextBandwidth(context),stopAfterMs);
	}

	/**
	 * Returns the correction for a certain symbol sequence.
	 * 
	 * @param sequence The sequence to correct
	 * @param context The optional context symbol to limit symbol link usage
	 * @param bandwidth The bandwidth used as a minimal symbol link probability addition
	 * @param stopAfterMs The optional amount of milliseconds after which the function will stop correcting
	 * @return The corrected sequence
	 */
	public ZStringSymbolParser correct(ZStringSymbolParser sequence,String context,double bandwidth,long stopAfterMs) {
		Date started = new Date();
		ZStringSymbolParser r = new ZStringSymbolParser();
		List<String> symbols = sequence.toSymbolsPunctuated();
		List<String> corrected = new ArrayList<String>();
		String before = "";
		String after = "";
		int i = 0;
		for (String symbol: symbols) {
			if (symbols.size()>(i + 1)) {
				after = symbols.get(i + 1);
			}
			if (UniversalNumeric.isNumeric(symbol)) {
				corrected.add(symbol);
			} else if (symbol.length()== 1 && (ZStringSymbolParser.isLineEndSymbol(symbol) || ZStringSymbolParser.isPunctuationSymbol(symbol))) {
				corrected.add(symbol);
			} else if (symbol.length()<=MAX_SYMBOL_LENGTH) {
				corrected.add(correct(before,symbol,after,context,bandwidth,started,stopAfterMs));
			}
			i++;
			before = symbol;
			if (stopAfterMs>0 && ((new Date()).getTime() - started.getTime())>=stopAfterMs) {
				break;
			}
		}
		if (i<symbols.size()) {
			for (int a = i; a < symbols.size(); a++) {
				corrected.add(symbols.get(a));
			}
		}
		r.fromSymbols(corrected,true,true);
		return r;
	}

	/**
	 * Returns the analyzer symbol corrections list.
	 * Uses the sequence analyzer symbol links to boost known symbol sequence combinations.
	 * 
	 * @param before The optional symbol before the symbol to correct
	 * @param symbol The symbol to correct
	 * @param after The optional symbol after the symbol to correct
	 * @param context The optional context symbol to limit symbol link usage
	 * @param bandwidth The bandwidth used as a minimal symbol link probability addition
	 * @param started The optional correction start time
	 * @param stopAfterMs The optional amount of milliseconds relative to the start time after which the function will stop correcting
	 * @return a list of possible analyzer symbol corrections
	 */
	public List<AnalyzerSymbol> getCorrections(String before,String symbol,String after,String context, double bandwidth,Date started,long stopAfterMs) {
		List<AnalyzerSymbol> r = getCorrections(symbol,context,started,stopAfterMs);
		if (r.size()>1 && (before.length()>0 || after.length()>0)) {
			if (before.length()>0) {
				for (AnalyzerSymbol as: r) {
					SequenceAnalyzerSymbolLink link = getKnownLinks().get(getLinkId(before,context,as.symbol));
					if (link!=null) {
						as.prob += bandwidth;
						as.prob += link.prob;
					}
				}
			}
			if (after.length()>0) {
				List<AnalyzerSymbol> afters = getCorrections(after,context,started,stopAfterMs);
				for (AnalyzerSymbol af: afters) {
					for (AnalyzerSymbol as: r) {
						SequenceAnalyzerSymbolLink link = getKnownLinks().get(getLinkId(as.symbol,context,af.symbol));
						if (link!=null) {
							as.prob += bandwidth;
							as.prob += link.prob;
						}
					}
				}
			}
		}
		return r;
	}
	
	/**
	 * Returns the analyzer symbol corrections list.
	 * 
	 * @param symbol The symbol to correct
	 * @param context The optional context symbol
	 * @param started The optional correction start time
	 * @param stopAfterMs The optional amount of milliseconds relative to the start time after which the function will stop correcting
	 * @return a list of possible analyzer symbol corrections
	 */
	public List<AnalyzerSymbol> getCorrections(String symbol,String context,Date started,long stopAfterMs) {
		List<AnalyzerSymbol> r = new ArrayList<AnalyzerSymbol>();
		if (knownSymbolsContains(symbol,context)) {
			AnalyzerSymbol s = new AnalyzerSymbol();
			s.symbol = symbol;
			s.prob = 1.0D;
			r.add(s);
		} else {
			List<String> added = new ArrayList<String>();
			List<ZStringBuilder> variations = generateVariations(symbol,context,started,stopAfterMs);
			for (ZStringBuilder var: variations) {
				AnalyzerSymbol s = getKnownSymbol(var.toString(),context);
				if (s!=null && !added.contains(s.symbol)) {
					added.add(s.symbol);
					r.add(s.copy());
				}
			}
			if (r.size()==0 && symbol.length()<=MAX_SECONDARY_LENGTH &&
				(started==null || stopAfterMs==0 || ((new Date()).getTime() - started.getTime())<stopAfterMs)
				) {
				variations = addVariations(variations,context,started,stopAfterMs);
				for (ZStringBuilder var: variations) {
					AnalyzerSymbol s = getKnownSymbol(var.toString(),context);
					if (s!=null && !added.contains(s.symbol)) {
						added.add(s.symbol);
						r.add(s.copy());
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
	 * @param context The optional context symbol
	 * @param started The correction start time
	 * @param stopAfterMs The optional amount of milliseconds relative to the start time after which the function will stop correcting
	 * @return The list of primary variations
	 */
	public List<ZStringBuilder> generateVariations(String symbol,String context,Date started,long stopAfterMs) {
		List<ZStringBuilder> r = new ArrayList<ZStringBuilder>();
		ZStringBuilder sym = new ZStringBuilder(symbol);
		addAdditions(r,sym,context,true);
		if (started==null || stopAfterMs==0 || ((new Date()).getTime() - started.getTime())<stopAfterMs) {
			addDeletes(r,sym,context,true);
		}
		if (started==null || stopAfterMs==0 || ((new Date()).getTime() - started.getTime())<stopAfterMs) {
			addSwitches(r,sym,context,true);
		}
		if (started==null || stopAfterMs==0 || ((new Date()).getTime() - started.getTime())<stopAfterMs) {
			addReplacements(r,sym,context,true);
		}
		if (started==null || stopAfterMs==0 || ((new Date()).getTime() - started.getTime())<stopAfterMs) {
			addCases(r,sym,context,true);
		}
		return r;
	}

	/**
	 * Adds secondary variations to a list of primary variations.
	 * 
	 * @param variations The primary variations
	 * @param context The optional context symbol
	 * @param started The correction start time
	 * @param stopAfterMs The optional amount of milliseconds relative to the start time after which the function will stop correcting
	 * @return The list of primary and secondary variations
	 */
	public List<ZStringBuilder> addVariations(List<ZStringBuilder> variations,String context,Date started,long stopAfterMs) {
		List<ZStringBuilder> currentVariations = new ArrayList<ZStringBuilder>(variations);
		int i = 0;
		for (ZStringBuilder sym: currentVariations) {
			if (started==null || stopAfterMs==0 || ((new Date()).getTime() - started.getTime())<stopAfterMs) {
				addAdditions(variations,sym,context,false);
				addDeletes(variations,sym,context,false);
				addSwitches(variations,sym,context,false);
				addReplacements(variations,sym,context,false);
				addCases(variations,sym,context,false);
			}
			if (!knownSymbolsContains(sym.toString(),context)) {
				variations.remove(i);
			} else {
				i++;
			}
		}
		return variations;
	}

	private void addAdditions(List<ZStringBuilder> variations,ZStringBuilder symbol,String context,boolean force) {
		for (int i = 0; i<symbol.length(); i++) {
			for (int i2 = 0; i2<ALPHABET.length(); i2++) {
				ZStringBuilder var = new ZStringBuilder();
				if (i==0) {
					var.append(ALPHABET.substring(i2,i2+1));
					var.append(symbol);
					if (force || knownSymbolsContains(var.toString(),context)) {
						variations.add(var);
					}
				}
				var = new ZStringBuilder();
				var.append(symbol.substring(0,i+1));
				var.append(ALPHABET.substring(i2,i2+1));
				if (symbol.length()>1) {
					var.append(symbol.substring(i+1));
				}
				if (force || knownSymbolsContains(var.toString(),context)) {
					variations.add(var);
				}
			}
		}
	}

	private void addDeletes(List<ZStringBuilder> variations,ZStringBuilder symbol,String context,boolean force) {
		if (symbol.length()>1) {
			for (int i = 0; i<symbol.length(); i++) {
				ZStringBuilder var = new ZStringBuilder();
				if (i==0) {
					var.append(symbol.substring(i+1));
				} else {
					var.append(symbol.substring(0,i));
					var.append(symbol.substring(i+1));
				}
				if (var.length()>0 && (force || knownSymbolsContains(var.toString(),context))) {
					variations.add(var);
				}
			}
		}
	}

	private void addSwitches(List<ZStringBuilder> variations,ZStringBuilder symbol,String context,boolean force) {
		if (symbol.length()>1) {
			for (int i = 0; i<(symbol.length() - 1); i++) {
				ZStringBuilder var = new ZStringBuilder();
				if (i>0){
					var.append(symbol.substring(0,i));
				}
				var.append(symbol.substring(i+1,i+2));
				var.append(symbol.substring(i,i+1));
				var.append(symbol.substring(i+2));
				if (!symbol.equals(var) && var.length()>0 && (force || knownSymbolsContains(var.toString(),context))) {
					variations.add(var);
				}
			}
		}
	}
	
	private void addReplacements(List<ZStringBuilder> variations,ZStringBuilder symbol,String context,boolean force) {
		for (int i = 0; i<symbol.length(); i++) {
			for (int i2 = 0; i2<ALPHABET.length(); i2++) {
				ZStringBuilder var = new ZStringBuilder();
				var.append(symbol.substring(0,i));
				var.append(ALPHABET.substring(i2,i2+1));
				if (symbol.length()>1) {
					var.append(symbol.substring(i+1));
				}
				if (!symbol.equals(var) && (force || knownSymbolsContains(var.toString(),context))) {
					variations.add(var);
				}
			}
		}
	}

	private void addCases(List<ZStringBuilder> variations,ZStringBuilder symbol,String context,boolean force) {
		ZStringBuilder var = null;
		var = new ZStringBuilder(symbol);
		var.toCase(true);
		if (!var.equals(symbol) && (force || knownSymbolsContains(var.toString(),context))) {
			variations.add(var);
		}
		var = new ZStringBuilder(symbol);
		var.toCase(false);
		if (!var.equals(symbol) && (force || knownSymbolsContains(var.toString(),context))) {
			variations.add(var);
		}
		var = new ZStringBuilder(symbol.substring(0,1).toCase(false));
		if (symbol.length()>1) {
			var.append(symbol.substring(1));
		}
		if (!var.equals(symbol) && (force || knownSymbolsContains(var.toString(),context))) {
			variations.add(var);
		}
	}
}
