package nl.zeesoft.zsd.preprocess;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class PreprocessorInstance {
	private String						language		= "";
	private List<PreprocessReplacement>	replacements	= new ArrayList<PreprocessReplacement>();
	
	// Override to implement
	public void initialize() {
		
	}
	
	public ZStringSymbolParser process(ZStringSymbolParser sequence) {
		ZStringSymbolParser r = new ZStringSymbolParser(sequence);
		for (PreprocessReplacement pr: replacements) {
			r.replace(pr.key,pr.value);
		}
		return r;
	}
	
	public void addReplacement(String key, String value) {
		replacements.add(new PreprocessReplacement(key,value));
	}

	public PreprocessReplacement getReplacement(String key) {
		PreprocessReplacement r = null;
		for (PreprocessReplacement pr: replacements) {
			if (pr.key.equals(key)) {
				r = pr;
				break;
			}
		}
		return r;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<PreprocessReplacement> getReplacements() {
		return replacements;
	}
}
