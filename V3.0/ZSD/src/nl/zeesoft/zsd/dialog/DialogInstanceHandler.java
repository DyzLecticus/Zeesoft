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
			if (!variable.name.equals(DialogInstance.VARIABLE_NEXT_DIALOG)) {
				String val = config.getEntityValueTranslator().getTypeValueFromInternalValues(iVals,variable.type,variable.complexName,variable.complexType);
				String valCor = config.getEntityValueTranslator().getTypeValueFromInternalValues(iValsCor,variable.type,variable.complexName,variable.complexType);
				String valSel = val;
				if (!variable.type.equals(BaseConfiguration.TYPE_ALPHABETIC) && valCor.length()>0) {
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
		dro.promptVariable = promptVariable;
		r.addDebugLogLine("    Prompt variable: ",promptVariable);
		DialogVariable variable = dialog.getVariable(promptVariable);
		if (variable!=null) {
			dro.prompt = variable.getPrompt(r.getRequest().randomizeOutput);
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
