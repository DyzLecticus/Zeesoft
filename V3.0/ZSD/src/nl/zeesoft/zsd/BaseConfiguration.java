package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.dialogs.Generic;

public class BaseConfiguration {
	public static final String					LANG_UNI				= "UNI";
	public static final String					LANG_ENG				= "ENG";
	public static final String					LANG_NLD				= "NLD";

	// Entities
	public static final String					TYPE_ALPHABETIC			= "ABC";
	public static final String					TYPE_NUMERIC			= "NUM";
	public static final String					TYPE_TIME				= "TIM";
	public static final String					TYPE_ORDER				= "ORD";
	public static final String					TYPE_ORDER2				= "OR2";
	public static final String					TYPE_MONTH				= "MNT";
	public static final String					TYPE_DURATION			= "DUR";
	public static final String					TYPE_DATE				= "DAT";
	public static final String					TYPE_PREPOSITION		= "PRE";

	// Complex entities
	public static final String					TYPE_NAME				= "NAM";

	private List<String>						languages				= new ArrayList<String>();

	private String								name					= "Dyz Lecticus";
	private String								primaryLanguage			= BaseConfiguration.LANG_ENG;
	private List<String>						supportedLanguages		= new ArrayList<String>();
	private SortedMap<String,String>			supportedAlphabets		= new TreeMap<String,String>();
	private SortedMap<String,List<String>>		supportedMasterContexts	= new TreeMap<String,List<String>>();

	private String								baseDir					= "base/";
	private String								extendDir				= "extend/";
	private String								overrideDir				= "override/";
	
	public BaseConfiguration() {
		initialize();
	}

	public BaseConfiguration(String name, String primaryLanguage) {
		this.name = name;
		this.primaryLanguage = primaryLanguage;
		initialize();
	}

	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("primaryLanguage",primaryLanguage,true));
		json.rootElement.children.add(new JsElem("baseDir",baseDir,true));
		json.rootElement.children.add(new JsElem("extendDir",extendDir,true));
		json.rootElement.children.add(new JsElem("overrideDir",overrideDir,true));
		JsElem langsElem = new JsElem("supportedLanguages",true);
		json.rootElement.children.add(langsElem);
		for (String language: supportedLanguages) {
			JsElem langElem = new JsElem();
			langsElem.children.add(langElem);
			langElem.children.add(new JsElem("code",language,true));
			List<String> smcs = supportedMasterContexts.get(language);
			JsElem smcsElem = new JsElem("supportedMasterContexts",true);
			langElem.children.add(smcsElem);
			for (String mc: smcs) {
				smcsElem.children.add(new JsElem(null,mc,true));
			}
		}
		return json;
	}

	public void fromJson(JsElem parent) {
		// TODO: implement
		supportedLanguages.clear();
		supportedMasterContexts.clear();
	}

	public List<String> getLanguages() {
		return languages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public List<String> getSupportedLanguages() {
		return supportedLanguages;
	}

	public SortedMap<String, String> getSupportedAlphabets() {
		return supportedAlphabets;
	}

	public SortedMap<String,List<String>> getSupportedMasterContexts() {
		return supportedMasterContexts;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public String getExtendDir() {
		return extendDir;
	}

	public void setExtendDir(String extendDir) {
		this.extendDir = extendDir;
	}

	public String getOverrideDir() {
		return overrideDir;
	}

	public void setOverrideDir(String overrideDir) {
		this.overrideDir = overrideDir;
	}

	protected void initialize() {
		languages.add(LANG_ENG);
		languages.add(LANG_NLD);
		languages.add(LANG_UNI);
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
		supportedAlphabets.put(language,SymbolCorrector.ALPHABET);
	}

	protected void initializeSupportedMasterContextsForLanguage(String language) {
		List<String> mcs = new ArrayList<String>();
		mcs.add(Generic.MASTER_CONTEXT_GENERIC);
		supportedMasterContexts.put(language,mcs);
	}
}
