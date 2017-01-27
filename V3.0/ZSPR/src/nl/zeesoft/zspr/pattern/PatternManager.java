package nl.zeesoft.zspr.pattern;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.SymbolParser;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zspr.pattern.patterns.UniversalAlphabetic;
import nl.zeesoft.zspr.pattern.patterns.UniversalNumber;
import nl.zeesoft.zspr.pattern.patterns.UniversalTime;
import nl.zeesoft.zspr.pattern.patterns.dutch.DutchConfirmation;
import nl.zeesoft.zspr.pattern.patterns.dutch.DutchDate;
import nl.zeesoft.zspr.pattern.patterns.dutch.DutchDuration;
import nl.zeesoft.zspr.pattern.patterns.dutch.DutchMonth;
import nl.zeesoft.zspr.pattern.patterns.dutch.DutchNumber;
import nl.zeesoft.zspr.pattern.patterns.dutch.DutchOrder;
import nl.zeesoft.zspr.pattern.patterns.dutch.DutchPreposition;
import nl.zeesoft.zspr.pattern.patterns.dutch.DutchTime;
import nl.zeesoft.zspr.pattern.patterns.english.EnglishConfirmation;
import nl.zeesoft.zspr.pattern.patterns.english.EnglishDate;
import nl.zeesoft.zspr.pattern.patterns.english.EnglishDuration;
import nl.zeesoft.zspr.pattern.patterns.english.EnglishMonth;
import nl.zeesoft.zspr.pattern.patterns.english.EnglishNumber;
import nl.zeesoft.zspr.pattern.patterns.english.EnglishOrder;
import nl.zeesoft.zspr.pattern.patterns.english.EnglishOrder2;
import nl.zeesoft.zspr.pattern.patterns.english.EnglishPreposition;
import nl.zeesoft.zspr.pattern.patterns.english.EnglishTime;

/**
 * A PatternManager provides an extensible, thread safe object for translating symbolic sequences into primary value objects.
 */
public class PatternManager extends Locker {	
	private List<PatternObject>		patterns	= new ArrayList<PatternObject>();

	/**
	 * Override this to modify or extend the default patterns.
	 * 
	 * The order of the patterns determines the order of the pattern matching search results.  
	 * 
	 * @param patterns The default patterns provided by the pattern manager.
	 */
	protected void addCustomPatterns(List<PatternObject> patterns) {
		// Override to modify or extend
	}
	
	/**
	 * Specifies how the pattern manager should concatenate values to pattern object value prefixes.
	 * 
	 * @return The value concatenator 
	 */
	public String getValueConcatenator() {
		return ":";
	}

	/**
	 * Specifies how the pattern manager should concatenate multiple pattern values for the same string. 
	 * 
	 * @return The or concatenator 
	 */
	public String getOrConcatenator() {
		return "|";
	}
	
	/**
	 * Specifies the maximum number string pattern to generate (starting at zero).
	 * 
	 * In order to support current and future dates, this should be at least 9999.
	 * 
	 * @return The maximum number string pattern to generate
	 */
	public int getMaximumNumber() {
		return 99999;
	}

	/**
	 * Specifies the maximum order string pattern to generate (starting at zero).
	 * 
	 * In order to support current and future dates, this should be at least 99.
	 * 
	 * @return The maximum order string pattern to generate
	 */
	public int getMaximumOrder() {
		return 999;
	}

	/**
	 * Specifies the current date (default = new Date()).
	 * 
	 * Used to translate patterns like 'now', 'today', 'tomorrow'.
	 * 
	 * @return The current date
	 */
	public Date getCurrentDate() {
		return new Date();
	}
	
	/**
	 * Call this method to (re)initialize all the patterns defined for this pattern manager.
	 */
	public final void initializePatterns() {
		lockMe(this);
		patterns.clear();
		addDefaultPatterns();
		addCustomPatterns(patterns);
		unlockMe(this);
		for (PatternObject pattern: getPatterns()) {
			pattern.setConcatenators(getValueConcatenator(),getOrConcatenator());
			if (pattern.getNumPatternStrings()==0) {
				pattern.initializePatternStrings(this);
			}
		}
	}
	
