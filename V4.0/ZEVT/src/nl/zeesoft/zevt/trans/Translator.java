package nl.zeesoft.zevt.trans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zevt.trans.entities.dutch.DutchConfirmation;
import nl.zeesoft.zevt.trans.entities.dutch.DutchCountry;
import nl.zeesoft.zevt.trans.entities.dutch.DutchCurrency;
import nl.zeesoft.zevt.trans.entities.dutch.DutchDate;
import nl.zeesoft.zevt.trans.entities.dutch.DutchDuration;
import nl.zeesoft.zevt.trans.entities.dutch.DutchLanguage;
import nl.zeesoft.zevt.trans.entities.dutch.DutchMathematic;
import nl.zeesoft.zevt.trans.entities.dutch.DutchMonth;
import nl.zeesoft.zevt.trans.entities.dutch.DutchNumeric;
import nl.zeesoft.zevt.trans.entities.dutch.DutchOrder;
import nl.zeesoft.zevt.trans.entities.dutch.DutchPreposition;
import nl.zeesoft.zevt.trans.entities.dutch.DutchProfanity;
import nl.zeesoft.zevt.trans.entities.dutch.DutchTime;
import nl.zeesoft.zevt.trans.entities.english.EnglishConfirmation;
import nl.zeesoft.zevt.trans.entities.english.EnglishCountry;
import nl.zeesoft.zevt.trans.entities.english.EnglishCurrency;
import nl.zeesoft.zevt.trans.entities.english.EnglishDate;
import nl.zeesoft.zevt.trans.entities.english.EnglishDuration;
import nl.zeesoft.zevt.trans.entities.english.EnglishLanguage;
import nl.zeesoft.zevt.trans.entities.english.EnglishMathematic;
import nl.zeesoft.zevt.trans.entities.english.EnglishMonth;
import nl.zeesoft.zevt.trans.entities.english.EnglishNumeric;
import nl.zeesoft.zevt.trans.entities.english.EnglishOrder;
import nl.zeesoft.zevt.trans.entities.english.EnglishOrder2;
import nl.zeesoft.zevt.trans.entities.english.EnglishPreposition;
import nl.zeesoft.zevt.trans.entities.english.EnglishProfanity;
import nl.zeesoft.zevt.trans.entities.english.EnglishTime;
import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.StateListener;

public class Translator extends Locker {
	public static final String					VALUE_CONCATENATOR			= ":";
	public static final String					OR_CONCATENATOR				= "|";
	public static final String					OR_CONCATENATOR_SPLITTER	= "\\|";
	
	private Config								configuration				= null;
	private List<EntityObject>					entities					= new ArrayList<EntityObject>();
	
	private int									maximumSymbols				= 1;
	
	private boolean								initializing				= false;
	private boolean								initialized					= false;
	private int									todo						= 0;
	private ZStringBuilder						entitiesJson				= null;
	
	private TranslatorRefreshWorker				refreshWorker				= null;

	public List<StateListener>					listeners					= new ArrayList<StateListener>();
	
	public Translator(Config config) {
		super(config.getMessenger());
		configuration = config;
		addDefaultEntities();
		refreshWorker = new TranslatorRefreshWorker(config,this);
	}

	public void addListener(StateListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}
	
	public void install() {
		lockMe(this);
		for (EntityObject eo: entities) {
			if (eo instanceof DatabaseEntityObject) {
				((DatabaseEntityObject) eo).install();
			}
		}
		unlockMe(this);
	}
	
	public boolean initialize() {
		boolean r = false;
		lockMe(this);
		if (!initialized && !initializing) {
			configuration.debug(this,"Initializing entity value translator ...");
			initializing = true;
			todo = 0;
			for (EntityObject eo: entities) {
				if (!eo.isInitialized()) {
					todo++;
				}
			}
			TranslatorInitWorker worker = new TranslatorInitWorker(configuration,this);
			worker.start();
			refreshWorker.start();
			r = true;
		}
		unlockMe(this);
		return r;
	}
	
	public void destroy() {
		lockMe(this);
		refreshWorker.stop();
		if (initialized) {
			for (EntityObject eo: entities) {
				eo.clear();
			}
			entities.clear();
			addDefaultEntities();
			initialized = false;
		}
		List<StateListener> lst = new ArrayList<StateListener>(listeners);
		unlockMe(this);
		for (StateListener listener: lst) {
			listener.stateChanged(this,false);
		}
	}
	
