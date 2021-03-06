package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.dialogs.Generic;
import nl.zeesoft.zsd.dialog.dialogs.GenericClassification;
import nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

/**
 * A DialogHandler can be used to interpret sequences and provide dialog oriented output using several methods.
 */
public class DialogHandler extends SequenceInterpreter {
	public DialogHandler(DialogHandlerConfiguration c) {
		super(c);
	}

	public DialogHandler(Messenger msgr,DialogHandlerConfiguration c) {
		super(msgr,c);
	}
	
	/**
	 * Handles a dialog request.
	 * 
	 * @param request The dialog request
	 * @return  The dialog response
	 */
	public DialogResponse handleDialogRequest(DialogRequest request) {
		if (request.masterContext.length()==0) {
			request.classifyMasterContext = true;
		}
		if (request.context.length()==0) {
			request.classifyContext = true;
		}
		request.translateEntityValues = true;

		DialogResponse r = new DialogResponse(request);
		buildInterpreterResponse(r);
		
		Date started = new Date();

		String language = request.language;
		boolean selectedPrimaryLanguage = false;
		String masterContext = request.masterContext;
		
		if (r.classifiedLanguages.size()>0) {
			language = r.classifiedLanguages.get(0).symbol;
		}
		if (language.length()==0) {
			selectedPrimaryLanguage = true;
			language = getConfiguration().getBase().getPrimaryLanguage();
		}
		if (r.classifiedMasterContexts.size()>0) {
			masterContext = r.classifiedMasterContexts.get(0).symbol;
		}
		List<String> processContexts = new ArrayList<String>();
		if (!r.request.classifyContext && r.request.context.length()>0) {
			processContexts.add(r.request.context);
		} else {
			for (SequenceClassifierResult res: r.classifiedContexts) {
				processContexts.add(res.symbol);
			}
			if (processContexts.size()==0 && r.request.context.length()>0) {
				processContexts.add(r.request.context);
			}
		}
		
		if (masterContext.length()==0 || processContexts.size()==0) {
			DialogInstance dialog = getGenericClassificationDialog(language);
			if (dialog!=null) {
				masterContext = dialog.getMasterContext();
				processContexts.clear();
				processContexts.add(dialog.getContext());
				if (selectedPrimaryLanguage) {
					r.classificationSequence = new ZStringSymbolParser(GenericClassification.TRIGGER_INPUT_LANGUAGE);
				} else {
					r.classificationSequence = new ZStringSymbolParser(GenericClassification.TRIGGER_INPUT_CONTEXT);
				}
			}
		}
		
		for (String processContext: processContexts) {
			DialogInstance dialog = getConfiguration().getDialogSet().getDialog(language,masterContext,processContext);
			String dialogId = language + "/" + masterContext + "/" + processContext;
			if (dialog==null) {
				r.addDebugLogLine("Dialog not found: ",dialogId);
			} else {
				ZStringBuilder str = new ZStringBuilder(dialogId);
				if (r.request.isTestRequest) {
					str.append(" (TEST)");
				}
				if (r.getRequest().getDialogId().equals(dialogId)) {
					r.addDebugLogLine("Continuing dialog: ",str);
				} else {
					r.addDebugLogLine("Handling dialog: ",str);
				}
				handleDialog(r,dialog);
			}
			if ((new Date()).getTime()>=(started.getTime() + getConfiguration().getBase().getMaxMsDialogPerSequence())) {
				r.addDebugLogLine("Context output processing timed out");
				break;
			}
		}
		
		if (r.contextOutputs.size()==0) {
			DialogInstance dialog = getGenericClassificationDialog(language);
			if (dialog!=null) {
				if (selectedPrimaryLanguage) {
					r.classificationSequence = new ZStringSymbolParser(GenericClassification.TRIGGER_INPUT_LANGUAGE);
				} else {
					r.classificationSequence = new ZStringSymbolParser(GenericClassification.TRIGGER_INPUT_CONTEXT);
				}
				r.addDebugLogLine("Handling dialog: ",dialog.getId());
				handleDialog(r,dialog);
			}
		}
		
		return r;
	}
	
	@Override
	protected DialogHandlerConfiguration getConfiguration() {
		return (DialogHandlerConfiguration) super.getConfiguration();
	}
	
	protected void handleDialog(DialogResponse r,DialogInstance dialog) {
		boolean log = false;
		if (r.contextOutputs.size()==0) {
			log = true;
		}
		DialogInstanceHandler handler = dialog.getNewHandler();
		if (handler==null) {
			handler = new GenericQnAHandler();
		}
		handler.setMessenger(getMessenger());
		handler.setConfig(getConfiguration());
		handler.setDialog(dialog);
		r.addDebugLogLine("    Initialized handler: ",handler.getClass().getName());
		handler.handleDialogIO(r);
		if (log && r.contextOutputs.size()>0) {
			logClassification(r,dialog);
		}
	}
	
	protected DialogInstance getGenericClassificationDialog(String language) {
		return getConfiguration().getDialogSet().getDialog(language,Generic.MASTER_CONTEXT_GENERIC,GenericClassification.CONTEXT_GENERIC_CLASSIFICATION);
	}
	
	protected void logClassification(DialogResponse r,DialogInstance dialog) {
		addResultToListIfEmpty(r.classifiedLanguages,dialog.getLanguage());
		addResultToListIfEmpty(r.classifiedMasterContexts,dialog.getMasterContext());
		addResultToListIfEmpty(r.classifiedContexts,dialog.getContext());
	}
	
	protected void addResultToListIfEmpty(List<SequenceClassifierResult> results,String symbol) {
		if (results.size()==0) {
			SequenceClassifierResult res = new SequenceClassifierResult();
			res.symbol = symbol;
			res.prob = 1.0D;
			res.probNormalized = 1.0D;
			results.add(res);
		}
	}
}
