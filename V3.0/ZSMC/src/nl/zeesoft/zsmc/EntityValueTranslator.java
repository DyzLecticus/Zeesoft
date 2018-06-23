package nl.zeesoft.zsmc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsmc.entity.EntityObject;
import nl.zeesoft.zsmc.entity.UniversalAlphabetic;
import nl.zeesoft.zsmc.entity.UniversalNumeric;
import nl.zeesoft.zsmc.entity.UniversalTime;
import nl.zeesoft.zsmc.entity.dutch.DutchDate;
import nl.zeesoft.zsmc.entity.dutch.DutchDuration;
import nl.zeesoft.zsmc.entity.dutch.DutchMonth;
import nl.zeesoft.zsmc.entity.dutch.DutchNumeric;
import nl.zeesoft.zsmc.entity.dutch.DutchOrder;
import nl.zeesoft.zsmc.entity.dutch.DutchPreposition;
import nl.zeesoft.zsmc.entity.dutch.DutchTime;
import nl.zeesoft.zsmc.entity.english.EnglishDate;
import nl.zeesoft.zsmc.entity.english.EnglishDuration;
import nl.zeesoft.zsmc.entity.english.EnglishMonth;
import nl.zeesoft.zsmc.entity.english.EnglishNumeric;
import nl.zeesoft.zsmc.entity.english.EnglishOrder;
import nl.zeesoft.zsmc.entity.english.EnglishOrder2;
import nl.zeesoft.zsmc.entity.english.EnglishPreposition;
import nl.zeesoft.zsmc.entity.english.EnglishTime;

public class EntityValueTranslator {
	private List<EntityObject>					entities			= new ArrayList<EntityObject>();
	
	private int									maximumSymbols		= 1;

	public EntityValueTranslator() {
		addDefaultEntities();
	}

	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence) {
		return translateToInternalValues(sequence,null,null);
	}

	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,String language) {
		List<String> languages = null;
		if (language.length()>0) {
			languages = new ArrayList<String>();
			languages.add(language);
		}
		return translateToInternalValues(sequence,languages,null);
	}
	
	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,List<String> types) {
		return translateToInternalValues(sequence,null,types);
	}

	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,List<String> languages,List<String> types) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		List<String> symbols = sequence.toSymbolsPunctuated();
		for (int i = 0; i<symbols.size(); i++) {
			int testNum = maximumSymbols;
			if ((i + testNum - 1) >= symbols.size()) {
				testNum = symbols.size() - i;
			}
			boolean translated = false;
			for (int t = testNum; t>0; t--) {
				int from = i;
				int to = i + (t - 1);
				if (to>=symbols.size()) {
					to = (symbols.size() - 1);
				}
				if (to>=from) {
					ZStringBuilder test = new ZStringBuilder();
					for (int s = from; s<=to; s++) {
						if (test.length()>0) {
							test.append(" ");
						}
						test.append(symbols.get(s));
					}
					String raw = test.toString();
					ZStringBuilder ivs = getInternalValuesForExternalValue(
						test.toCase(true).toString(),raw,((to + 1) - from),languages,types
						);
					if (ivs.length()>0) {
						if (r.length()>0) {
							r.append(" ");
						}
						r.append(ivs);
						translated = true;
						i = to;
						break;
					}
				}
			}
			if (!translated) {
				if (r.length()>0) {
					r.append(" ");
				}
				r.append(symbols.get(i));
			}
		}
		return r;
	}

	public ZStringSymbolParser translateToExternalValues(ZStringSymbolParser sequence) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		List<String> symbols = sequence.toSymbolsPunctuated();
		for (int i = 0; i<symbols.size(); i++) {
			String sym = symbols.get(i);
			String ext = getExternalValueForInternalValues(sym);
			if (ext.length()>0) {
				sym = ext;
			}
			if (r.length()>0) {
				r.append(" ");
			}
			r.append(sym);
		}
		return r;
	}

	public EntityObject getEntityObject(String language,String type) {
		EntityObject r = null;
		for (EntityObject eo: entities) {
			if (eo.getLanguage().equals(language) && eo.getType().equals(type)) {
				r = eo;
				break;
			}
		}
		return r;
	}

	public EntityObject getEntityObject(String prefix) {
		EntityObject r = null;
		for (EntityObject eo: entities) {
			if (eo.getInternalValuePrefix().equals(prefix)) {
				r = eo;
				break;
			}
		}
		return r;
	}

	public List<EntityObject> getEntities() {
		return entities;
	}
	
	public void initialize() {
		for (EntityObject eo: entities) {
			if (!eo.isInitialized()) {
				eo.initialize(this);
				if (eo.getMaximumSymbols()>maximumSymbols) {
					maximumSymbols = eo.getMaximumSymbols();
				}
			}
		}
	}

	public void addDefaultEntities() {
		entities.add(new EnglishDate());
		entities.add(new DutchDate());
		entities.add(new EnglishTime());
		entities.add(new DutchTime());
		entities.add(new EnglishDuration());
		entities.add(new DutchDuration());
		entities.add(new EnglishMonth());
		entities.add(new DutchMonth());
		entities.add(new EnglishOrder());
		entities.add(new EnglishOrder2());
		entities.add(new DutchOrder());
		entities.add(new EnglishNumeric());
		entities.add(new DutchNumeric());
		entities.add(new EnglishPreposition());
		entities.add(new DutchPreposition());
		entities.add(new UniversalTime());
		entities.add(new UniversalNumeric());
		entities.add(new UniversalAlphabetic());
	}

	/**
	 * Specifies the value concatenator.
	 * 
	 * @return The value concatenator
	 */
	public String getValueConcatenator() {
		return ":";
	}

	/**
	 * Specifies the or concatenator.
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
	 * Specifies the maximum order string pattern to generate (starting at first).
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

	private ZStringBuilder getInternalValuesForExternalValue(String str, String raw, int numSymbols,List<String> languages,List<String> types) {
		ZStringBuilder r = new ZStringBuilder();
		for (EntityObject eo: entities) {
			if (eo.getMaximumSymbols()>=numSymbols) {
				if ((languages==null || languages.size()==0 || languages.contains(eo.getLanguage())) &&
					(types==null || types.size()==0 || types.contains(eo.getType()))
					) {
					String iv = "";
					if (eo instanceof UniversalAlphabetic) {
						iv = eo.getInternalValueForExternalValue(raw);
					} else {
						iv = eo.getInternalValueForExternalValue(str);
					}
					if (iv.length()>0) {
						if (r.length()>0) {
							r.append(getOrConcatenator());
						}
						r.append(iv);
					}
				}
			}
		}
		return r;
	}
	
	private String getExternalValueForInternalValues(String str) {
		String r = "";
		if (str.contains(getOrConcatenator())) {
			str = str.split("\\" + getOrConcatenator())[0];
		}
		if (str.contains(getValueConcatenator())) {
			String prefix = str.split(getValueConcatenator())[0] + getValueConcatenator();
			EntityObject eo = getEntityObject(prefix);
			if (eo!=null) {
				r = eo.getExternalValueForInternalValue(str);
			}
		}
		return r;
	}
}
