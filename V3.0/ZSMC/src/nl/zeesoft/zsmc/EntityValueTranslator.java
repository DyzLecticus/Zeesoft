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
import nl.zeesoft.zsmc.entity.complex.ComplexObject;
import nl.zeesoft.zsmc.entity.complex.dutch.DutchName;
import nl.zeesoft.zsmc.entity.complex.english.EnglishName;
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
		return translateToInternalValues(sequence,"","",true);
	}

	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,String language) {
		return translateToInternalValues(sequence,language,"",true);
	}

	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,String language,String type) {
		return translateToInternalValues(sequence,language,type,true);
	}

	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,String language,String type,boolean doComplex) {
		List<String> languages = null;
		List<String> types = null;
		if (language.length()>0) {
			languages = new ArrayList<String>();
			languages.add(language);
		}
		if (type.length()>0) {
			types = new ArrayList<String>();
			types.add(type);
		}
		return translateToInternalValues(sequence,languages,types,doComplex);
	}
	
	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,List<String> types) {
		return translateToInternalValues(sequence,null,types,true);
	}

	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,List<String> languages,List<String> types) {
		return translateToInternalValues(sequence,languages,types,true);
	}

	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,List<String> languages,List<String> types,boolean doComplex) {
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

		if (doComplex) {
			ZStringSymbolParser complex = translateToInternalValuesComplex(r,languages,types);
			if (complex.length()>0) {
				r = complex;
			}
		}
		
		return r;
	}

	public ZStringSymbolParser translateToExternalValues(ZStringSymbolParser sequence) {
		return translateToExternalValues(sequence,"",false);
	}

	public ZStringSymbolParser translateToExternalValues(ZStringSymbolParser sequence,String type,boolean singleOnly) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		List<String> symbols = sequence.toSymbolsPunctuated();
		List<String> newSymbols = new ArrayList<String>();
		for (int i = 0; i<symbols.size(); i++) {
			String sym = symbols.get(i);
			if (!singleOnly || !sym.contains(getOrConcatenator())) {
				String ext = getExternalValueForInternalValues(sym,type);
				if (ext.length()>0) {
					sym = ext;
				}
			}
			newSymbols.add(sym);
		}
		r.fromSymbols(newSymbols,false,false);
		return r;
	}

	public String getInternalValueFromInternalValues(String str,String type) {
		String r = "";
		String[] values = str.split("\\" + getOrConcatenator());
		for (int v = 0; v < values.length; v++) {
			if (values[v].contains(getValueConcatenator())) {
				String prefix = values[v].split(getValueConcatenator())[0] + getValueConcatenator();
				EntityObject eo = getEntityObject(prefix);
				if (eo!=null && (type.length()==0 || eo.getType().equals(type))) {
					r = values[v];
					break;
				}
			}
		}
		return r;
	}

	public String getExternalValueForInternalValues(String str,String type) {
		String r = "";
		String value = getInternalValueFromInternalValues(str,type);
		String prefix = value.split(getValueConcatenator())[0] + getValueConcatenator();
		EntityObject eo = getEntityObject(prefix);
		if (eo!=null && (type.length()==0 || eo.getType().equals(type))) {
			r = eo.getExternalValueForInternalValue(value);
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
		// Complex entities
		entities.add(new EnglishName());
		entities.add(new DutchName());
		// Regular entities
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
			if (!(eo instanceof ComplexObject)) {
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
		}
		return r;
	}

	private ZStringSymbolParser translateToInternalValuesComplex(ZStringSymbolParser entityValueSequence,List<String> languages,List<String> types) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		for (EntityObject eo: entities) {
			if (eo instanceof ComplexObject) {
				if ((languages==null || languages.size()==0 || languages.contains(eo.getLanguage())) &&
					(types==null || types.size()==0 || types.contains(eo.getType()))
					) {
					ComplexObject co = (ComplexObject) eo;
					ZStringSymbolParser t = co.translateToInternalValues(entityValueSequence);
					if (t.length()>0) {
						r = t;
						entityValueSequence = t;
					}
				}
			}
		}
		return r;
	}
}
