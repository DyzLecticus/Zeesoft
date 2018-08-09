package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.sequence.SequenceMatcherResult;

public abstract class DialogInstanceHandler {
	private DialogHandlerConfiguration				config	= null;
	private DialogInstance							dialog	= null;
	
	public void setConfig(DialogHandlerConfiguration config) {
		this.config = config;
	}
	
	public void setDialog(DialogInstance dialog) {
		this.dialog = dialog;
	}
	
	public void handleDialogIO(DialogResponse r) {
		String promptVariable = "";
		
		DialogResponseOutput dro = new DialogResponseOutput(dialog.getContext());
		
		for (DialogVariable variable: dialog.getVariables()) {
			DialogVariableValue dvv = r.getRequest().dialogVariableValues.get(variable.name);
			if (dvv==null) {
				dvv = new DialogVariableValue();
				dvv.name = variable.name;
				dro.values.put(variable.name,dvv);
			} else {
				dro.values.put(variable.name,dvv.copy());
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
			DialogVariableValue dvv = r.getRequest().dialogVariableValues.get(variable.name);
			if (!variable.name.equals(DialogInstance.VARIABLE_NEXT_DIALOG) && (dvv==null || dvv.internalValue.length()==0)) {
				String val = getConfig().getEntityValueTranslator().getTypeValueFromInternalValues(iVals,variable.type,variable.complexName,variable.complexType);
				String valCor = getConfig().getEntityValueTranslator().getTypeValueFromInternalValues(iValsCor,variable.type,variable.complexName,variable.complexType);
				String valSel = val;
				if (valSel.length()==0 || (!variable.type.equals(BaseConfiguration.TYPE_ALPHABETIC) && valCor.length()>0)) {
					valSel = valCor;
				}
				if (valSel.length()>0) {
					String extVal = getConfig().getEntityValueTranslator().getExternalValueForInternalValues(valSel,variable.type);
					if (dro.setDialogVariableValue(r,variable.name,extVal,valSel)) {
						updatedValues.add(dro.values.get(variable.name));
					}
				}
			}
		}

		for (DialogVariable variable: dialog.getVariables()) {
			DialogVariableValue dvv = dro.values.get(variable.name);
			if (dvv.internalValue.length()==0) {
				promptVariable = dvv.name;
				break;
			}
		}
		
		buildDialogResponseOutput(r,dro,updatedValues,promptVariable);
	}
	
	public void buildDialogResponseOutput(DialogResponse r,DialogResponseOutput dro,List<DialogVariableValue> updatedValues,String promptVariable) {
		if (promptVariable.length()==0 || !r.getRequest().getDialogId().equals(dialog.getId())) {
			r.addDebugLogLine("    Find matches for sequence: ",r.classificationSequence);
			List<SequenceMatcherResult> matches = dialog.getMatcher().getMatches(r.classificationSequence,"",true,r.getRequest().matchThreshold);
			r.addDebugLogLine("    Found matches for sequence: ","" + matches.size());
			if (matches.size()>0) {
				List<SequenceMatcherResult> options = new ArrayList<SequenceMatcherResult>();
				double highest = matches.get(0).probNormalized;
				int added = 0;
				for (SequenceMatcherResult match: matches) {
					if (match.probNormalized==highest) {
						options.add(match);
					}
					if (added==10) {
						ZStringBuilder val = new ZStringBuilder();
						val.append("[... ");
						val.append("" + (matches.size() - 10));
						val.append("]");
						r.addDebugLogLine("    - ",val);
					} else if (added<10) {
						ZStringBuilder str = new ZStringBuilder();
						str.append(match.result.sequence);
						str.append(" (");
						str.append("" + match.prob);
						str.append(" / ");
						str.append("" + match.probNormalized);
						str.append(")");
						r.addDebugLogLine("    - ",str);
					}
					added++;
				}
				SequenceMatcherResult sel = getOption(r.getRequest().randomizeOutput,options);
				List<ZStringBuilder> split = sel.result.sequence.split(dialog.getMatcher().getIoSeparator());
				dro.output = new ZStringSymbolParser(split.get(1).substring(1));
			}
		}
		
		if (updatedValues.size()>0 || promptVariable.length()>0) {
			setPrompt(r,dro,updatedValues,promptVariable);
		}
		
		if (dro.output.length()>0 || dro.prompt.length()>0) {
			dro.getOutputFromPrompt();
			replaceVariablesAndCorrectCase(dro,dro.output);
			replaceVariablesAndCorrectCase(dro,dro.prompt);
			r.contextOutputs.add(dro);
			r.addDebugLogLine("    Set dialog output: ",dro.output);
			if (dro.prompt.length()>0) {
				r.addDebugLogLine("    Set dialog prompt: ",dro.prompt);
			}
		}
	}

	protected void replaceVariablesAndCorrectCase(DialogResponseOutput dro, ZStringSymbolParser output) {
		if (output.length()>0) {
			output.replace("{selfName}",getConfig().getBase().getName());
			for (Entry<String,DialogVariableValue> entry: dro.values.entrySet()) {
				output.replace("{" + entry.getKey() + "}",entry.getValue().externalValue);
			}
			output.replace(0,1,output.substring(0,1).toString().toUpperCase());
		}
	}
	
	protected void setPrompt(DialogResponse r,DialogResponseOutput dro,List<DialogVariableValue> updatedValues,String promptVariable) {
		dro.promptVariableName = promptVariable;
		r.addDebugLogLine("    Prompt variable: ",promptVariable);
		DialogVariable variable = dialog.getVariable(promptVariable);
		if (variable!=null) {
			dro.promptVariableType = variable.type;
			dro.prompt = new ZStringSymbolParser(variable.getPrompt(r.getRequest().randomizeOutput));
		}
	}
	
	protected DialogHandlerConfiguration getConfig() {
		return config;
	}

	protected DialogInstance getDialog() {
		return dialog;
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
