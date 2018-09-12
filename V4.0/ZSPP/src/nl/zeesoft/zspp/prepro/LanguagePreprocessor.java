package nl.zeesoft.zspp.prepro;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class LanguagePreprocessor {
	private String						language		= "";
	private List<PreprocessReplacement>	replacements	= new ArrayList<PreprocessReplacement>();

	// Override to implement
	protected void initializeReplacements() {
		
	}
	
	protected ZStringSymbolParser process(ZStringSymbolParser sequence) {
		ZStringSymbolParser r = new ZStringSymbolParser(sequence);
		for (PreprocessReplacement pr: replacements) {
			r.replace(pr.key,pr.value);
		}
		return r;
	}
	
	protected void addReplacement(String key, String value) {
		replacements.add(new PreprocessReplacement(key,value));
	}

	protected PreprocessReplacement getReplacement(String key) {
		PreprocessReplacement r = null;
		for (PreprocessReplacement pr: replacements) {
			if (pr.key.equals(key)) {
				r = pr;
				break;
			}
		}
		return r;
	}

	protected String getLanguage() {
		return language;
	}

	protected void setLanguage(String language) {
		this.language = language;
	}

	protected List<PreprocessReplacement> getReplacements() {
		return replacements;
	}
	
	protected JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("language",language,true));
		JsElem repsElem = new JsElem("replacements",true);
		json.rootElement.children.add(repsElem);
		for (PreprocessReplacement rep: replacements) {
			JsElem repElem = new JsElem();
			repsElem.children.add(repElem);
			repElem.children.add(new JsElem("key",rep.key,true));
			repElem.children.add(new JsElem("val",rep.value,true));
		}
		return json;
	}
	
	protected void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			replacements.clear();
			language = json.rootElement.getChildString("language",language);
			JsElem repsElem = json.rootElement.getChildByName("replacements");
			if (repsElem!=null) {
				for (JsElem repElem: repsElem.children) {
					PreprocessReplacement rep = new PreprocessReplacement();
					rep.key = repElem.getChildString("key");
					rep.value = repElem.getChildString("val");
					if (rep.key.length()>0 && rep.value.length()>0) {
						replacements.add(rep);
					}
				}
			}
		}
	}
}