	/**
	 * Returns the specified pattern or null if it does not exist.
	 * 
	 * @param name The full class name (including package) of the pattern
	 * @return The pattern or null
	 */
	public final PatternObject getPatternByClassName(String name) {
		PatternObject ptn = null;
		for (PatternObject pattern: getPatterns()) {
			if (pattern.getClass().getName().equals(name)) {
				ptn = pattern;
				break;
			}
		}
		return ptn;
	}
	
	/**
	 * Returns a list of patterns that match a certain string.
	 * 
	 * These patterns can then be used to translate the string into values.
	 * 
	 * @param str The string
	 * @return A list of patterns that match a certain string
	 */
	public final List<PatternObject> getMatchingPatternsForString(String str) {
		List<PatternObject> matches = new ArrayList<PatternObject>();
		for (PatternObject pattern: getPatterns()) {
			if (pattern.stringMatchesPattern(str)) {
				matches.add(pattern);
			}
		}
		return matches;
	}

	/**
	 * Returns a list of patterns that can translate certain values.
	 *
	 * These patterns can then be used to translate the values back into string representations.
	 * 
	 * @param str The values
	 * @return A list of patterns that can translate certain values
	 */
	public final List<PatternObject> getPatternsForValues(String str) {
		List<PatternObject> ptns = new ArrayList<PatternObject>();
		if (str.indexOf(getOrConcatenator())>0) {
			String[] ptnVals = str.split("\\|");
			for (String ptnVal: ptnVals) {
				PatternObject ptn = getPatternForValue(ptnVal);
				if (ptn!=null) {
					ptns.add(ptn);
				}
			}
		} else {
			PatternObject ptn = getPatternForValue(str);
			if (ptn!=null) {
				ptns.add(ptn);
			}
		}
		return ptns;
	}
	
	/**
	 * Scans a symbol sequence and translates it to a primary value object string using all matching patterns.
	 * 
	 * Selects longest matching symbol sequence patterns over shorter symbol sequence patterns.
	 * 
	 * @param sequence The symbol sequence
	 * @param expectedTypes The optional list of expected pattern base value types to limit the translation
	 * @return The translated values
	 */
	public final StringBuilder scanAndTranslateSequence(StringBuilder sequence, List<String> expectedTypes) {
		List<String> symbols = SymbolParser.parseSymbols(sequence);
		List<String> translated = new ArrayList<String>();
		int i = 0;
		int skip = 0;
		for (String symbol: symbols) {
			if (skip>0) {
				skip--;
			} else {
				List<PatternObject> matchingPatterns = new ArrayList<PatternObject>();
				SortedMap<String,String> matchingPatternStrings = new TreeMap<String,String>();
				for (PatternObject pattern: getPatterns()) {
					if (expectedTypes==null || expectedTypes.size()==0 || expectedTypes.contains(pattern.getBaseValueType())) {
						if (pattern.getMaximumSymbols()>1) {
							int max = pattern.getMaximumSymbols();
							int from = i;
							int to = i + max;
							int repeat = max;
							if (to >= symbols.size()) {
								repeat = repeat - (to - symbols.size());
								if (repeat==0) {
									repeat = 1;
								}
								to = symbols.size();
							}
							for (int r = 0; r < repeat; r++) {
								StringBuilder test = new StringBuilder();
								int length = 0;
								for (int si = from; si < (to - r); si++) {
									if (test.length()>0) {
										test.append(" ");
									}
									test.append(symbols.get(si));
									length++;
								}
								if (test.length()>0) {
									if (pattern.stringMatchesPattern(test.toString().toLowerCase())) {
										int nSkip = (length - 1); 
										if (nSkip > skip) {
											matchingPatterns.clear();
											matchingPatternStrings.clear();
											matchingPatterns.add(pattern);
											matchingPatternStrings.put(pattern.getClass().getName(),test.toString());
											skip = nSkip;
										} else if (nSkip == skip) {
											matchingPatterns.add(pattern);
											matchingPatternStrings.put(pattern.getClass().getName(),test.toString());
										}
										break;
									}
								}
							}
						} else if (pattern.stringMatchesPattern(symbol.toLowerCase()) && skip<=0) {
							matchingPatterns.add(pattern);
							matchingPatternStrings.put(pattern.getClass().getName(),symbol);
						}
					}
				}
				if (matchingPatterns.size()>0) {
					symbol = "";
					for (PatternObject pattern: matchingPatterns) {
						if (symbol.length()>0) {
							symbol += getOrConcatenator();
						}
						String value = matchingPatternStrings.get(pattern.getClass().getName());
						if (pattern.getNumPatternStrings()>0) {
							value = value.toLowerCase();
						}
						symbol += pattern.getValueForString(value);
					}
				}
				translated.add(symbol);
			}
			i++;
		}
		symbols = translated;
		sequence = new StringBuilder();
		for (String symbol: symbols) {
			if (sequence.length()>0) {
				sequence.append(" ");
			}
			sequence.append(symbol);
		}
		return sequence;
	}

