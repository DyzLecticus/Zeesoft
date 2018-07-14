package nl.zeesoft.zsd;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.dialog.DialogHandlerConfiguration;
import nl.zeesoft.zsd.dialog.DialogInstance;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.sequence.SequenceMatcherResult;

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
			r.addDebugLogLine("Selected dialog: ",dialogId);
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
			}
			
		}
		
		return r;
	}
	
	@Override
	protected DialogHandlerConfiguration getConfiguration() {
		return (DialogHandlerConfiguration) super.getConfiguration();
	}
}
