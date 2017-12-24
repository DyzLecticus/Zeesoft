package nl.zeesoft.zspr;

import java.util.ArrayList;
import java.util.List;

/**
 * Customizable global language configuration.
 */
public class Language {
	public static final String		ENG			= "ENG";
	public static final String		NLD			= "NLD";
	
	private static List<Language>	languages	= new ArrayList<Language>();
	
	private String					code		= "";
	private String					name		= "";

	private Language(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public static Language getLanguage(String code) {
		Language r = null;
		if (languages.size()==0) {
			initializeDefaultConfiguration();
		}
		for (Language language: languages) {
			if (language.getCode().equals(code)) {
				r = language;
				break;
			}
		}
		return r;
	}
	
	public static List<Language> getLanguages() {
		if (languages.size()==0) {
			initializeDefaultConfiguration();
		}
		return new ArrayList<Language>(languages);
	}
	
	public static Language addLanguage(String code,String name) {
		Language r = getLanguage(code);
		if (r==null) {
			addNewLanguage(code,name);
		}
		return r;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	private static void initializeDefaultConfiguration() {
		addNewLanguage(ENG,"English");
		addNewLanguage(NLD,"Nederlands");
	}

	private static Language addNewLanguage(String code,String name) {
		Language r = new Language(code,name);
		languages.add(r);
		return r;
	}
}
