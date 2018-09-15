package nl.zeesoft.znlb.lang;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Languages {
	public static final String		UNI				= "UN";
	public static final String		ENG				= "EN";
	public static final String		NLD				= "NL";
	
	private static final String[]	LANGUAGES		=
		{UNI,ENG,NLD};
	private static final String[]	LANGUAGE_NAMES	=
		{"Universal","English","Dutch"};

	private List<String>			languageCodes	= new ArrayList<String>();
	private List<String>			languageNames	= new ArrayList<String>();
	
	public void initialize() {
		for (int i = 0; i < LANGUAGES.length; i++) {
			languageCodes.add(LANGUAGES[i]);
			languageNames.add(LANGUAGE_NAMES[i]);
		}
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem langsElem = new JsElem("languages",true);
		json.rootElement.children.add(langsElem);
		int i = 0;
		for (String code: languageCodes) {
			String name = languageNames.get(i);
			JsElem langElem = new JsElem();
			langsElem.children.add(langElem);
			langElem.children.add(new JsElem("code",code,true));
			langElem.children.add(new JsElem("name",name,true));
			i++;
		}
		return json;
	}
	
	public List<String> getCodes() {
		return languageCodes;
	}
	
	public List<String> getNames() {
		return languageNames;
	}
	
	public String getNameForCode(String code) {
		String r = "";
		int i = languageCodes.indexOf(code);
		if (i>=0) {
			r = languageNames.get(i);
		}
		return r;
	}
	
	public String getCodeForName(String name) {
		String r = "";
		int i = languageNames.indexOf(name);
		if (i>=0) {
			r = languageCodes.get(i);
		}
		return r;
	}
}
