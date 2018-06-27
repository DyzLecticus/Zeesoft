package nl.zeesoft.zsd;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

/**
 * A TsvToJson instance can be used to convert TSV formatted data into a JSON format for Analyzer instances.
 */
public class TsvToJson {
	
	/**
	 * Parses TSV formatted data and returns a JSON object.
	 * 
	 * @param tsv The TSV formatted data
	 * @return The JSON object
	 */
	public JsFile parseTsv(ZStringBuilder tsv) {
		JsFile r = new JsFile();
		r.rootElement = new JsElem();
		if (tsv.containsOneOfCharacters("\n")) {
			List<ZStringBuilder> lines = tsv.split("\n");
			int l = 0;
			for (ZStringBuilder line: lines) {
				if (l>0) {
					if (line.containsOneOfCharacters("\t")) {
						List<ZStringBuilder> sequences = line.split("\t");
						if (sequences.size()>1) {
							if (sequences.size()>2) {
								addSequenceElem(r.rootElement,sequences.get(0),sequences.get(1),sequences.get(2));
							} else {
								addSequenceElem(r.rootElement,sequences.get(0),sequences.get(1),null);
							}
						}
					} else {
						addSequenceElem(r.rootElement,line,null,null);
					}
				}
				l++;
			}
		} else {
			addSequenceElem(r.rootElement,tsv,null,null);
		}
		return r;
	}
	
	private void addSequenceElem(JsElem parent,ZStringBuilder input, ZStringBuilder output, ZStringBuilder context) {
		JsElem seq = new JsElem("sequence");
		parent.children.add(seq);
		seq.children.add(new JsElem("input",input,true));
		if (output!=null && output.length()>0) {
			seq.children.add(new JsElem("output",output,true));
		}
		if (context!=null && context.length()>0) {
			seq.children.add(new JsElem("context",context,true));
		}
	}
}
