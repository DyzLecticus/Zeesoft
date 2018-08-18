package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.dialogs.ForeignTransfer;
import nl.zeesoft.zsd.dialog.dialogs.Generic;
import nl.zeesoft.zsd.dialog.dialogs.Room;
import nl.zeesoft.zsd.dialog.dialogs.Support;

/**
 * A BaseConfiguration instance is used to initialize sequence interpreter and dialog handler configurations.
 */
public class BaseConfiguration {
	public static final String					LANG_UNI					= "UN";
	public static final String					LANG_ENG					= "EN";
	public static final String					LANG_NLD					= "NL";

	// Next dialog indicator
	public static final String					TYPE_NEXT_DIALOG			= "NXD";

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
	public static final String					TYPE_LANGUAGE				= "LNG";
	public static final String					TYPE_CURRENCY				= "CUR";
	public static final String					TYPE_PROFANITY				= "PRF";
	public static final String					TYPE_CONFIRMATION			= "CNF";
	public static final String					TYPE_SMILEY					= "SML";
	public static final String					TYPE_FROWNY					= "FRN";

	// Complex entities
	public static final String					TYPE_NAME					= "NAM";

	private String								name						= "Dyz Lecticus";
	private String								email						= "dyz.lecticus@zeesoft.nl";
	private String								smiley						= ":-)";
	private String								frowny						= ":-(";
	private String								primaryLanguage				= BaseConfiguration.LANG_ENG;
	private List<String>						supportedLanguages			= new ArrayList<String>();
	private SortedMap<String,String>			supportedAlphabets			= new TreeMap<String,String>();
	private SortedMap<String,List<String>>		supportedMasterContexts		= new TreeMap<String,List<String>>();

	private String								dataDir						= "";
	private String								baseDir						= "base/";
	private String								extendDir					= "extend/";
	private String								overrideDir					= "override/";

	private boolean								generateReadFormat			= true;
	
	private long								maxMsInterpretPerSymbol		= 100;
	private long								maxMsInterpretPerSequence	= 2000;
	private long								maxMsDialogPerSequence		= 1000;

	private String								selfTestBaseLineFileName	= "selfTestBaseLineSummary.json";
	
	private SortedMap<String,String>			parameters					= new TreeMap<String,String>();
	
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
		json.rootElement.children.add(new JsElem("email",email,true));
		json.rootElement.children.add(new JsElem("smiley",smiley,true));
		json.rootElement.children.add(new JsElem("frowny",frowny,true));
		json.rootElement.children.add(new JsElem("primaryLanguage",primaryLanguage,true));
		json.rootElement.children.add(new JsElem("dataDir",dataDir,true));
		json.rootElement.children.add(new JsElem("baseDir",baseDir,true));
		json.rootElement.children.add(new JsElem("extendDir",extendDir,true));
		json.rootElement.children.add(new JsElem("overrideDir",overrideDir,true));
		json.rootElement.children.add(new JsElem("generateReadFormat","" + generateReadFormat));
		json.rootElement.children.add(new JsElem("maxMsInterpretPerSymbol","" + maxMsInterpretPerSymbol));
		json.rootElement.children.add(new JsElem("maxMsInterpretPerSequence","" + maxMsInterpretPerSequence));
		json.rootElement.children.add(new JsElem("maxMsDialogPerSequence","" + maxMsDialogPerSequence));
		json.rootElement.children.add(new JsElem("selfTestBaseLineFileName",selfTestBaseLineFileName,true));
		JsElem paramsElem = new JsElem("parameters",true);
		json.rootElement.children.add(paramsElem);
		for (Entry<String,String> entry: parameters.entrySet()) {
			JsElem pElem = new JsElem();
			paramsElem.children.add(pElem);
			pElem.children.add(new JsElem("key",entry.getKey(),true));
			pElem.children.add(new JsElem("value",entry.getValue(),true));
		}
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
		email = json.rootElement.getChildString("email",email);
		smiley = json.rootElement.getChildString("smiley",smiley);
		frowny = json.rootElement.getChildString("frowny",frowny);
		primaryLanguage = json.rootElement.getChildString("primaryLanguage",primaryLanguage);
		dataDir = json.rootElement.getChildString("dataDir",dataDir);
		baseDir = json.rootElement.getChildString("baseDir",baseDir);
		extendDir = json.rootElement.getChildString("extendDir",extendDir);
		overrideDir = json.rootElement.getChildString("overrideDir",overrideDir);
		generateReadFormat = json.rootElement.getChildBoolean("generateReadFormat",generateReadFormat);
		maxMsInterpretPerSymbol = json.rootElement.getChildLong("maxMsInterpretPerSymbol",maxMsInterpretPerSymbol);
		maxMsInterpretPerSequence = json.rootElement.getChildLong("maxMsInterpretPerSequence",maxMsInterpretPerSequence);
		maxMsDialogPerSequence = json.rootElement.getChildLong("maxMsDialogPerSequence",maxMsDialogPerSequence);
		selfTestBaseLineFileName = json.rootElement.getChildString("selfTestBaseLineFileName",selfTestBaseLineFileName);
		JsElem paramsElem = json.rootElement.getChildByName("parameters");
		if (paramsElem!=null) {
			for (JsElem pElem: paramsElem.children) {
				String key = pElem.getChildString("key");
				String value = pElem.getChildString("value");
				parameters.put(key,value);
			}
		}
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

	public String getFullSelfTestBaseLineFileName() {
		return dataDir + selfTestBaseLineFileName;
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
	 * Returns the e-mail address of the agent.
	 * 
	 * @return The e-mail address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the e-mail address of the agent.
	 * 
	 * @param email The e-mail address
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public String getSmiley() {
		return smiley;
	}

	public void setSmiley(String smiley) {
		this.smiley = smiley;
	}

	public String getFrowny() {
		return frowny;
	}

	public void setFrowny(String frowny) {
		this.frowny = frowny;
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

	public String getSelfTestBaseLineFileName() {
		return selfTestBaseLineFileName;
	}

	public void setSelfTestBaseLineFileName(String selfTestBaseLineFileName) {
		this.selfTestBaseLineFileName = selfTestBaseLineFileName;
	}
	

	public SortedMap<String,String> getParameters() {
		return parameters;
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
		mcs.add(Support.MASTER_CONTEXT_SUPPORT);
		mcs.add(Room.MASTER_CONTEXT_ROOM);
		mcs.add(ForeignTransfer.MASTER_CONTEXT_FOREIGN_TRANSFER);
		supportedMasterContexts.put(language,mcs);
	}
}
