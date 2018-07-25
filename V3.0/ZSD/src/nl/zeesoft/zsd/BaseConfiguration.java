package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.dialogs.Generic;
import nl.zeesoft.zsd.dialog.dialogs.Room;

/**
 * A BaseConfiguration instance is used to initialize sequence interpreter and dialog handler configurations.
 */
public class BaseConfiguration {
	public static final String					LANG_UNI					= "UNI";
	public static final String					LANG_ENG					= "ENG";
	public static final String					LANG_NLD					= "NLD";

	// Entities
	public static final String					TYPE_ALPHABETIC				= "ABC";
	public static final String					TYPE_NUMERIC				= "NUM";
	public static final String					TYPE_MATHEMATIC				= "MTH";
	public static final String					TYPE_TIME					= "TIM";
	public static final String					TYPE_ORDER					= "ORD";
	public static final String					TYPE_ORDER2					= "OR2";
	public static final String					TYPE_MONTH					= "MNT";
	public static final String					TYPE_DURATION				= "DUR";
	public static final String					TYPE_DATE					= "DAT";
	public static final String					TYPE_PREPOSITION			= "PRE";
	public static final String					TYPE_COUNTRY				= "CNT";
	public static final String					TYPE_PROFANITY				= "PRF";
	public static final String					TYPE_CONFIRMATION			= "CNF";

	// Complex entities
	public static final String					TYPE_NAME					= "NAM";

	private String								name						= "Dyz Lecticus";
	private String								primaryLanguage				= BaseConfiguration.LANG_ENG;
	private List<String>						supportedLanguages			= new ArrayList<String>();
	private SortedMap<String,String>			supportedAlphabets			= new TreeMap<String,String>();
	private SortedMap<String,List<String>>		supportedMasterContexts		= new TreeMap<String,List<String>>();

	private boolean								debug						= false;
	
	private String								dataDir						= "";
	private String								baseDir						= "base/";
	private String								extendDir					= "extend/";
	private String								overrideDir					= "override/";

	private boolean								generateReadFormat			= true;
	
	private long								maxMsInterpretPerSymbol		= 100;
	private long								maxMsInterpretPerSequence	= 2000;
	private long								maxMsDialogPerSequence		= 1000;

	public BaseConfiguration() {
		initialize();
	}

	public BaseConfiguration(String name, String primaryLanguage) {
		this.name = name;
		this.primaryLanguage = primaryLanguage;
		initialize();
	}

