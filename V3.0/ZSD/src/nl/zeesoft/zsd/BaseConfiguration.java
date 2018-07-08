package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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
		supportedLanguages = getInitialSupportedLanguages();
		for (String language: supportedLanguages) {
			supportedMasterContexts.put(language,getSupportedMasterContextsForLanguage(language));
		}
	}

	protected List<String> getInitialSupportedLanguages() {
		List<String> r = new ArrayList<String>();
		r.add(LANG_ENG);
		r.add(LANG_NLD);
		return r;
	}
	
	protected List<String> getSupportedMasterContextsForLanguage(String language) {
		List<String> r = new ArrayList<String>();
		r.add(Generic.MASTER_CONTEXT_GENERIC);
		return r;
	}
}
