package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.sequence.SequenceMatcherResult;

public abstract class DialogInstanceHandler {
	private DialogHandlerConfiguration				config	= null;
	private DialogInstance							dialog	= null;
	private SortedMap<String,DialogVariableValue>	values	= new TreeMap<String,DialogVariableValue>();
	
	public void setConfig(DialogHandlerConfiguration config) {
		this.config = config;
	}
	
	public void setDialog(DialogInstance dialog) {
		this.dialog = dialog;
	}
	
	public void handleDialogIO(DialogResponse r) {
		String promptVariable = "";
		
		for (DialogVariable variable: dialog.getVariables()) {
			DialogVariableValue dvv = r.getRequest().dialogVariableValues.get(variable.name);
			if (dvv==null) {
				dvv = new DialogVariableValue();
				dvv.name = variable.name;
				values.put(variable.name,dvv);
			} else {
				values.put(variable.name,dvv.copy());
			}
			if (dvv.internalValue.length()==0) {
				dvv.internalValue = variable.initialValue;
			}
		}

		List<DialogVariableValue> updatedValues = new ArrayList<DialogVariableValue>();
		List<String> iVals = r.entityValueTranslation.toSymbols();
		List<String> iValsCor = r.entityValueTranslationCorrected.toSymbols();
		for (DialogVariable variable: dialog.getVariables()) {
			// TODO: Solution for 'next dialog' variable type?
			if (!variable.name.equals(DialogInstance.VARIABLE_NEXT_DIALOG)) {
				String val = config.getEntityValueTranslator().getTypeValueFromInternalValues(iVals,variable.type,variable.complexName,variable.complexType);
				String valCor = config.getEntityValueTranslator().getTypeValueFromInternalValues(iValsCor,variable.type,variable.complexName,variable.complexType);
				String valSel = "";
				if (val.length()>0 && val.equals(valCor)) {
					valSel = val;
				}
				if (valSel.length()>0) {
					DialogVariableValue dvv = values.get(variable.name);
					if (!dvv.internalValue.equals(valSel)) {
						dvv.internalValue = valSel;
						dvv.externalValue = getConfig().getEntityValueTranslator().getExternalValueForInternalValues(valSel,variable.type); 
						updatedValues.add(dvv);
						ZStringBuilder line = new ZStringBuilder("    Updated variable ");
						line.append(dvv.name);
						line.append(": ");
						r.addDebugLogLine(line,dvv.internalValue);
					}
				}
			}
		}

		for (DialogVariable variable: dialog.getVariables()) {
			DialogVariableValue dvv = values.get(variable.name);
			if (dvv.internalValue.length()==0) {
				promptVariable = dvv.name;
				break;
			}
		}

		ZStringSymbolParser output = new ZStringSymbolParser();
		if (updatedValues.size()>0) {
			output = updatedValues(r,updatedValues,promptVariable);
		} else {
			if (promptVariable.length()==0 || !r.getRequest().getDialogId().equals(dialog.getId())) {
				List<SequenceMatcherResult> matches = dialog.getMatcher().getMatches(r.correctedInput,"",true,r.getRequest().matchThreshold);
				if (matches.size()==0) {
					r.addDebugLogLine("    Failed to find matches for sequence: ",r.correctedInput);
				} else {
					r.addDebugLogLine("    Found matches for sequence: ","" + matches.size());
					List<SequenceMatcherResult> options = new ArrayList<SequenceMatcherResult>();
					for (SequenceMatcherResult match: matches) {
						if (match.probNormalized==1D) {
							options.add(match);
						}
						ZStringBuilder str = new ZStringBuilder();
						str.append(match.result.sequence);
						str.append(" (");
						str.append("" + match.prob);
						str.append(" / ");
						str.append("" + match.probNormalized);
						str.append(")");
						r.addDebugLogLine("    - ",str);
					}
					SequenceMatcherResult sel = getOption(r.getRequest().randomizeOutput,options);
					List<ZStringBuilder> split = sel.result.sequence.split(dialog.getMatcher().getIoSeparator());
					output = new ZStringSymbolParser(split.get(1).substring(1));
				}
			}
		}
		
		if (output.length()>0) {
			output.replace("{selfName}",getConfig().getBase().getName());
			for (Entry<String,DialogVariableValue> entry: values.entrySet()) {
				output.replace("{" + entry.getKey() + "}",entry.getValue().externalValue);
			}
			output.replace(0,1,output.substring(0,1).toString().toUpperCase());
			DialogResponseOutput dro = new DialogResponseOutput(dialog.getContext(),output);
			r.contextOutputs.put(dialog.getContext(),dro);
			r.addDebugLogLine("    Set dialog output: ",dro.output);
			if (dro.prompt.length()>0) {
				r.addDebugLogLine("    Set dialog prompt: ",dro.prompt);
			}
		}
	}
	
	protected ZStringSymbolParser updatedValues(DialogResponse r,List<DialogVariableValue> updatedValues,String promptVariable) {
		r.addDebugLogLine("    Prompt variable: ",promptVariable);
		ZStringSymbolParser output = new ZStringSymbolParser();
		DialogVariable variable = dialog.getVariable(promptVariable);
		if (variable!=null) {
			output = variable.getPrompt(r.getRequest().randomizeOutput);
		}
		return output;
	}

	protected DialogHandlerConfiguration getConfig() {
		return config;
	}

	protected DialogInstance getDialog() {
		return dialog;
	}

	protected SortedMap<String, DialogVariableValue> getValues() {
		return values;
	}
	
	private SequenceMatcherResult getOption(boolean random,List<SequenceMatcherResult> options) {
		SequenceMatcherResult r = null;
		int num = 0;
		if (random && options.size()>1) {
			ZIntegerGenerator generator = new ZIntegerGenerator(0,(options.size() - 1));
			num = generator.getNewInteger();
		}
		if (num<options.size()) {
			r = options.get(num);
		}
		return r;
	}
}
