package nl.zeesoft.zspr.pattern;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.SymbolParser;
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

public class PatternManager {	
	private List<PatternObject>		patterns	= new ArrayList<PatternObject>();

	protected void addCustomPatterns(List<PatternObject> patterns) {
		// Override to modify or extend
	}
	
	public String getValueConcatenator() {
		return ":";
	}

	public String getOrConcatenator() {
		return "|";
	}
	
	public int getMaximumNumber() {
		return 99999;
	}

	public int getMaximumOrder() {
		return 999;
	}

	public Date getCurrentDate() {
		return new Date();
	}
	
	public final void initializePatterns() {
		patterns.clear();
		addDefaultPatterns();
		addCustomPatterns(patterns);
		for (PatternObject pattern: patterns) {
			pattern.setConcatenators(getValueConcatenator(),getOrConcatenator());
			if (pattern.getNumPatternStrings()==0) {
				pattern.initializePatternStrings(this);
			}
		}
	}
	
	public final PatternObject getPatternByClassName(String name) {
		PatternObject ptn = null;
		for (PatternObject pattern: patterns) {
			if (pattern.getClass().getName().equals(name)) {
				ptn = pattern;
				break;
			}
		}
		return ptn;
	}
	
	public final List<PatternObject> getMatchingPatternsForString(String str) {
		List<PatternObject> matches = new ArrayList<PatternObject>();
		for (PatternObject pattern: patterns) {
			if (pattern.stringMatchesPattern(str)) {
				matches.add(pattern);
			}
		}
		return matches;
	}

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
	
	public final PatternObject getPatternForValue(String str) {
		PatternObject ptn = null;
		if (str.indexOf(getValueConcatenator())>0) {
			String[] ptnVal = str.split(getValueConcatenator());
			for (PatternObject pattern: patterns) {
				if (pattern.getValuePrefix().equals(ptnVal[0])) {
					ptn = pattern;
					break;
				}
			}
		}
		return ptn;
	}
	
	public final StringBuilder scanAndTranslateInput(StringBuilder input, List<String> expectedTypes) {
		List<String> symbols = SymbolParser.parseSymbols(input);
		List<String> translated = new ArrayList<String>();
		int i = 0;
		int skip = 0;
		for (String symbol: symbols) {
			if (skip>0) {
				skip--;
			} else {
				List<PatternObject> matchingPatterns = new ArrayList<PatternObject>();
				SortedMap<String,String> matchingPatternStrings = new TreeMap<String,String>();
				for (PatternObject pattern: patterns) {
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
		input = new StringBuilder();
		for (String symbol: symbols) {
			if (input.length()>0) {
				input.append(" ");
			}
			input.append(symbol);
		}
		return input;
	}

	public final StringBuilder scanAndTranslateOutput(StringBuilder output) {
		List<String> symbols = SymbolParser.parseSymbols(output);
		output = new StringBuilder();
		for (String symbol: symbols) {
			if (output.length()>0) {
				output.append(" ");
			}
			List<PatternObject> patterns = getPatternsForValues(symbol);
			if (patterns.size()>0) {
				if (symbol.indexOf(getOrConcatenator())>0) {
					symbol = symbol.split("\\" + getOrConcatenator())[0];
				}
				output.append(patterns.get(0).getStringForValue(symbol));
			} else {
				output.append(symbol);
			}
		}
		return output;
	}
	
	public final List<PatternObject> getPatterns() {
		return new ArrayList<PatternObject>(patterns);
	}
	
	public final int getNumberValueFromPatternValue(String value) {
		int r = 0;
		PatternObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PatternObject.TYPE_NUMBER)) {
			String str = getStringValueFromPatternValue(pattern,value);
			r = Integer.parseInt(str);
		}
		return r;
	}

	public final boolean getConfirmationValueFromPatternValue(String value) {
		boolean r = false;
		PatternObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PatternObject.TYPE_CONFIRMATION)) {
			String str = getStringValueFromPatternValue(pattern,value);
			r = Boolean.parseBoolean(str);
		}
		return r;
	}

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

	public final String getPrepositionStringValueFromPatternValue(String value) {
		String r = "";
		PatternObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PatternObject.TYPE_PREPOSITION)) {
			r = pattern.getStringForValue(value);
		}
		return r;
	}

	public final String getStringValueFromPatternValue(String value) {
		String r = "";
		PatternObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PatternObject.TYPE_ALPHABETIC)) {
			r = getStringValueFromPatternValue(pattern,value);
		}
		return r;
	}
	
	public final String getStringValueFromPatternValue(PatternObject pattern, String value) {
		String r = "";
		if (pattern==null) {
			pattern = getPatternForValue(value);
		}
		if (pattern!=null) {
			r = value.substring(pattern.getValuePrefix().length() + 1);
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
}
