package nl.zeesoft.zsd;

import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler;

public class DialogHandler extends SequenceInterpreter {
	public DialogHandler(DialogHandlerConfiguration c) {
		super(c);
	}

	public DialogResponse handleDialogRequest(DialogRequest request) {
		if (request.masterContext.length()==0) {
			request.classifyMasterContext = true;
		}
		if (request.context.length()==0) {
			request.classifyContext = true;
		}
		request.translateEntiyValues = true;

		DialogResponse r = new DialogResponse(request);
		buildInterpreterResponse(r);
		
		String language = request.language;
		String masterContext = request.masterContext;
		String context = request.context;
		
		if (r.responseLanguages.size()>0) {
			language = r.responseLanguages.get(0).symbol;
		}
		if (language.length()==0) {
			language = getConfiguration().getBase().getPrimaryLanguage();
		}
		if (r.responseMasterContexts.size()>0) {
			masterContext = r.responseMasterContexts.get(0).symbol;
		}
		if (r.responseContexts.size()>0) {
			context = r.responseContexts.get(0).symbol;
		}
		
		DialogInstance dialog = getConfiguration().getDialogSet().getDialog(language,masterContext,context);
		String dialogId = language + "/" + masterContext + "/" + context;
		if (dialog==null) {
			r.addDebugLogLine("Dialog not found: ",dialogId);
		} else {
			if (r.getRequest().getDialogId().equals(dialogId)) {
				r.addDebugLogLine("Continuing dialog: ",dialogId);
			} else {
				r.addDebugLogLine("Selected dialog: ",dialogId);
			}
			DialogInstanceHandler handler = dialog.getNewHandler();
			if (handler==null) {
				handler = new GenericQnAHandler();
			}
			handler.setDialog(dialog);
			r.addDebugLogLine("Initialized handler: ",handler.getClass().getName());
			handler.handleDialogIO(r);
		}
		
		return r;
	}
	
	@Override
	protected DialogHandlerConfiguration getConfiguration() {
		return (DialogHandlerConfiguration) super.getConfiguration();
	}
}