	/**
	 * Scans and translates values to a symbol sequence.
	 * 
	 * In case of multiple value representations the first matching pattern value prefix will be used.
	 * 
	 * @param values A string representation of the values
	 * @return The translated symbol sequence
	 */
	public final StringBuilder scanAndTranslateValues(StringBuilder values) {
		List<String> symbols = SymbolParser.parseSymbols(values);
		values = new StringBuilder();
		for (String symbol: symbols) {
			if (values.length()>0) {
				values.append(" ");
			}
			List<PatternObject> patterns = getPatternsForValues(symbol);
			if (patterns.size()>0) {
				if (symbol.indexOf(getOrConcatenator())>0) {
					symbol = symbol.split("\\" + getOrConcatenator())[0];
				}
				values.append(patterns.get(0).getStringForValue(symbol));
			} else {
				values.append(symbol);
			}
		}
		return values;
	}
	
	/**
	 * Returns a read only list of patterns.
	 * 
	 * @return A read only list of patterns
	 */
	public final List<PatternObject> getPatterns() {
		lockMe(this);
		List<PatternObject> r = new ArrayList<PatternObject>(patterns);
		unlockMe(this);
		return r;
	}
	
	/**
	 * Helper function to easily translate a value to an integer primitive 
	 * 
	 * @param value The value string
	 * @return The integer primitive
	 */
	public final int getNumberValueFromPatternValue(String value) {
		int r = 0;
		PatternObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PatternObject.TYPE_NUMBER)) {
			String str = getStringValueFromPatternValue(pattern,value);
			r = Integer.parseInt(str);
		}
		return r;
	}

	/**
	 * Helper function to easily translate a value to a boolean primitive 
	 * 
	 * @param value The value string
	 * @return The boolean primitive
	 */
	public final boolean getConfirmationValueFromPatternValue(String value) {
		boolean r = false;
		PatternObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PatternObject.TYPE_CONFIRMATION)) {
			String str = getStringValueFromPatternValue(pattern,value);
			r = Boolean.parseBoolean(str);
		}
		return r;
	}

	/**
	 * Helper function to easily translate a value to a Date object 
	 * 
	 * @param value The value string
	 * @return The Date object
	 */
	public final Date getDateValueFromPatternValue(String value) {
		Date r = null;
		PatternObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PatternObject.TYPE_DATE)) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY,0);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MILLISECOND,0);
			
			String str = getStringValueFromPatternValue(pattern,value);
			String[] vals = str.split("-");
			for (int i = 0; i<vals.length; i++) {
				int val = Integer.parseInt(vals[i]);
				if (i==0) {
					cal.set(Calendar.YEAR,val);
				} else if (i==1) {
					cal.set(Calendar.MONTH,(val - 1));
				} else if (i==2) {
					cal.set(Calendar.DATE,val);
				}
			}

			r = cal.getTime();
		}
		return r;
	}

	/**
	 * Helper function to easily translate a time value to a long primitive 
	 * 
	 * @param value The time value string
	 * @return The long primitive
	 */
	public final long getTimeValueFromPatternValue(String value) {
		long r = 0;
		PatternObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PatternObject.TYPE_TIME)) {
			String str = getStringValueFromPatternValue(pattern,value);
			String[] vals = str.split(getValueConcatenator());
			for (int i = 0; i<vals.length; i++) {
				long val = Long.parseLong(vals[i]);
				if (i==0) {
					r += (val * 60 * 60 * 1000); 
				} else if (i==1) {
					r += (val * 60 * 1000); 
				} else if (i==2) {
					r += (val * 1000); 
				}
			}
		}
		return r;
	}

	/**
	 * Helper function to easily translate a duration value to a long primitive 
	 * 
	 * @param value The duration value string
	 * @return The long primitive
	 */
	public final long getDurationValueFromPatternValue(String value) {
		long r = 0;
		PatternObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PatternObject.TYPE_DURATION)) {
			String str = getStringValueFromPatternValue(pattern,value);
			String[] vals = str.split(getValueConcatenator());
			for (int i = 0; i<vals.length; i++) {
				long val = Long.parseLong(vals[i]);
				if (i==0) {
					r += (val * 60 * 60 * 1000); 
				} else if (i==1) {
					r += (val * 60 * 1000); 
				}
			}
		}
		return r;
	}

	/**
	 * Helper function to easily translate a preposition value 
	 * 
	 * @param value The value string
	 * @return The preposition
	 */
	public final String getPrepositionStringValueFromPatternValue(String value) {
		String r = "";
		PatternObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PatternObject.TYPE_PREPOSITION)) {
			r = pattern.getStringForValue(value);
		}
		return r;
	}

	/**
	 * Helper function to easily translate a string value 
	 * 
	 * @param value The value string
	 * @return The string
	 */
	public final String getStringValueFromPatternValue(String value) {
		String r = "";
		PatternObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PatternObject.TYPE_ALPHABETIC)) {
			r = getStringValueFromPatternValue(pattern,value);
		}
		return r;
	}

	protected final void addPattern(PatternObject pattern) {
		patterns.add(pattern);
	}
	
	protected final void addPattern(int index, PatternObject pattern) {
		patterns.add(index,pattern);
	}

	private final void addDefaultPatterns() {
		addPattern(new EnglishConfirmation());
		addPattern(new DutchConfirmation());
		addPattern(new EnglishDate());
		addPattern(new DutchDate());
		addPattern(new EnglishTime());
		addPattern(new DutchTime());
		addPattern(new EnglishDuration());
		addPattern(new DutchDuration());
		addPattern(new EnglishMonth());
		addPattern(new DutchMonth());
		addPattern(new EnglishOrder());
		addPattern(new EnglishOrder2());
		addPattern(new DutchOrder());
		addPattern(new EnglishNumber());
		addPattern(new DutchNumber());
		addPattern(new EnglishPreposition());
		addPattern(new DutchPreposition());
		addPattern(new UniversalTime());
		addPattern(new UniversalNumber());
		addPattern(new UniversalAlphabetic());
	}

	private String getStringValueFromPatternValue(PatternObject pattern, String value) {
		String r = "";
		if (pattern==null) {
			pattern = getPatternForValue(value);
		}
		if (pattern!=null) {
			r = value.substring(pattern.getValuePrefix().length() + 1);
		}
		return r;
	}
	
	private PatternObject getPatternForValue(String str) {
		PatternObject ptn = null;
		if (str.indexOf(getValueConcatenator())>0) {
			String[] ptnVal = str.split(getValueConcatenator());
			for (PatternObject pattern: getPatterns()) {
				if (pattern.getValuePrefix().equals(ptnVal[0])) {
					ptn = pattern;
					break;
				}
			}
		}
		return ptn;
	}
}
