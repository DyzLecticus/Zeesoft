package nl.zeesoft.zids.server;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zacs.database.model.Example;
import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zids.database.model.Dialog;
import nl.zeesoft.zids.database.model.DialogVariable;
import nl.zeesoft.zids.database.model.Session;
import nl.zeesoft.zids.database.model.SessionDialogVariable;
import nl.zeesoft.zids.dialog.controller.CtrObject;
import nl.zeesoft.zids.dialog.pattern.PtnManager;
import nl.zeesoft.zids.dialog.pattern.PtnObject;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;

public class SvrSessionHandler {
	public static final String		SESSION_PROCESS_INPUT_REQUEST 	= "SessionProcessInputRequest";
	public static final String		SESSION_ENDED_REQUEST 			= "SessionEndedRequest";
	
	private static final boolean 	DEBUG 							= false;
	
	private Session 				session 						= null;
	
	private PtnManager				patternManager					= null;
	
	public SvrSessionHandler(PtnManager patternManager) {
		this.patternManager = patternManager;
	}
	
	public String handleSessionRequest(StringBuilder type, StringBuilder sessionId, StringBuilder context, StringBuilder input) {
		session = null; 
		String err = "";
		if (type!=null && type.length()>=0) {
			if (type.toString().equals(SESSION_PROCESS_INPUT_REQUEST)) {
				if (input!=null) {
					session = SvrControllerSessions.getInstance().getOrAddSessionBySessionId(sessionId.toString());
					session.setOutput(getOutputForSessionInput(session,input,context));
				} else {
					err = "SessionProcessInputRequest requires input";
				}
			} else if (type.toString().equals(SESSION_ENDED_REQUEST)) {
				if (sessionId!=null && sessionId.length()>=0) {
					session = SvrControllerSessions.getInstance().getSessionBySessionId(sessionId.toString());
				}
				if (session!=null) {
					SvrControllerSessions.getInstance().closeSession(session);
				} else {
					err = "Session not found with id: " + sessionId;
				}
			} else {
				err = "Unknown session request type: " + type;
			}
		}
		return err;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	private StringBuilder[] getSessionOutputAndContextForInputAndContext(Session s,StringBuilder input,StringBuilder context, boolean contextDynamic, boolean correctInput, boolean correctLineEnd, boolean correctInputOnly) {
		StringBuilder[] outputAndContext = SvrControllerSessions.getInstance().getOutputAndContextForInputAndContext(input,context,contextDynamic,correctInput,correctLineEnd,correctInputOnly);
		if (DbConfig.getInstance().isDebug()) {
			s.getLogAssignments().append("\n");
			s.getLogAssignments().append(outputAndContext[5]);
		}
		return outputAndContext;
	}

	private StringBuilder getOutputForSessionInput(Session s, StringBuilder input, StringBuilder context) {
		StringBuilder output = new StringBuilder();
		if (context==null) {
			context = new StringBuilder();
		}
		
		input = cleanUpText(input);
		s.appendLogLine(DEBUG,"< " + input,false);
		
		List<String> expectedTypes = getExpectedTypesForDialog(s.getDialog());
		
		context = getContextForSessionInput(s,input,context,expectedTypes);
		s.setContext(context);
		s.appendLogLine(DEBUG,"- Context: " + context,true);
		Dialog dialog = getDialogForContext(context);

		expectedTypes = getExpectedTypesForDialog(dialog);
		
		//Messenger.getInstance().debug(this,"==================================");
		//Messenger.getInstance().debug(this,"Input context: " + context + ", session context: " + s.getContext());
		
		// Prepend input with previous output
		if ((dialog==null || dialog==s.getDialog()) && s.getOutput().length()>0) {
			input.insert(0," ");
			input.insert(0,s.getOutput());
		}

		if (patternManager!=null) {
			input = patternManager.scanAndTranslateInput(input,expectedTypes); 
			s.appendLogLine(DEBUG,"- Translated input: " + input,true);
		}
		
		//Messenger.getInstance().debug(this,"Context: " + context + ", input: " + input);
		
		StringBuilder[] outputAndContext = getSessionOutputAndContextForInputAndContext(s,input,context,context.length()==0,true,true,false);
		output = outputAndContext[0];
		//Messenger.getInstance().debug(this,"Context: " + context + "\n< " + input + "\n> " + output);

		if (outputAndContext[3].length()>0 && !Generic.stringBuilderEquals(input,outputAndContext[3])) {
			s.appendLogLine(DEBUG,"- Corrected input: " + outputAndContext[3],true);
		}
		
		StringBuilder variables	= outputAndContext[4];
		StringBuilder validationResponse = new StringBuilder();
		CtrObject controller = validateSessionDialogVariables(s,variables);
		if (controller!=null && controller.getResponse().length()>0) {
			validationResponse = controller.getResponse();
			if (controller.getResponseContext().length()>0) {
				context = controller.getResponseContext();
			}
		}

		//Messenger.getInstance().debug(this,"Variables: " + variables + ", validationResponse: " + validationResponse);

		boolean redo = false;
		boolean correctInput = true;
		boolean correctLineEnd = true;

		if (validationResponse.length()==0) {
			// Check dialog unselection
			if (context.length()>0 && s.getDialog()!=null && !Generic.stringBuilderStartsWith(context,s.getDialog().getContextSymbol())) {
				dialog = getDialogForContext(context);
				if (dialog!=null && dialog.getId()!=s.getDialog().getId()) {
					s.setDialog(null);
					SvrControllerSessions.getInstance().removeSessionDialogVariablesFromSession(s);
				}
			}

			// Check dialog selection
			if (context.length()>0 && s.getDialog()==null) {
				dialog = getDialogForContext(context);
				if (dialog!=null) {
					s.appendLogLine(DEBUG,"- Selected dialog: " + dialog.getName(),true);
					s.setDialog(dialog);
					context = new StringBuilder(dialog.getContextSymbol());
					redo = true;
				}
			}
			
			if (s.getDialog()!=null) {
				if (redo) {
					controller = validateSessionDialogVariables(s,variables);
					if (controller!=null && controller.getResponse().length()>0) {
						validationResponse = controller.getResponse();
						if (controller.getResponseContext().length()>0) {
							context = controller.getResponseContext();
						}
					}
				}
				if (validationResponse.length()==0) {
					// Check for unknown variables
					for (DialogVariable dv: s.getDialog().getVariables()) {
						StringBuilder value = null;
						for (SessionDialogVariable sv: s.getDialogVariables()) {
							if (sv.getCode().equals(dv.getCode())) {
								value = sv.getValue();
								break;
							}
						}
						if (value==null||value.length()==0) {
							input = new StringBuilder(dv.getPromptInput());
							context = new StringBuilder(dv.getContextSymbol());
							redo = true;
							correctInput = false;
							correctLineEnd = false;
							break;
						}
					}
				}
			}
		}
		
		if (validationResponse.length()>0) {
			output = validationResponse;
		} else if (redo) {
			//Messenger.getInstance().debug(this,"Context: " + context + ", input: " + input);
			outputAndContext = getSessionOutputAndContextForInputAndContext(s,input,context,false,correctInput,correctLineEnd,false);
			output = outputAndContext[0];
		}

		// Replace variable codes with session variable values
		if (output.length()>0) {
			for (SessionDialogVariable sv: s.getDialogVariables()) {
				Generic.stringBuilderReplace(output,sv.getCode(),sv.getValue().toString());
			}
			if (patternManager!=null) {
				output = patternManager.scanAndTranslateOutput(output); 
			}
		} else {
			output = new StringBuilder("?");
		}
		
		if (!context.equals(s.getContext())) {
			s.setContext(context);
			s.appendLogLine(DEBUG,"- Context: " + context,true);
		}
		s.appendLogLine(DEBUG,"> " + output,false);
		
		SvrControllerSessions.getInstance().updateSession(s);
		return output;
	}
	
	private List<String> getExpectedTypesForDialog(Dialog d) {
		List<String> expectedTypes = new ArrayList<String>();
		if (d!=null) {
			for (DialogVariable dv: d.getVariables()) {
				if (!expectedTypes.contains(dv.getType().getName())) {
					expectedTypes.add(dv.getType().getName());
				}
			}
		}
		return expectedTypes;
	}
	
	private StringBuilder getContextForSessionInput(Session s, StringBuilder input, StringBuilder context,List<String> expectedTypes) {
		if (context.length()==0) {
			if (patternManager!=null) {
				input = patternManager.scanAndTranslateInput(input,expectedTypes); 
			}
			StringBuilder[] outputAndContext = getSessionOutputAndContextForInputAndContext(s,input,context,true,true,true,true);
			if (outputAndContext[1].length()>0) {
				List<String> symbols = null;
				if (s.getContext().length()>0 && s.getContext().indexOf(" ")<0) {
					symbols = Symbol.parseTextSymbols(outputAndContext[1],0);
				}
				if (symbols==null || !symbols.contains(s.getContext())) {
					context = outputAndContext[1];
				}
			} else {
				context = s.getContext();
			}
		}
		return context;
	}

	private CtrObject validateSessionDialogVariables(Session s, StringBuilder variables) {
		CtrObject controller = null;
		if (s.getDialog()!=null) {
			boolean updated = updateSessionDialogVariables(s,variables);
			if (updated) {
				if (s.getDialog().getControllerClassName().length()>0) {
					controller = (CtrObject) Generic.instanceForName(s.getDialog().getControllerClassName());
					controller.validateSessionDialogVariables(s,patternManager);
				}
			}
		}
		return controller;
	}
	
	private boolean updateSessionDialogVariables(Session s, StringBuilder variables) {
		boolean updated = false;
		if (s.getDialog()!=null) {
			// Check for variable values
			if (variables.length()>0) {
				List<StringBuilder> objs = Generic.stringBuilderSplit(variables,Generic.SEP_OBJ);
				for (StringBuilder obj: objs) {
					List<StringBuilder> vals = Generic.stringBuilderSplit(obj,Generic.SEP_STR);
					if (vals.size()==2) {
						StringBuilder code = vals.get(1);
						StringBuilder value = vals.get(0);
						updated = updateSessionDialogVariable(s,code,value);
					}
				}
			}
		}
		return updated;
	}
	
	private boolean updateSessionDialogVariable(Session s,StringBuilder code, StringBuilder value) {
		boolean updated = false;
		DialogVariable variable = null;
		for (DialogVariable dv: s.getDialog().getVariables()) {
			if (code.toString().equals(dv.getCode())) {
				variable = dv;
				break;
			}
		} 
		List<PtnObject> patterns = patternManager.getPatternsForValues(value.toString());
		if (patterns.size()>0) {
			List<StringBuilder> ptnVals = Generic.stringBuilderSplit(value,"|");
			int i = 0;
			if (variable!=null) {
				for (PtnObject pattern: patterns) {
					if (pattern.getBaseValueType().equals(variable.getType().getName())) {
						SvrControllerSessions.getInstance().addOrUpdateSessionDialogVariable(s,code.toString(),ptnVals.get(i));
						updated = true;
						break;
					}
					i++;
				}
			}
			if (!updated) {
				i = 0;
				for (PtnObject pattern: patterns) {
					for (DialogVariable dv: s.getDialog().getVariables()) {
						SessionDialogVariable sv = SvrControllerSessions.getInstance().getSessionDialogVariableByCode(s,dv.getCode());
						if ((sv==null || sv.getValue()==null || sv.getValue().length()==0) && pattern.getBaseValueType().equals(dv.getType().getName())) {
							SvrControllerSessions.getInstance().addOrUpdateSessionDialogVariable(s,dv.getCode(),ptnVals.get(i));
							updated = true;
							break;
						}
					} 
					if (updated) {
						break;
					}
					i++;
				}
			}
		}
		return updated;
	}

	private Dialog getDialogForContext(StringBuilder context) {
		List<String> contextSymbols = Symbol.parseTextSymbols(context,0);
		Dialog foundDialog = null;
		for (String symbol: contextSymbols) {
			for (Dialog d: SvrControllerDialogs.getInstance().getDialogsAsList()) {
				if (d.getContextSymbol().equals(symbol)) {
					foundDialog = d;
					break;
				}
			}
			if (foundDialog!=null) {
				break;
			}
		}
		return foundDialog;
	}
	
	private StringBuilder cleanUpText(StringBuilder text) {
		if (text.length()>0) {
			text = Example.cleanUpText(text);
		}
		if (text.length()>0) {
			text = Generic.stringBuilderTrim(text);
		}
		if (text.length()>0) {
			StringBuilder first = new StringBuilder(text.substring(0,1).toUpperCase());
			if (text.length()==1) {
				text = first;
			} else {
				first.append(text.substring(1));
				text = first;
			}
		}
		return text;
	}
}
