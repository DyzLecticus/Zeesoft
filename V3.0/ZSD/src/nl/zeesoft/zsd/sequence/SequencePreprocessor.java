package nl.zeesoft.zsd.sequence;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.initialize.Initializable;

public abstract class SequencePreprocessor implements Initializable {
	private SortedMap<String,String>	replacements	= new TreeMap<String,String>();

	@Override
	public void initialize(List<ZStringBuilder> data) {
		// TODO Parse JSON, create replacements
	}
	
	public abstract void initialize();
	
	public ZStringSymbolParser process(ZStringSymbolParser sequence) {
		ZStringSymbolParser r = new ZStringSymbolParser(sequence);
		r.trim();
		if (r.length()>0) {
			List<String> symbols = r.toSymbolsPunctuated();
			r.fromSymbols(symbols,true,true);
			String end = r.substring(r.length() - 1,r.length()).toString();
			if (!ZStringSymbolParser.isLineEndSymbol(end)) {
				r.append(".");
			}
			for (Entry<String,String> entry: replacements.entrySet()) {
				r.replace(entry.getKey(),entry.getValue());
			}
		}
		return r;
	}
	
	public void addReplacement(String key, String value) {
		replacements.put(key,value);
	}

	public SortedMap<String, String> getReplacements() {
		return replacements;
	}
}
