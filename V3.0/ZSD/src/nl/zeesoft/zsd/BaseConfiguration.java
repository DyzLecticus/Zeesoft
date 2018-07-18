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
		json.rootElement.children.add(new JsElem("baseDir",baseDir,true));
		json.rootElement.children.add(new JsElem("extendDir",extendDir,true));
		json.rootElement.children.add(new JsElem("overrideDir",overrideDir,true));
		json.rootElement.children.add(new JsElem("maxMsInterpretPerSymbol","" + maxMsInterpretPerSymbol,true));
		json.rootElement.children.add(new JsElem("maxMsInterpretPerSequence","" + maxMsInterpretPerSequence,true));
		json.rootElement.children.add(new JsElem("maxMsDialogPerSequence","" + maxMsDialogPerSequence,true));
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
		name = json.rootElement.getChildValueByName("name").toString();
		primaryLanguage = json.rootElement.getChildValueByName("primaryLanguage").toString();
		baseDir = json.rootElement.getChildValueByName("baseDir").toString();
		extendDir = json.rootElement.getChildValueByName("extendDir").toString();
		overrideDir = json.rootElement.getChildValueByName("overrideDir").toString();
		maxMsInterpretPerSymbol = Long.parseLong(json.rootElement.getChildValueByName("maxMsInterpretPerSymbol").toString());
		maxMsInterpretPerSequence = Long.parseLong(json.rootElement.getChildValueByName("maxMsInterpretPerSequence").toString());
		maxMsDialogPerSequence = Long.parseLong(json.rootElement.getChildValueByName("maxMsDialogPerSequence").toString());
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
	 * Returns the base directory for data files.
	 * 
	 * @return The base directory
	 */
	public String getBaseDir() {
		return baseDir;
	}

	/**
	 * Sets the base directory for data files.
	 * 
	 * @param baseDir The base directory
	 */
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	/**
	 * Returns the extension directory for data files.
	 * 
	 * @return The extension directory
	 */
	public String getExtendDir() {
		return extendDir;
	}

	/**
	 * Sets the extension directory for data files.
	 * 
	 * @param extendDir The extension directory
	 */
	public void setExtendDir(String extendDir) {
		this.extendDir = extendDir;
	}

	/**
	 * Returns the override directory for data files.
	 * 
	 * @return The override directory
	 */
	public String getOverrideDir() {
		return overrideDir;
	}

	/**
	 * Sets the override directory for data files.
	 * 
	 * @param overrideDir The override directory
	 */
	public void setOverrideDir(String overrideDir) {
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
