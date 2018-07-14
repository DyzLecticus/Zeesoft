package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

public class DialogResponseOutput {
	public String									context							= "";
	public ZStringSymbolParser						output							= new ZStringSymbolParser();
	public ZStringSymbolParser						prompt							= new ZStringSymbolParser();

	public DialogResponseOutput() {
		
	}

	public DialogResponseOutput(String context,ZStringSymbolParser output) {
		this.context = context;
		setOutput(output);
	}
	
	public void setOutput(ZStringSymbolParser output) {
		if (output.endsWith("?")) {
			List<ZStringSymbolParser> sentences = parseSentences(output);
			prompt.append(sentences.get(sentences.size() - 1));
			if (sentences.size()>1) {
				this.output.append(output.substring(0,output.length() - prompt.length() - 1));
			}
		} else {
			this.output = output;
		}
	}
	
	private List<ZStringSymbolParser> parseSentences(ZStringSymbolParser sequence) {
		List<ZStringSymbolParser> r = new ArrayList<ZStringSymbolParser>();
		addSplitsToList(sequence,r);
		return r;
	}
	
	private void addSplitsToList(ZStringSymbolParser sequence,List<ZStringSymbolParser> list) {
		List<ZStringBuilder> splitted = sequence.split(". ");
		for (ZStringBuilder spl: splitted) {
			List<ZStringBuilder> splitted2 = spl.split("! ");
			for (ZStringBuilder spl2: splitted2) {
				List<ZStringBuilder> splitted3 = spl2.split("? ");
				for (ZStringBuilder spl3: splitted3) {
					list.add(new ZStringSymbolParser(spl3));
				}
			}
		}
	}
}
