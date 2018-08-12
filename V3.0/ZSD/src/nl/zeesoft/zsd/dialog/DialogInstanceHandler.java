package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.sequence.SequenceMatcherResult;

public abstract class DialogInstanceHandler {
	private Messenger								messenger		= null;
	private DialogHandlerConfiguration				config			= null;
	private DialogInstance							dialog			= null;
	private DialogResponse							response		= null;
	private DialogResponseOutput					responseOutput	= null;
	private List<DialogVariableValue> 				updatedValues	= new ArrayList<DialogVariableValue>();
	
	public void setMessenger(Messenger messenger) {
		this.messenger = messenger;
	}
	
	public void setConfig(DialogHandlerConfiguration config) {
		this.config = config;
	}
	
	public void setDialog(DialogInstance dialog) {
		this.dialog = dialog;
	}
	
	public void handleDialogIO(DialogResponse r) {
		String promptVariable = "";
		response = r;
		responseOutput = new DialogResponseOutput(dialog.getContext());
		promptVariable = initializeVariables();
		buildDialogResponseOutput(promptVariable);
	}
	
	protected String initializeVariables() {
		String promptVariable = "";
		for (DialogVariable variable: dialog.getVariables()) {
			DialogVariableValue dvv = response.getRequest().dialogVariableValues.get(variable.name);
			if (dvv==null) {
				dvv = new DialogVariableValue();
				dvv.name = variable.name;
				responseOutput.values.put(variable.name,dvv);
			} else {
				responseOutput.values.put(variable.name,dvv.copy());
			}
			if (dvv.internalValue.length()==0) {
				dvv.internalValue = variable.initialValue;
			}
		}
		
		List<String> iVals = response.entityValueTranslation.toSymbols();
		List<String> iValsCor = response.entityValueTranslationCorrected.toSymbols();
		for (DialogVariable variable: dialog.getVariables()) {
			DialogVariableValue dvv = response.getRequest().dialogVariableValues.get(variable.name);
			if (!variable.name.equals(DialogInstance.VARIABLE_NEXT_DIALOG) && (dvv==null || dvv.internalValue.length()==0)) {
				String val = getConfig().getEntityValueTranslator().getTypeValueFromInternalValues(iVals,variable.type,variable.complexName,variable.complexType);
				String valCor = getConfig().getEntityValueTranslator().getTypeValueFromInternalValues(iValsCor,variable.type,variable.complexName,variable.complexType);
				String valSel = val;
				if (valSel.length()==0 || (!variable.type.equals(BaseConfiguration.TYPE_ALPHABETIC) && valCor.length()>0)) {
					valSel = valCor;
				}
				if (valSel.length()>0) {
					String extVal = getConfig().getEntityValueTranslator().getExternalValueForInternalValues(valSel,variable.type);
					setDialogVariableValue(variable.name,extVal,valSel);
				}
			}
		}

		for (DialogVariable variable: dialog.getVariables()) {
			DialogVariableValue dvv = responseOutput.values.get(variable.name);
			if (dvv.internalValue.length()==0) {
				promptVariable = dvv.name;
				break;
			}
		}

		return promptVariable;
	}

	protected void setDialogVariableValue(String name,String extVal) {
		setDialogVariableValue(name,extVal,"");
	}

	protected void setDialogVariableValue(String name,String extVal,String intVal) {
		if (responseOutput.setDialogVariableValue(response,name,extVal,intVal)) {
			DialogVariableValue val = responseOutput.values.get(name);
			if (!updatedValues.contains(val)) {
				updatedValues.add(val);
			}
		}
	}
	
