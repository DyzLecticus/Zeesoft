package nl.zeesoft.zsd;


import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.initialize.Initializable;
import nl.zeesoft.zsd.preprocess.DutchPreprocessor;
import nl.zeesoft.zsd.preprocess.EnglishPreprocessor;
import nl.zeesoft.zsd.preprocess.PreprocessReplacement;
import nl.zeesoft.zsd.preprocess.PreprocessorInstance;

public class SequencePreprocessor implements Initializable {
	private SortedMap<String,PreprocessorInstance> processors = new TreeMap<String,PreprocessorInstance>();
	
	public SequencePreprocessor() {
		addDefaultProcessors();
	}
	
	@Override
	public void initialize(List<ZStringBuilder> data) {
		if (data==null || data.size()==0) {
			initialize();
		} else {
			for (ZStringBuilder file: data) {
				JsFile json = new JsFile();
				json.fromStringBuilder(file);
				fromJson(json);
			}
		}
	}
	
	public ZStringSymbolParser process(ZStringSymbolParser sequence) {
		return process(sequence,null);
	}
	
	public ZStringSymbolParser process(ZStringSymbolParser sequence,String language) {
		ZStringSymbolParser r = sequence;
		r.trim();
		if (r.length()>0) {
			List<String> symbols = r.toSymbolsPunctuated();
			String end = symbols.get(symbols.size() - 1);
			if (!ZStringSymbolParser.isLineEndSymbol(end)) {
				symbols.add(".");
			}
			r.fromSymbols(symbols,true,true);
			for (Entry<String,PreprocessorInstance> entry: processors.entrySet()) {
				if (language==null || language.length()==0 || entry.getKey().equals(language)) {
					r = entry.getValue().process(r);
				}
			}
		}
		return r;
	}

	public void initialize() {
		for (Entry<String,PreprocessorInstance> entry: processors.entrySet()) {
			entry.getValue().initialize();
		}
	}
	
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem langsElem = new JsElem("langages",true);
		json.rootElement.children.add(langsElem);
		for (Entry<String,PreprocessorInstance> entry: processors.entrySet()) {
			JsElem langElem = new JsElem();
			langsElem.children.add(langElem);
			langElem.children.add(new JsElem("code",entry.getKey(),true));
			JsElem repsElem = new JsElem("replacements",true);
			langElem.children.add(repsElem);
			for (PreprocessReplacement pr: entry.getValue().getReplacements()) {
				JsElem repElem = new JsElem();
				repsElem.children.add(repElem);
				repElem.children.add(new JsElem("key",pr.key,true));
				repElem.children.add(new JsElem("value",pr.value,true));
			}
		}
		return json;
	}

	public void fromJson(JsFile json) {
		if (json.rootElement!=null && json.rootElement.children.size()>0) {
			for (JsElem langElem: json.rootElement.children.get(0).children) {
				String code = langElem.getChildValueByName("code").toString();
				PreprocessorInstance sp = processors.get(code);
				if (sp==null) {
					sp = new PreprocessorInstance();
					processors.put(code,sp);
				}
				for (JsElem repElem: langElem.getChildByName("replacements").children) {
					String key = repElem.getChildValueByName("key").toString();
					String value = repElem.getChildValueByName("value").toString();
					if (sp.getReplacement(key)==null) {
						sp.addReplacement(key, value);
					}
				}
			}
		}
	}

	public SortedMap<String, PreprocessorInstance> getProcessors() {
		return processors;
	}
	
	protected void addDefaultProcessors() {
		processors.put(BaseConfiguration.LANG_ENG,new EnglishPreprocessor());
		processors.put(BaseConfiguration.LANG_NLD,new DutchPreprocessor());
	}
}
