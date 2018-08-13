package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.entity.UniversalAlphabetic;
import nl.zeesoft.zsd.entity.UniversalCurrency;
import nl.zeesoft.zsd.entity.UniversalMathematic;
import nl.zeesoft.zsd.entity.UniversalNumeric;
import nl.zeesoft.zsd.entity.UniversalTime;
import nl.zeesoft.zsd.entity.complex.ComplexObject;
import nl.zeesoft.zsd.entity.complex.dutch.DutchName;
import nl.zeesoft.zsd.entity.complex.english.EnglishName;
import nl.zeesoft.zsd.entity.entities.dutch.DutchConfirmation;
import nl.zeesoft.zsd.entity.entities.dutch.DutchCountry;
import nl.zeesoft.zsd.entity.entities.dutch.DutchCurrency;
import nl.zeesoft.zsd.entity.entities.dutch.DutchDate;
import nl.zeesoft.zsd.entity.entities.dutch.DutchDuration;
import nl.zeesoft.zsd.entity.entities.dutch.DutchLanguage;
import nl.zeesoft.zsd.entity.entities.dutch.DutchMathematic;
import nl.zeesoft.zsd.entity.entities.dutch.DutchMonth;
import nl.zeesoft.zsd.entity.entities.dutch.DutchNumeric;
import nl.zeesoft.zsd.entity.entities.dutch.DutchOrder;
import nl.zeesoft.zsd.entity.entities.dutch.DutchPreposition;
import nl.zeesoft.zsd.entity.entities.dutch.DutchProfanity;
import nl.zeesoft.zsd.entity.entities.dutch.DutchTime;
import nl.zeesoft.zsd.entity.entities.english.EnglishConfirmation;
import nl.zeesoft.zsd.entity.entities.english.EnglishCountry;
import nl.zeesoft.zsd.entity.entities.english.EnglishCurrency;
import nl.zeesoft.zsd.entity.entities.english.EnglishDate;
import nl.zeesoft.zsd.entity.entities.english.EnglishDuration;
import nl.zeesoft.zsd.entity.entities.english.EnglishLanguage;
import nl.zeesoft.zsd.entity.entities.english.EnglishMathematic;
import nl.zeesoft.zsd.entity.entities.english.EnglishMonth;
import nl.zeesoft.zsd.entity.entities.english.EnglishNumeric;
import nl.zeesoft.zsd.entity.entities.english.EnglishOrder;
import nl.zeesoft.zsd.entity.entities.english.EnglishOrder2;
import nl.zeesoft.zsd.entity.entities.english.EnglishPreposition;
import nl.zeesoft.zsd.entity.entities.english.EnglishProfanity;
import nl.zeesoft.zsd.entity.entities.english.EnglishTime;
import nl.zeesoft.zsd.initialize.Initializable;

public class EntityValueTranslator implements Initializable {
	private List<EntityObject>					entities			= new ArrayList<EntityObject>();
	
	private int									maximumSymbols		= 1;

	public EntityValueTranslator() {
		addDefaultEntities();
	}

	@Override
	public void initialize(List<ZStringBuilder> data) {
		initialize();
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
	 * Specifies the or concatenator splitter.
	 * 
	 * @return The or concatenator splitter
	 */
	protected String getOrConcatenatorSplitter() {
		return "\\" + getOrConcatenator();
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

	/**
	 * Translates a sequence to internal entity values.
	 * 
	 * @param sequence The sequence
	 * @return The translated sequence
	 */
	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence) {
		return translateToInternalValues(sequence,"","",true);
	}

	/**
	 * Translates a sequence to internal entity values.
	 * 
	 * @param sequence The sequence
	 * @param language An optional entity language to limit entities
	 * @return The translated sequence
	 */
	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,String language) {
		return translateToInternalValues(sequence,language,"",true);
	}