	protected void buildDialogResponseOutput(String promptVariable) {
		if (promptVariable.length()==0 || !response.getRequest().getDialogId().equals(dialog.getId())) {
			response.addDebugLogLine("    Find matches for sequence: ",response.classificationSequence);
			DialogRequest req = (DialogRequest) response.request;
			String context = "";
			if (req.filterContexts.size()>0) {
				for (String matcherContext: dialog.getMatcher().getKnownContexts()) {
					for (String filterContext: req.filterContexts) {
						if (matcherContext.equals(filterContext)) {
							context = filterContext;
							response.addDebugLogLine("    Selected filter context: ",context);
							break;
						}
					}
					if (context.length()>0) {
						break;
					}
				}
			}
			List<SequenceMatcherResult> matches = dialog.getMatcher().getMatches(response.classificationSequence,context,true,response.getRequest().matchThreshold);
			response.addDebugLogLine("    Found matches for sequence: ","" + matches.size());
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
						response.addDebugLogLine("    - ",val);
					} else if (added<10) {
						ZStringBuilder str = new ZStringBuilder();
						str.append(match.result.sequence);
						str.append(" (");
						str.append("" + match.prob);
						str.append(" / ");
						str.append("" + match.probNormalized);
						str.append(")");
						response.addDebugLogLine("    - ",str);
					}
					added++;
				}
				SequenceMatcherResult sel = getOption(response.getRequest().randomizeOutput,options);
				List<ZStringBuilder> split = sel.result.sequence.split(dialog.getMatcher().getIoSeparator());
				responseOutput.output = new ZStringSymbolParser(split.get(1).substring(1));
			}
		}
		
		if (updatedValues.size()>0 || promptVariable.length()>0) {
			setPrompt(promptVariable);
		}
		
		if (checkResponseOutput()) {
			responseOutput.getOutputFromPrompt();
			replaceVariablesAndCorrectCase(responseOutput,responseOutput.output);
			replaceVariablesAndCorrectCase(responseOutput,responseOutput.prompt);
			response.contextOutputs.add(responseOutput);
			response.addDebugLogLine("    Set dialog output: ",responseOutput.output);
			if (responseOutput.prompt.length()>0) {
				response.addDebugLogLine("    Set dialog prompt: ",responseOutput.prompt);
			}
		}
	}
	
	protected void setPrompt(String promptVariable) {
		responseOutput.promptVariableName = promptVariable;
		response.addDebugLogLine("    Prompt variable: ",promptVariable);
		DialogVariable variable = dialog.getVariable(promptVariable);
		if (variable!=null) {
			responseOutput.promptVariableType = variable.type;
			responseOutput.prompt = new ZStringSymbolParser(variable.getPrompt(response.getRequest().randomizeOutput));
		}
	}

	protected boolean checkResponseOutput() {
		return (responseOutput.output.length()>0 || responseOutput.prompt.length()>0);
	}
	
	protected void replaceVariablesAndCorrectCase(DialogResponseOutput responseOutput, ZStringSymbolParser output) {
		if (output.length()>0) {
			output.replace("{selfName}",getConfig().getBase().getName());
			for (Entry<String,DialogVariableValue> entry: responseOutput.values.entrySet()) {
				output.replace("{" + entry.getKey() + "}",entry.getValue().externalValue);
			}
			output.replace(0,1,output.substring(0,1).toString().toUpperCase());
		}
	}

	protected Messenger getMessenger() {
		return messenger;
	}
	
	protected DialogHandlerConfiguration getConfig() {
		return config;
	}

	protected DialogInstance getDialog() {
		return dialog;
	}
	
	protected DialogResponse getResponse() {
		return response;
	}

	protected DialogResponseOutput getResponseOutput() {
		return responseOutput;
	}
	
	protected String getExternalValueForNumber(int num) {
		String r = "" + num;
		EntityObject eo = getConfig().getEntityValueTranslator().getEntityObject(getDialog().getLanguage(),BaseConfiguration.TYPE_NUMERIC);
		if (eo!=null) {
			String test = eo.getExternalValueForInternalValue(r);
			if (test.length()>0) {
				r = test;
			}
		}
		return r;
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
