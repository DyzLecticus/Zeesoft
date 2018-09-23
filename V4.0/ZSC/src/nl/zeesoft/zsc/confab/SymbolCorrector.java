package nl.zeesoft.zsc.confab;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

public class SymbolCorrector {
	private static final int		MAX_SYMBOL_LENGTH		= 40;
	private static final int		MAX_SECONDARY_LENGTH	= 32;
	
	public static final String		ALPHABET				= "abcdefghijklmnopqrstuvwxyz";

	public List<String> getVariations(String symbol,List<String> knownSymbols,Date started,long stopAfterMs,String alphabet) {
		if (alphabet==null || alphabet.length()==0) {
			alphabet = ALPHABET;
		}
		List<String> r = new ArrayList<String>();
		if (symbol.length()<MAX_SYMBOL_LENGTH) {
			List<ZStringBuilder> variations = generateVariations(symbol,knownSymbols,started,stopAfterMs,alphabet);
			for (ZStringBuilder var: variations) {
				String sym = var.toString();
				if (!sym.equals(symbol) && knownSymbols.contains(sym) && !r.contains(sym)) {
					r.add(sym);
				}
			}
			if (r.size()==0 && symbol.length()<=MAX_SECONDARY_LENGTH &&
				(started==null || stopAfterMs==0 || ((new Date()).getTime() - started.getTime())<stopAfterMs)
				) {
				variations = addVariations(variations,knownSymbols,started,stopAfterMs,alphabet);
				for (ZStringBuilder var: variations) {
					String sym = var.toString();
					if (!sym.equals(symbol) && knownSymbols.contains(sym) && !r.contains(sym)) {
						r.add(sym);
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
	 * @param alphabet The optional lower cased alphabet to use
	 * @return The list of primary variations
	 */
	public List<ZStringBuilder> generateVariations(String symbol,List<String> knownSymbols,Date started,long stopAfterMs,String alphabet) {
		if (alphabet==null || alphabet.length()==0) {
			alphabet = ALPHABET;
		}
		List<ZStringBuilder> r = new ArrayList<ZStringBuilder>();
		ZStringBuilder sym = new ZStringBuilder(symbol);
		addAdditions(r,sym,knownSymbols,true,alphabet);
		if (started==null || stopAfterMs==0 || ((new Date()).getTime() - started.getTime())<stopAfterMs) {
			addDeletes(r,sym,knownSymbols,true);
		}
		if (started==null || stopAfterMs==0 || ((new Date()).getTime() - started.getTime())<stopAfterMs) {
			addSwitches(r,sym,knownSymbols,true);
		}
		if (started==null || stopAfterMs==0 || ((new Date()).getTime() - started.getTime())<stopAfterMs) {
			addReplacements(r,sym,knownSymbols,true,alphabet);
		}
		if (started==null || stopAfterMs==0 || ((new Date()).getTime() - started.getTime())<stopAfterMs) {
			addCases(r,sym,knownSymbols,true);
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
	 * @param alphabet The optional lower cased alphabet to use
	 * @return The list of primary and secondary variations
	 */
	public List<ZStringBuilder> addVariations(List<ZStringBuilder> variations,List<String> knownSymbols,Date started,long stopAfterMs,String alphabet) {
		if (alphabet==null || alphabet.length()==0) {
			alphabet = ALPHABET;
		}
		List<ZStringBuilder> currentVariations = new ArrayList<ZStringBuilder>(variations);
		int i = 0;
		boolean timedOut = false;
		for (ZStringBuilder sym: currentVariations) {
			if (!timedOut) {
				if (started==null || stopAfterMs==0 || ((new Date()).getTime() - started.getTime())<stopAfterMs) {
					addAdditions(variations,sym,knownSymbols,false,alphabet);
					addDeletes(variations,sym,knownSymbols,false);
					addSwitches(variations,sym,knownSymbols,false);
					addReplacements(variations,sym,knownSymbols,false,alphabet);
					addCases(variations,sym,knownSymbols,false);
				} else {
					timedOut = true;
				}
			}
			if (!knownSymbols.contains(sym.toString())) {
				variations.remove(i);
			} else {
				i++;
			}
		}
		return variations;
	}

	private void addAdditions(List<ZStringBuilder> variations,ZStringBuilder symbol,List<String> knownSymbols,boolean force,String alphabet) {
		for (int i = 0; i<symbol.length(); i++) {
			for (int i2 = 0; i2<alphabet.length(); i2++) {
				ZStringBuilder var = new ZStringBuilder();
				if (i==0) {
					var.append(alphabet.substring(i2,i2+1));
					var.append(symbol);
					if (force || knownSymbols.contains(var.toString())) {
						variations.add(var);
					}
				}
				var = new ZStringBuilder();
				var.append(symbol.substring(0,i+1));
				var.append(alphabet.substring(i2,i2+1));
				if (symbol.length()>1) {
					var.append(symbol.substring(i+1));
				}
				if (force || knownSymbols.contains(var.toString())) {
					variations.add(var);
				}
			}
		}
	}

	private void addDeletes(List<ZStringBuilder> variations,ZStringBuilder symbol,List<String> knownSymbols,boolean force) {
		if (symbol.length()>1) {
			for (int i = 0; i<symbol.length(); i++) {
				ZStringBuilder var = new ZStringBuilder();
				if (i==0) {
					var.append(symbol.substring(i+1));
				} else {
					var.append(symbol.substring(0,i));
					var.append(symbol.substring(i+1));
				}
				if (var.length()>0 && (force || knownSymbols.contains(var.toString()))) {
					variations.add(var);
				}
			}
		}
	}

	private void addSwitches(List<ZStringBuilder> variations,ZStringBuilder symbol,List<String> knownSymbols,boolean force) {
		if (symbol.length()>1) {
			for (int i = 0; i<(symbol.length() - 1); i++) {
				ZStringBuilder var = new ZStringBuilder();
				if (i>0){
					var.append(symbol.substring(0,i));
				}
				var.append(symbol.substring(i+1,i+2));
				var.append(symbol.substring(i,i+1));
				var.append(symbol.substring(i+2));
				if (!symbol.equals(var) && var.length()>0 && (force || knownSymbols.contains(var.toString()))) {
					variations.add(var);
				}
			}
		}
	}
	
	private void addReplacements(List<ZStringBuilder> variations,ZStringBuilder symbol,List<String> knownSymbols,boolean force,String alphabet) {
		for (int i = 0; i<symbol.length(); i++) {
			for (int i2 = 0; i2<alphabet.length(); i2++) {
				ZStringBuilder var = new ZStringBuilder();
				var.append(symbol.substring(0,i));
				var.append(alphabet.substring(i2,i2+1));
				if (symbol.length()>1) {
					var.append(symbol.substring(i+1));
				}
				if (!symbol.equals(var) && (force || knownSymbols.contains(var.toString()))) {
					variations.add(var);
				}
			}
		}
	}

	private void addCases(List<ZStringBuilder> variations,ZStringBuilder symbol,List<String> knownSymbols,boolean force) {
		ZStringBuilder var = null;
		var = new ZStringBuilder(symbol);
		var.toCase(true);
		if (!var.equals(symbol) && (force || knownSymbols.contains(var.toString()))) {
			variations.add(var);
		}
		var = new ZStringBuilder(symbol);
		var.toCase(false);
		if (!var.equals(symbol) && (force || knownSymbols.contains(var.toString()))) {
			variations.add(var);
		}
		var = new ZStringBuilder(symbol.substring(0,1).toCase(false));
		if (symbol.length()>1) {
			var.append(symbol.substring(1));
		}
		if (!var.equals(symbol) && (force || knownSymbols.contains(var.toString()))) {
			variations.add(var);
		}
	}
}
