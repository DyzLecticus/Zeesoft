package nl.zeesoft.zids.dialog.pattern;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zids.database.pattern.PtnDatabaseObject;
import nl.zeesoft.zids.database.pattern.PtnHuman;
import nl.zeesoft.zids.database.pattern.PtnSelf;

public class PtnManager {
	private List<PtnObject> 			patterns 			= new ArrayList<PtnObject>();
	
	public final void initializePatterns() {
		patterns.clear();
		addDefaultPatterns();
		addCustomPatterns(patterns);
		for (PtnObject pattern: patterns) {
			if (pattern.getNumPatternStrings()==0) {
				pattern.initializePatternStrings(this);
			}
		}
	}
	
	public final void initializeDatabasePatterns() {
		List<PtnObject> testPatterns = new ArrayList<PtnObject>();
		for (PtnObject pattern: testPatterns) {
			if (pattern instanceof PtnDatabaseObject) {
				pattern.clearPatternStrings();
				patterns.remove(pattern);
			}
		}
		addDefaultDatabasePatterns();
		addCustomDatabasePatterns(patterns);
		for (PtnObject pattern: patterns) {
			if (pattern.getNumPatternStrings()==0) {
				if (pattern instanceof PtnDatabaseObject) {
					pattern.initializePatternStrings(this);
				}
			}
		}
	}

	public final PtnObject getPatternByClassName(String name) {
		PtnObject ptn = null;
		for (PtnObject pattern: patterns) {
			if (pattern.getClass().getName().equals(name)) {
				ptn = pattern;
				break;
			}
		}
		return ptn;
	}
	
	public final List<PtnObject> getMatchingPatternsForString(String str) {
		List<PtnObject> matches = new ArrayList<PtnObject>();
		for (PtnObject pattern: patterns) {
			if (pattern.stringMatchesPattern(str)) {
				matches.add(pattern);
			}
		}
		return matches;
	}

	public final List<PtnObject> getPatternsForValues(String str) {
		List<PtnObject> ptns = new ArrayList<PtnObject>();
		if (str.indexOf("|")>0) {
			String[] ptnVals = str.split("\\|");
			for (String ptnVal: ptnVals) {
				PtnObject ptn = getPatternForValue(ptnVal);
				if (ptn!=null) {
					ptns.add(ptn);
				}
			}
		} else {
			PtnObject ptn = getPatternForValue(str);
			if (ptn!=null) {
				ptns.add(ptn);
			}
		}
		return ptns;
	}
	
	public final PtnObject getPatternForValue(String str) {
		PtnObject ptn = null;
		if (str.indexOf(":")>0) {
			String[] ptnVal = str.split(":");
			for (PtnObject pattern: patterns) {
				if (pattern.getValuePrefix().equals(ptnVal[0])) {
					ptn = pattern;
					break;
				}
			}
		}
		return ptn;
	}
	
	public final StringBuilder scanAndTranslateInput(StringBuilder input, List<String> expectedTypes) {
		List<String> symbols = Symbol.parseTextSymbols(input,0);
		List<String> translated = new ArrayList<String>();
		int i = 0;
		int skip = 0;
		for (String symbol: symbols) {
			if (skip>0) {
				skip--;
			} else {
				List<PtnObject> matchingPatterns = new ArrayList<PtnObject>();
				SortedMap<String,String> matchingPatternStrings = new TreeMap<String,String>();
				for (PtnObject pattern: patterns) {
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
					for (PtnObject pattern: matchingPatterns) {
						if (symbol.length()>0) {
							symbol += "|";
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
		List<String> symbols = Symbol.parseTextSymbols(output,0);
		output = new StringBuilder();
		for (String symbol: symbols) {
			if (output.length()>0) {
				output.append(" ");
			}
			List<PtnObject> patterns = getPatternsForValues(symbol);
			if (patterns.size()>0) {
				if (symbol.indexOf("|")>0) {
					symbol = symbol.split("\\|")[0];
				}
				output.append(patterns.get(0).getStringForValue(symbol));
			} else {
				output.append(symbol);
			}
		}
		return output;
	}
	
	public final List<PtnObject> getPatterns() {
		return new ArrayList<PtnObject>(patterns);
	}
	
	public final int getNumberValueFromPatternValue(String value) {
		int r = 0;
		PtnObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PtnObject.TYPE_NUMBER)) {
			String str = getStringValueFromPatternValue(pattern,value);
			r = Integer.parseInt(str);
		}
		return r;
	}

	public final boolean getConfirmationValueFromPatternValue(String value) {
		boolean r = false;
		PtnObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PtnObject.TYPE_CONFIRMATION)) {
			String str = getStringValueFromPatternValue(pattern,value);
			r = Boolean.parseBoolean(str);
		}
		return r;
	}

	public final Date getDateValueFromPatternValue(String value) {
		Date r = null;
		PtnObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PtnObject.TYPE_DATE)) {
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
		PtnObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PtnObject.TYPE_TIME)) {
			String str = getStringValueFromPatternValue(pattern,value);
			String[] vals = str.split(":");
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
		PtnObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PtnObject.TYPE_DURATION)) {
			String str = getStringValueFromPatternValue(pattern,value);
			String[] vals = str.split(":");
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
		PtnObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PtnObject.TYPE_PREPOSITION)) {
			r = pattern.getStringForValue(value);
		}
		return r;
	}

	public final String getStringValueFromPatternValue(String value) {
		String r = "";
		PtnObject pattern = getPatternForValue(value);
		if (pattern!=null && pattern.getBaseValueType().equals(PtnObject.TYPE_ALPHABETIC)) {
			r = getStringValueFromPatternValue(pattern,value);
		}
		return r;
	}
	
	public final String getStringValueFromPatternValue(PtnObject pattern, String value) {
		String r = "";
		if (pattern==null) {
			pattern = getPatternForValue(value);
		}
		if (pattern!=null) {
			r = value.substring(pattern.getValuePrefix().length() + 1);
		}
		return r;
	}
	
	protected final void addPattern(PtnObject pattern) {
		patterns.add(pattern);
	}

	protected final void addPattern(int index, PtnObject pattern) {
		patterns.add(index,pattern);
	}

	protected void addCustomPatterns(List<PtnObject> patterns) {
		// Override to modify or extend
	}

	protected void addCustomDatabasePatterns(List<PtnObject> patterns) {
		// Override to modify or extend
	}
	
	protected int getMaximumNumber() {
		return 99999;
	}

	protected int getMaximumOrder() {
		return 999;
	}

	private final void addDefaultPatterns() {
		addPattern(new PtnEnglishConfirmation());
		addPattern(new PtnDutchConfirmation());
		addPattern(new PtnEnglishDate());
		addPattern(new PtnDutchDate());
		addPattern(new PtnEnglishTime());
		addPattern(new PtnDutchTime());
		addPattern(new PtnEnglishDuration());
		addPattern(new PtnDutchDuration());
		addPattern(new PtnEnglishMonth());
		addPattern(new PtnDutchMonth());
		addPattern(new PtnEnglishOrder());
		addPattern(new PtnEnglishOrder2());
		addPattern(new PtnDutchOrder());
		addPattern(new PtnEnglishNumber());
		addPattern(new PtnDutchNumber());
		addPattern(new PtnEnglishPreposition());
		addPattern(new PtnDutchPreposition());
		addPattern(new PtnUniversalTime());
		addPattern(new PtnUniversalNumber());
		addPattern(new PtnUniversalAlphabetic());
	}

	private final void addDefaultDatabasePatterns() {
		addPattern(0,new PtnHuman());
		addPattern(0,new PtnSelf());
	}
}
