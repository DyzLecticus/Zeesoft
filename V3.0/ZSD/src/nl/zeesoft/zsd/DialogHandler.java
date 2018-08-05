package nl.zeesoft.zsd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

/**
 * A DialogHandler can be used to interpret sequences and provide dialog oriented output using several methods.
 */
public class DialogHandler extends SequenceInterpreter {
	public DialogHandler(DialogHandlerConfiguration c) {
		super(c);
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
		String masterContext = request.masterContext;
		
		if (r.responseLanguages.size()>0) {
			language = r.responseLanguages.get(0).symbol;
		}
		if (language.length()==0) {
			language = getConfiguration().getBase().getPrimaryLanguage();
		}
		if (r.responseMasterContexts.size()>0) {
			masterContext = r.responseMasterContexts.get(0).symbol;
		}
		List<String> processContexts = new ArrayList<String>();
		if (!r.request.classifyContext && r.request.context.length()>0) {
			processContexts.add(r.request.context);
		} else {
			for (SequenceClassifierResult res: r.responseContexts) {
				processContexts.add(res.symbol);
			}
			if (processContexts.size()==0 && r.request.context.length()>0) {
				processContexts.add(r.request.context);
			}
		}
		
		for (String processContext: processContexts) {
			DialogInstance dialog = getConfiguration().getDialogSet().getDialog(language,masterContext,processContext);
			String dialogId = language + "/" + masterContext + "/" + processContext;
			if (dialog==null) {
				r.addDebugLogLine("Dialog not found: ",dialogId);
			} else {
				if (r.getRequest().getDialogId().equals(dialogId)) {
					r.addDebugLogLine("Continuing dialog: ",dialogId);
				} else {
					r.addDebugLogLine("Handling dialog: ",dialogId);
				}
				DialogInstanceHandler handler = dialog.getNewHandler();
				if (handler==null) {
					handler = new GenericQnAHandler();
				}
				handler.setConfig(getConfiguration());
				handler.setDialog(dialog);
				r.addDebugLogLine("    Initialized handler: ",handler.getClass().getName());
				handler.handleDialogIO(r);
			}
			if ((new Date()).getTime()>=(started.getTime() + getConfiguration().getBase().getMaxMsDialogPerSequence())) {
				r.addDebugLogLine("Context output processing timed out");
				break;
			}
		}
		
		return r;
	}
	
	@Override
	protected DialogHandlerConfiguration getConfiguration() {
		return (DialogHandlerConfiguration) super.getConfiguration();
	}
}
