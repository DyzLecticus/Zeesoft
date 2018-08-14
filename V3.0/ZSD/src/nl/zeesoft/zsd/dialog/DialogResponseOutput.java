package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

public class DialogResponseOutput {
	public String									context							= "";
	public ZStringSymbolParser						output							= new ZStringSymbolParser();
	public ZStringSymbolParser						prompt							= new ZStringSymbolParser();
	public String									promptVariableName				= "";
	public String									promptVariableType				= "";
	public SortedMap<String,DialogVariableValue>	values							= new TreeMap<String,DialogVariableValue>();

	public DialogResponseOutput() {
		
	}

	public DialogResponseOutput(String context) {
		this.context = context;
	}

	public void appendOutput(String add) {
		if (output.length()>0) {
			output.append(" ");
		}
		output.append(add);
	}

	public void appendOutput(ZStringBuilder add) {
		if (output.length()>0) {
			output.append(" ");
		}
		output.append(add);
	}
	
	public void getOutputFromPrompt() {
		if (prompt.length()>0) {
			List<ZStringSymbolParser> sentences = parseSentences(prompt);
			if (sentences.size()>1) {
				ZStringBuilder ori = prompt;
				prompt = sentences.get(sentences.size() - 1);
				ZStringSymbolParser remaining = new ZStringSymbolParser(ori.substring(0,ori.length() - prompt.length() - 1));
				if (output.length()>0) {
					output.append(" ");
				}
				output.append(remaining);
			}
		}
	}

	public void clearDialogVariableValues() {
		for (Entry<String,DialogVariableValue> entry: values.entrySet()) {
			entry.getValue().internalValue = "";
			entry.getValue().externalValue = "";
		}
	}

	public boolean setDialogVariableValue(DialogResponse r,String name,String extVal,boolean session) {
		return setDialogVariableValue(r,name,extVal,"",session);
	}
	
	public boolean setDialogVariableValue(DialogResponse r,String name,String extVal,String intVal,boolean session) {
		boolean changed = false;
		DialogVariableValue dvv = values.get(name);
		if (dvv==null) {
			dvv = new DialogVariableValue();
			dvv.name = name;
			values.put(dvv.name,dvv);
			changed = true;
		} else if (!dvv.externalValue.equals(extVal) || !dvv.internalValue.equals(intVal)) {
			changed = true;
		}
		if (changed) {
			dvv.externalValue = extVal;
			dvv.internalValue = intVal;
			dvv.session = session;
			ZStringBuilder line = new ZStringBuilder("    Updated");
			if (session) {
				line.append(" session");
			}
			line.append(" variable ");
			line.append(dvv.name);
			line.append(": ");
			ZStringBuilder value = new ZStringBuilder();
			if (dvv.internalValue.length()>0) {
				value.append(dvv.internalValue);
				value.append(" = ");
			}
			value.append(dvv.externalValue);
			r.addDebugLogLine(line,value);
		}
		return changed;
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