	/**
	 * Translates a sequence to internal entity values.
	 * 
	 * @param sequence The sequence
	 * @param language An optional entity language to limit entities
	 * @param type An optional entity type to limit entities
	 * @return The translated sequence
	 */
	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,String language,String type) {
		return translateToInternalValues(sequence,language,type,true);
	}

	/**
	 * Translates a sequence to internal entity values.
	 * 
	 * @param sequence The sequence
	 * @param language An optional entity language to limit entities
	 * @param type An optional entity type to limit entities
	 * @param doComplex Indicates complex entities are to be translated
	 * @return The translated sequence
	 */
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
	
	/**
	 * Translates a sequence to internal entity values.
	 * 
	 * @param sequence The sequence
	 * @param types An optional list of entity types to limit entities
	 * @return The translated sequence
	 */
	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,List<String> types) {
		return translateToInternalValues(sequence,null,types,true);
	}

	/**
	 * Translates a sequence to internal entity values.
	 * 
	 * @param sequence The sequence
	 * @param languages An optional list of entity languages to limit entities
	 * @param types An optional list of entity types to limit entities
	 * @return The translated sequence
	 */
	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,List<String> languages,List<String> types) {
		return translateToInternalValues(sequence,languages,types,true);
	}

	/**
	 * Translates a sequence to internal entity values.
	 * 
	 * @param sequence The sequence
	 * @param languages An optional list of entity languages to limit entities
	 * @param types An optional list of entity types to limit entities
	 * @param doComplex Indicates complex entities are to be translated
	 * @return The translated sequence
	 */
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
	
	/**
	 * Translates a sequence containing internal entity values to external values.
	 * 
	 * @param sequence The sequence containing internal entity values
	 * @return The translated sequence
	 */
	public ZStringSymbolParser translateToExternalValues(ZStringSymbolParser sequence) {
		return translateToExternalValues(sequence,"",false);
	}

	/**
	 * Translates a sequence containing internal entity values to external values.
	 * 
	 * @param sequence The sequence containing internal entity values
	 * @param type The entity type used to limit entity value translation
	 * @param singleOnly Indicates only unambiguous values are to be translated
	 * @return The translated sequence
	 */
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

	/**
	 * Returns the first value of a specific type from a list of internal entity values.
	 * The value is removed from the input list.
	 * 
	 * @param internalValues The list of internal values
	 * @param internalValuesCorrected The list of corrected internal values
	 * @param type The entity type
	 * @param complexName The optional complex entity name
	 * @param complexType The optional complex entity type
	 * @return The first value or "";
	 */
	public String getTypeValueFromInternalValues(List<String> internalValues,List<String> internalValuesCorrected,String type,String complexName,String complexType) {
		String r = "";
		List<String> vals = new ArrayList<String>(internalValues);
		int i = 0;
		for (String val: vals) {
			String v = "";
			if (complexType.length()>0) {
				v = getInternalValueFromInternalValues(val,complexType);
				String[] str = v.split(getValueConcatenator());
				if (str.length==4 && str[1].equals(complexName)) {
					v = str[2] + getValueConcatenator() + str[3];
				} else {
					v = "";
				}
			} else {
				v = getInternalValueFromInternalValues(val,type);
			}
			if (v.length()>0) {
				if (internalValuesCorrected!=null && internalValuesCorrected.size()==internalValues.size()) {
					internalValuesCorrected.remove(i);
					internalValues.remove(i);
					i--;
				}
				r = v;
				break;
			}
			i++;
		}
		return r;
	}
	
	/**
	 * Returns a specific type of internal entity value from a concatenated set of internal values.
	 * 
	 * @param str A concatenated set of internal values
	 * @param type The type of entity value
	 * @return The internal value or an empty string
	 */
	public String getInternalValueFromInternalValues(String str,String type) {
		String r = "";
		String[] values = str.split(getOrConcatenatorSplitter());
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

	/**
	 * Returns the external value from a concatenated set of internal values.
	 * 
	 * @param str A concatenated set of internal values
	 * @param type The optional entity type to limit value selection 
	 * @return The external value
	 */
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
	
	/**
	 * Returns the type value for an internal value.
	 * 
	 * @param value The internal value
	 * @return The type value or null
	 */
	public Object getTypeValueForInternalValue(String value) {
		Object r = null;
		String prefix = value.split(getValueConcatenator())[0] + getValueConcatenator();
		EntityObject eo = getEntityObject(prefix);
		if (eo!=null) {
			r = eo.getTypeValueForInternalValue(value);
		}
		return r;
	}
	
	/**
	 * Returns the boolean type value for an internal value.
	 * 
	 * @param value The internal value
	 * @param def The default type value
	 * @return The boolean type value or the default type value
	 */
	public boolean getBooleanTypeValueForInternalValue(String value, boolean def) {
		boolean r = def;
		Object o = getTypeValueForInternalValue(value);
		if (o!=null && o instanceof Boolean) {
			r = (Boolean) o;
		}
		return r;
	}

	/**
	 * Returns the long type value for an internal value.
	 * 
	 * @param value The internal value
	 * @param def The default type value
	 * @return The long type value or the default type value
	 */
	public long getLongTypeValueForInternalValue(String value, long def) {
		long r = def;
		Object o = getTypeValueForInternalValue(value);
		if (o!=null && o instanceof Long) {
			r = (Long) o;
		}
		return r;
	}
	
	/**
	 * Returns the integer type value for an internal value.
	 * 
	 * @param value The internal value
	 * @param def The default type value
	 * @return The integer type value or the default type value
	 */
	public int getIntegerTypeValueForInternalValue(String value, int def) {
		int r = def;
		Object o = getTypeValueForInternalValue(value);
		if (o!=null && o instanceof Integer) {
			r = (Integer) o;
		}
		return r;
	}
	
	/**
	 * Returns a specific entity object.
	 * 
	 * @param language The entity language
	 * @param type The entity type
	 * @return The entity object or null
	 */
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

	/**
	 * Returns a specific entity object.
	 * 
	 * @param prefix The entity internal value prefix
	 * @return The entity object or null
	 */
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

	/**
	 * Returns the list of entities.
	 * 
	 * @return The list of entities
	 */
	public List<EntityObject> getEntities() {
		return entities;
	}

	/**
	 * Returns a list of entities excluding the complex objects.
	 *  
	 * @param languages An optional list languages to filter the list
	 * @param types An optional list types to filter the list
	 * @return The list of entities
	 */
	public List<EntityObject> getEntityObjects(List<String> languages,List<String> types) {
		List<EntityObject> r = new ArrayList<EntityObject>();
		List<EntityObject> list = getEntities(languages,types);
		for (EntityObject eo: list) {
			if (!(eo instanceof ComplexObject)) {
				r.add(eo);
			}
		}
		return r;
	}
	
	/**
	 * Returns a list of complex entities.
	 *  
	 * @param languages An optional list languages to filter the list
	 * @param types An optional list types to filter the list
	 * @return The list of entities
	 */
	public List<EntityObject> getComplexObjects(List<String> languages,List<String> types) {
		List<EntityObject> r = new ArrayList<EntityObject>();
		List<EntityObject> list = getEntities(languages,types);
		for (EntityObject eo: list) {
			if (eo instanceof ComplexObject) {
				r.add(eo);
			}
		}
		return r;
	}
	
	/**
	 * Returns a list of entities.
	 *  
	 * @param languages An optional list languages to filter the list
	 * @param types An optional list types to filter the list
	 * @return The list of entities
	 */
	public List<EntityObject> getEntities(List<String> languages,List<String> types) {
		List<EntityObject> r = new ArrayList<EntityObject>();
		for (EntityObject eo: entities) {
			if ((languages==null || languages.size()==0 || languages.contains(eo.getLanguage())) &&
				(types==null || types.size()==0 || types.contains(eo.getType()))
				) {
				r.add(eo);
			}
		}
		return r;
	}
	
	/**
	 * Initializes the entities.
	 */
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

	/**
	 * Adds all default entities to the list of entities.
	 * This method is called by the constructor.
	 */
	protected void addDefaultEntities() {
		// Complex entities
		entities.add(new EnglishName());
		entities.add(new DutchName());
		// Regular entities
		entities.add(new EnglishMathematic());
		entities.add(new DutchMathematic());
		entities.add(new EnglishConfirmation());
		entities.add(new DutchConfirmation());
		entities.add(new EnglishProfanity());
		entities.add(new DutchProfanity());
		entities.add(new EnglishCurrency());
		entities.add(new DutchCurrency());
		entities.add(new EnglishLanguage());
		entities.add(new DutchLanguage());
		entities.add(new EnglishCountry());
		entities.add(new DutchCountry());
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
		entities.add(new UniversalCurrency());
		entities.add(new UniversalTime());
		entities.add(new UniversalMathematic());
		entities.add(new UniversalNumeric());
		entities.add(new UniversalAlphabetic());
	}
	
	private ZStringBuilder getInternalValuesForExternalValue(String str, String raw, int numSymbols,List<String> languages,List<String> types) {
		ZStringBuilder r = new ZStringBuilder();
		List<EntityObject> list = getEntityObjects(languages,types);
		for (EntityObject eo: list) {
			if (eo.getMaximumSymbols()>=numSymbols) {
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
		return r;
	}

	private ZStringSymbolParser translateToInternalValuesComplex(ZStringSymbolParser entityValueSequence,List<String> languages,List<String> types) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		List<EntityObject> list = getComplexObjects(languages,types);
		for (EntityObject eo: list) {
			ComplexObject co = (ComplexObject) eo;
			ZStringSymbolParser t = co.translateToInternalValues(entityValueSequence);
			if (t.length()>0) {
				r = t;
				entityValueSequence = t;
			}
		}
		return r;
	}
}