	/**
	 * Converts the configuration to JSON.
	 * 
	 * @return The JSON
	 */
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("primaryLanguage",primaryLanguage,true));
		json.rootElement.children.add(new JsElem("debug","" + debug));
		json.rootElement.children.add(new JsElem("dataDir",dataDir,true));
		json.rootElement.children.add(new JsElem("baseDir",baseDir,true));
		json.rootElement.children.add(new JsElem("extendDir",extendDir,true));
		json.rootElement.children.add(new JsElem("overrideDir",overrideDir,true));
		json.rootElement.children.add(new JsElem("generateReadFormat","" + generateReadFormat));
		json.rootElement.children.add(new JsElem("maxMsInterpretPerSymbol","" + maxMsInterpretPerSymbol));
		json.rootElement.children.add(new JsElem("maxMsInterpretPerSequence","" + maxMsInterpretPerSequence));
		json.rootElement.children.add(new JsElem("maxMsDialogPerSequence","" + maxMsDialogPerSequence));
		JsElem langsElem = new JsElem("supportedLanguages",true);
		json.rootElement.children.add(langsElem);
		for (String language: supportedLanguages) {
			JsElem langElem = new JsElem();
			langsElem.children.add(langElem);
			langElem.children.add(new JsElem("code",language,true));
			langElem.children.add(new JsElem("alphabet",supportedAlphabets.get(language),true));
			List<String> smcs = supportedMasterContexts.get(language);
			if (smcs!=null) {
				JsElem smcsElem = new JsElem("supportedMasterContexts",true);
				langElem.children.add(smcsElem);
				for (String mc: smcs) {
					smcsElem.children.add(new JsElem(null,mc,true));
				}
			}
		}
		return json;
	}

	/**
	 * Initializes the configuration using the supplied JSON.
	 * 
	 * @param json The JSON
	 */
	public void fromJson(JsFile json) {
		supportedLanguages.clear();
		supportedAlphabets.clear();
		supportedMasterContexts.clear();
		name = json.rootElement.getChildString("name",name);
		primaryLanguage = json.rootElement.getChildString("primaryLanguage",primaryLanguage);
		debug = json.rootElement.getChildBoolean("debug",debug);
		dataDir = json.rootElement.getChildString("dataDir",dataDir);
		baseDir = json.rootElement.getChildString("baseDir",baseDir);
		extendDir = json.rootElement.getChildString("extendDir",extendDir);
		overrideDir = json.rootElement.getChildString("overrideDir",overrideDir);
		generateReadFormat = json.rootElement.getChildBoolean("generateReadFormat",generateReadFormat);
		maxMsInterpretPerSymbol = json.rootElement.getChildLong("maxMsInterpretPerSymbol",maxMsInterpretPerSymbol);
		maxMsInterpretPerSequence = json.rootElement.getChildLong("maxMsInterpretPerSequence",maxMsInterpretPerSequence);
		maxMsDialogPerSequence = json.rootElement.getChildLong("maxMsDialogPerSequence",maxMsDialogPerSequence);
		JsElem langsElem = json.rootElement.getChildByName("supportedLanguages");
		if (langsElem!=null) {
			for (JsElem langElem: langsElem.children) {
				String language = langElem.getChildValueByName("code").toString();
				supportedLanguages.add(language);
				supportedAlphabets.put(language,langElem.getChildValueByName("alphabet").toString());
				JsElem mcsElem = langElem.getChildByName("supportedMasterContexts");
				if (mcsElem!=null) {
					List<String> mcs = new ArrayList<String>();
					for (JsElem mcElem: mcsElem.children) {
						mcs.add(mcElem.value.toString());
					}
					supportedMasterContexts.put(language,mcs);
				}
			}
		}
	}

	public String getFullBaseDir() {
		return dataDir + baseDir;
	}

	public String getFullOverrideDir() {
		return dataDir + overrideDir;
	}

	public String getFullExtendDir() {
		return dataDir + extendDir;
	}
	
	/**
	 * Returns the name of the agent.
	 * 
	 * @return The name of the agent
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the agent.
	 * 
	 * @param name The name of the agent
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the primary language of the agent.
	 * 
	 * @return The primary language
	 */
	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	/**
	 * Sets the primary language of the agent.
	 * 
	 * @param primaryLanguage The primary language
	 */
	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	/**
	 * Returns the list of languages the agent supports
	 * 
	 * @return The list of languages
	 */
	public List<String> getSupportedLanguages() {
		return supportedLanguages;
	}

	/**
	 * Returns the supported alphabets per language
	 * 
	 * @return The supported alphabets
	 */
	public SortedMap<String, String> getSupportedAlphabets() {
		return supportedAlphabets;
	}

	/**
	 * Returns the supported master contexts per language
	 * 
	 * @return The supported master contexts
	 */
	public SortedMap<String,List<String>> getSupportedMasterContexts() {
		return supportedMasterContexts;
	}

	/**
	 * Indicates the application is in debug mode.
	 * 
	 * @return True if the application is in debug mode
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Sets the application debug mode.
	 * 
	 * @param debug Indicates the application is in debug mode
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Returns the directory for data files.
	 * 
	 * @return The data directory
	 */
	public String getDataDir() {
		return dataDir;
	}

	/**
	 * Sets the directory for data files.
	 * 
	 * @param dataDir The data directory
	 */
	public void setDataDir(String dataDir) {
		if (!dataDir.endsWith("/") && !dataDir.endsWith("\\")) {
			dataDir += "/";
		}
		this.dataDir = dataDir;
	}

	/**
	 * Sets the base directory for data files.
	 * 
	 * @param baseDir The base directory
	 */
	public void setBaseDir(String baseDir) {
		if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
			baseDir += "/";
		}
		this.baseDir = baseDir;
	}

	/**
	 * Sets the extension directory for data files.
	 * 
	 * @param extendDir The extension directory
	 */
	public void setExtendDir(String extendDir) {
		if (!extendDir.endsWith("/") && !extendDir.endsWith("\\")) {
			extendDir += "/";
		}
		this.extendDir = extendDir;
	}

	/**
	 * Sets the override directory for data files.
	 * 
	 * @param overrideDir The override directory
	 */
	public void setOverrideDir(String overrideDir) {
		if (!overrideDir.endsWith("/") && !overrideDir.endsWith("\\")) {
			overrideDir += "/";
		}
		this.overrideDir = overrideDir;
	}

	/**
	 * Indicates data files are written in a readable format.
	 * 
	 * @return True if data files are written in a readable format
	 */
	public boolean isGenerateReadFormat() {
		return generateReadFormat;
	}

	/**
	 * Indicates data files will be written in a readable format.
	 * 
	 * @param generateReadFormat If true, data files will be written in a readable format
	 */
	public void setGenerateReadFormat(boolean generateReadFormat) {
		this.generateReadFormat = generateReadFormat;
	}

	/**
	 * Returns the maximum number of milliseconds interpretation should take per symbol.
	 * This is used as an indication and not strictly enforced.
	 * 
	 * @return The maximum number of milliseconds
	 */
	public long getMaxMsInterpretPerSymbol() {
		return maxMsInterpretPerSymbol;
	}

	/**
	 * Sets the maximum number of milliseconds interpretation should take per symbol.
	 * This is used as an indication and not strictly enforced.
	 * 
	 * @param maxMsPerSymbol The maximum number of milliseconds
	 */
	public void setMaxMsInterpretPerSymbol(long maxMsPerSymbol) {
		this.maxMsInterpretPerSymbol = maxMsPerSymbol;
	}

	/**
	 * Returns the maximum number of milliseconds interpretation should take per sequence.
	 * This is used as an indication and not strictly enforced.
	 * 
	 * @return The maximum number of milliseconds
	 */
	public long getMaxMsInterpretPerSequence() {
		return maxMsInterpretPerSequence;
	}

	/**
	 * Sets the maximum number of milliseconds interpretation should take per sequence.
	 * This is used as an indication and not strictly enforced.
	 * 
	 * @param maxMsPerSequence The maximum number of milliseconds
	 */
	public void setMaxMsInterpretPerSequence(long maxMsPerSequence) {
		this.maxMsInterpretPerSequence = maxMsPerSequence;
	}

	/**
	 * Returns the maximum number of milliseconds dialog handling can take after interpretation.
	 * This is used as an indication and not strictly enforced.
	 * 
	 * @return The maximum number of milliseconds
	 */
	public long getMaxMsDialogPerSequence() {
		return maxMsDialogPerSequence;
	}

	/**
	 * Sets the maximum number of milliseconds dialog handling can take after interpretation.
	 * This is used as an indication and not strictly enforced.
	 * 
	 * @param maxMsDialogPerSequence The maximum number of milliseconds
	 */
	public void setMaxMsDialogPerSequence(long maxMsDialogPerSequence) {
		this.maxMsDialogPerSequence = maxMsDialogPerSequence;
	}

	protected void initialize() {
		initializeSupportedLanguages();
		for (String language: supportedLanguages) {
			initializeSupportedAlphabetsForLanguage(language);
			initializeSupportedMasterContextsForLanguage(language);
		}
	}

	protected void initializeSupportedLanguages() {
		supportedLanguages.add(LANG_ENG);
		supportedLanguages.add(LANG_NLD);
	}

	protected void initializeSupportedAlphabetsForLanguage(String language) {
		if (language.equals(LANG_NLD)) {
			supportedAlphabets.put(language,"ΰαηθικλοσφϊό" + SymbolCorrector.ALPHABET);
		} else {
			supportedAlphabets.put(language,SymbolCorrector.ALPHABET);
		}
	}

	protected void initializeSupportedMasterContextsForLanguage(String language) {
		List<String> mcs = new ArrayList<String>();
		mcs.add(Generic.MASTER_CONTEXT_GENERIC);
		mcs.add(Room.MASTER_CONTEXT_ROOM);
		supportedMasterContexts.put(language,mcs);
	}
}
