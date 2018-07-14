package nl.zeesoft.zsd.dialog;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.sequence.SequenceMatcherResult;

public abstract class DialogInstanceHandler {
	private DialogInstance							dialog	= null;
	private SortedMap<String,DialogVariableValue>	values	= new TreeMap<String,DialogVariableValue>();
	
	public void setDialog(DialogInstance dialog) {
		this.dialog = dialog;
	}
	
	public void handleDialogIO(DialogResponse r) {
		String promptVariable = "";
		for (Entry<String,DialogVariable> entry: dialog.getVariables().entrySet()) {
			DialogVariableValue dvv = r.getRequest().dialogVariableValues.get(entry.getKey());
			if (dvv==null) {
				dvv = new DialogVariableValue();
				dvv.name = entry.getKey();
				values.put(entry.getKey(),dvv);
			} else {
				values.put(entry.getKey(),dvv.copy());
			}
			if (dvv.internalValue.length()==0) {
				dvv.internalValue = entry.getValue().initialValue;
			}
			if (dvv.internalValue.length()==0) {
				promptVariable = dvv.name;
			}
		}
		if (promptVariable.length()==0 || !r.getRequest().getDialogId().equals(dialog.getId())) {
			List<SequenceMatcherResult> matches = dialog.getMatcher().getMatches(r.correctedInput,"",true);
			if (matches.size()==0) {
				r.addDebugLogLine("Failed to find matches for sequence: ",r.correctedInput);
			} else {
				r.addDebugLogLine("Found matches for sequence: ","" + matches.size());
				for (SequenceMatcherResult match: matches) {
					ZStringBuilder str = new ZStringBuilder();
					str.append(match.result.sequence);
					str.append(" (");
					str.append("" + match.prob);
					str.append(" / ");
					str.append("" + match.probNormalized);
					str.append(")");
					r.addDebugLogLine(" - ",str);
				}
				List<ZStringBuilder> split = matches.get(0).result.sequence.split(dialog.getMatcher().getIoSeparator());
				ZStringSymbolParser output = new ZStringSymbolParser(split.get(1).substring(1));
				DialogResponseOutput dro = new DialogResponseOutput(dialog.getContext(),output);
				r.contextOutputs.put(dialog.getContext(),dro);
				r.addDebugLogLine("Selected match output: ",dro.output);
				if (dro.prompt.length()>0) {
					r.addDebugLogLine("Selected match prompt: ",dro.prompt);
				}
			}
		}
	}
}