	public boolean isInitialized() {
		boolean r = false;
		lockMe(this);
		r = initialized;
		unlockMe(this);
		return r;
	}

	public ZStringBuilder getEntitiesJson() {
		ZStringBuilder r = null;
		lockMe(this);
		r = entitiesJson;
		unlockMe(this);
		return r;
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
		return translateToInternalValues(sequence,"","");
	}

	/**
	 * Translates a sequence to internal entity values.
	 * 
	 * @param sequence The sequence
	 * @param language An optional entity language to limit entities
	 * @return The translated sequence
	 */
	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,String language) {
		return translateToInternalValues(sequence,language,"");
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
		return translateToInternalValues(sequence,languages,types);
	}
	
	/**
	 * Translates a sequence to internal entity values.
	 * 
	 * @param sequence The sequence
	 * @param types An optional list of entity types to limit entities
	 * @return The translated sequence
	 */
	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,List<String> types) {
		return translateToInternalValues(sequence,null,types);
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
	public ZStringSymbolParser translateToInternalValues(ZStringSymbolParser sequence,List<String> languages,List<String> types) {
		ZStringSymbolParser r = new ZStringSymbolParser();
		
		if (isInitialized()) {
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
		
		if (isInitialized()) {
			List<String> symbols = sequence.toSymbolsPunctuated();
			List<String> newSymbols = new ArrayList<String>();
			for (int i = 0; i<symbols.size(); i++) {
				String sym = symbols.get(i);
				if (!singleOnly || !sym.contains(OR_CONCATENATOR)) {
					String ext = getExternalValueForInternalValues(sym,type);
					if (ext.length()>0) {
						sym = ext;
					}
				}
				newSymbols.add(sym);
			}
			r.fromSymbols(newSymbols,false,false);
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
		String[] values = str.split(OR_CONCATENATOR_SPLITTER);
		for (int v = 0; v < values.length; v++) {
			if (values[v].contains(VALUE_CONCATENATOR)) {
				String prefix = values[v].split(VALUE_CONCATENATOR)[0] + VALUE_CONCATENATOR;
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
		String prefix = value.split(VALUE_CONCATENATOR)[0] + VALUE_CONCATENATOR;
		EntityObject eo = getEntityObject(prefix);
		if (eo!=null && (type.length()==0 || eo.getType().equals(type))) {
			r = eo.getExternalValueForInternalValue(value);
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
		lockMe(this);
		for (EntityObject eo: entities) {
			if (eo.getLanguage().equals(language) && eo.getType().equals(type)) {
				r = eo;
				break;
			}
		}
		unlockMe(this);
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
		lockMe(this);
		for (EntityObject eo: entities) {
			if (eo.getInternalValuePrefix().equals(prefix)) {
				r = eo;
				break;
			}
		}
		unlockMe(this);
		return r;
	}

	/**
	 * Adds an entity to the list of entities
	 * 
	 * @param eo The entity
	 */
	public void addEntity(EntityObject eo) {
		lockMe(this);
		entities.add(eo);
		unlockMe(this);
	}

	/**
	 * Replaces an entity to the list of entities
	 * 
	 * @param eo The entity
	 */
	public void replaceEntity(EntityObject eo) {
		EntityObject curr = getEntityObject(eo.getInternalValuePrefix());
		if (curr!=null) {
			lockMe(this);
			int index = entities.indexOf(curr);
			if (index>=0) {
				entities.add(entities.indexOf(curr),eo);
				entities.remove(curr);
			}
			unlockMe(this);
		}
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
		lockMe(this);
		for (EntityObject eo: entities) {
			if ((languages==null || languages.size()==0 || languages.contains(eo.getLanguage())) &&
				(types==null || types.size()==0 || types.contains(eo.getType()))
				) {
				r.add(eo);
			}
		}
		unlockMe(this);
		return r;
	}

	protected boolean logDatabaseRequestFailures() {
		return true;
	}
	
	protected Config getConfiguration() {
		return configuration;
	}
	
	protected void initializeEntities() {
		for (EntityObject eo: entities) {
			if (!eo.isInitialized()) {
				eo.initialize();
				if (eo.getMaximumSymbols()>maximumSymbols) {
					maximumSymbols = eo.getMaximumSymbols();
				}
			}
		}
	}
	
	protected void initializedEntity(EntityObject eo) {
		boolean done = false;
		lockMe(this);
		todo--;
		if (todo==0) {
			done = true;
		}
		unlockMe(this);
		if (isInitialized()) {
			replaceEntity(eo);
			configuration.debug(this,"Replaced entity: " + eo.getName());
		} else {
			if (done) {
				lockMe(this);
				initialized = true;
				initializing = false;
				List<StateListener> lst = new ArrayList<StateListener>(listeners);
				unlockMe(this);
				configuration.debug(this,"Initialized entity value translator");
				for (StateListener listener: lst) {
					listener.stateChanged(this,true);
				}
				createEntitiesJson();
			}
		}
	}

	protected void createEntitiesJson() {
		configuration.debug(this,"Creating entities JSON ...");
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem eosElem = new JsElem("entities",true);
		json.rootElement.children.add(eosElem);
		lockMe(this);
		List<EntityObject> ents = new ArrayList<EntityObject>(entities);
		unlockMe(this);
		for (EntityObject eo: ents) {
			if (!eo.getLanguage().equals(Languages.UNI)) {
				JsElem eoElem = new JsElem();
				eosElem.children.add(eoElem);
				JsFile eoJs = eo.toJson();
				eoJs.rootElement.children.add(0,new JsElem("name",eo.getName(),true));
				eoElem.children = eoJs.rootElement.children;
			}
		}
		ZStringBuilder str = null;
		if (configuration.isDebug()) {
			str = json.toStringBuilderReadFormat();
		} else {
			str = json.toStringBuilderReadFormat();
		}
		lockMe(this);
		entitiesJson = str;
		unlockMe(this);
		configuration.debug(this,"Created entities JSON");
	}
	
	protected void refreshEntities() {
		if (isInitialized()) {
			List<EntityObject> refresh = getRefreshEntities();
			lockMe(this);
			todo = refresh.size();
			unlockMe(this);
			for (EntityObject eo: refresh) {
				eo.initialize();
			}
		}
	}
	
	protected List<EntityObject> getRefreshEntities() {
		List<EntityObject> r = new ArrayList<EntityObject>();
		r.add(new EnglishDate(this));
		r.add(new DutchDate(this));
		return r;
	}
	
	protected void addDefaultEntities() {
		entities.add(new EnglishMathematic(this));
		entities.add(new DutchMathematic(this));
		entities.add(new EnglishConfirmation(this));
		entities.add(new DutchConfirmation(this));
		entities.add(new EnglishProfanity(this));
		entities.add(new DutchProfanity(this));
		entities.add(new EnglishCurrency(this));
		entities.add(new DutchCurrency(this));
		entities.add(new EnglishLanguage(this));
		entities.add(new DutchLanguage(this));
		entities.add(new EnglishCountry(this));
		entities.add(new DutchCountry(this));
		entities.add(new EnglishDate(this));
		entities.add(new DutchDate(this));
		entities.add(new EnglishTime(this));
		entities.add(new DutchTime(this));
		entities.add(new EnglishDuration(this));
		entities.add(new DutchDuration(this));
		entities.add(new EnglishMonth(this));
		entities.add(new DutchMonth(this));
		entities.add(new EnglishOrder(this));
		entities.add(new EnglishOrder2(this));
		entities.add(new DutchOrder(this));
		entities.add(new EnglishNumeric(this));
		entities.add(new DutchNumeric(this));
		entities.add(new EnglishPreposition(this));
		entities.add(new DutchPreposition(this));
		entities.add(new UniversalMathematic(this));
		entities.add(new UniversalCurrency(this));
		entities.add(new UniversalSmiley(this));
		entities.add(new UniversalFrowny(this));
		entities.add(new UniversalTime(this));
		entities.add(new UniversalNumeric(this));
		entities.add(new UniversalAlphabetic(this));
	}
	
	private ZStringBuilder getInternalValuesForExternalValue(String str, String raw, int numSymbols,List<String> languages,List<String> types) {
		ZStringBuilder r = new ZStringBuilder();
		List<EntityObject> list = getEntities(languages,types);
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
						r.append(OR_CONCATENATOR);
					}
					r.append(iv);
				}
			}
		}
		return r;
	}
}
