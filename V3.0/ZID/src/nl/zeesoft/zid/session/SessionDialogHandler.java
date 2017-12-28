package nl.zeesoft.zid.session;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogExample;
import nl.zeesoft.zid.dialog.DialogVariable;
import nl.zeesoft.zid.dialog.DialogVariableExample;
import nl.zeesoft.zsc.confabulator.Confabulator;
import nl.zeesoft.zsc.confabulator.confabulations.ContextConfabulation;
import nl.zeesoft.zsc.confabulator.confabulations.CorrectionConfabulation;
import nl.zeesoft.zsc.confabulator.confabulations.ExtensionConfabulation;
import nl.zeesoft.zspr.Language;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObject;
import nl.zeesoft.zspr.pattern.patterns.UniversalAlphabetic;

/**
 * Session dialog handlers are used to handle session input.
 */
public class SessionDialogHandler extends Locker {
	private static final String				END_INPUT						= "[END_INPUT]";
	private static final String				END_OUTPUT						= "[END_OUTPUT]";

	private List<Dialog>					dialogs							= null;
	private PatternManager					patternManager					= null;

	private Confabulator					correctionConfabulator			= null;
	private Confabulator					extensionConfabulator			= null;

	private int								maxOutputSymbols				= 32;
	
	public SessionDialogHandler(List<Dialog> dialogs, PatternManager patternManager) {
		super(null);
		this.dialogs = dialogs;
		this.patternManager = patternManager;
	}
	
	public SessionDialogHandler(Messenger msgr, List<Dialog> dialogs, PatternManager patternManager) {
		super(msgr);
		this.dialogs = dialogs;
		this.patternManager = patternManager;
	}

	public int getMaxOutputSymbols() {
		return maxOutputSymbols;
	}

	public void setMaxOutputSymbols(int maxOutputSymbols) {
		this.maxOutputSymbols = maxOutputSymbols;
	}

	/**
	 * Initializes the three confabulators and pattern manager.
	 */
	public void initialize() {
		initialize(true);
	}

	/**
	 * Initializes the three confabulators and optionally the pattern manager.
	 * 
	 * @param updatePatternManager True if the pattern manager is to be updated
	 */
	public void initialize(boolean updatePatternManager) {
		lockMe(this);
		correctionConfabulator = new Confabulator(getMessenger());
		correctionConfabulator.setLog(true);
		extensionConfabulator = new Confabulator(getMessenger());
		extensionConfabulator.setLog(true);
		for (Dialog dialog: dialogs) {
			ZStringSymbolParser dialogContext = new ZStringSymbolParser();
			dialogContext.append(dialog.getName());
			dialogContext.append(" ");
			dialogContext.append(dialog.getLanguage().getCode());
			for (DialogExample example: dialog.getExamples()) {
				correctionConfabulator.learnSequence(getSafeText(example.getInput()),dialogContext);

				ZStringSymbolParser sequence = new ZStringSymbolParser();
				sequence.append(example.getInput());
				sequence.append(" ");
				sequence.append(END_INPUT);
				sequence.append(" ");
				sequence.append(example.getOutput());
				sequence.append(" ");
				sequence.append(END_OUTPUT);
				extensionConfabulator.learnSequence(getSafeText(sequence),dialogContext);
			}
			for (DialogVariable variable: dialog.getVariables()) {
				for (DialogVariableExample example: variable.getExamples()) {
					ZStringSymbolParser sequence = new ZStringSymbolParser();
					sequence.append(example.getQuestion());
					sequence.append(" ");
					sequence.append(example.getAnswer());
					ZStringSymbolParser variableContext = new ZStringSymbolParser();
					variableContext.append(dialog.getName());
					variableContext.append(" ");
					variableContext.append(dialog.getName());
					variableContext.append(":");
					variableContext.append(variable.getName());
					variableContext.append(" ");
					variableContext.append(dialog.getLanguage().getCode());
					correctionConfabulator.learnSequence(getSafeText(sequence),variableContext);
				}
			}
		}
		unlockMe(this);
		if (updatePatternManager) {
			UniversalAlphabetic pattern = (UniversalAlphabetic) patternManager.getPatternByClassName(UniversalAlphabetic.class.getName());
			if (pattern!=null) {
				pattern.setKnownSymbols(getAllSequenceSymbols());
			}
		}
	}

	/**
	 * Returns a list of all symbols in all three confabulators.
	 * 
	 * @return A list of all symbols in all three confabulators
	 */
	public List<String> getAllSequenceSymbols() {
		List<String> r = new ArrayList<String>();
		lockMe(this);
		correctionConfabulator.confabulate(new ContextConfabulation());
		extensionConfabulator.confabulate(new ContextConfabulation());
		for (String symbol: correctionConfabulator.getAllSequenceSymbols()) {
			if (!r.contains(symbol)) {
				r.add(symbol);
			}
		}
		for (String symbol: extensionConfabulator.getAllSequenceSymbols()) {
			if (!r.contains(symbol)) {
				r.add(symbol);
			}
		}
		unlockMe(this);
		return r;
	}
	
	/**
	 * Handles session input.
	 * Updates the session lastActivity, output and log.
	 * 
	 * @param session The session
	 */
	public void handleSessionInput(Session session) {
		lockMe(this);
		ZStringSymbolParser prevOutput = session.getOutput();
		
		session.setLastActivity(new ZDate());
		session.setPatternManager(patternManager);
		session.setOutput(new ZStringSymbolParser());

		// Correct input
		ZStringSymbolParser input = new ZStringSymbolParser(session.getInput().trim());
		if (!ZStringSymbolParser.endsWithLineEndSymbol(input)) {
			List<String> extensionSymbols = confabulateExtension(input,"",1);
			for (String symbol: extensionSymbols) {
				if (ZStringSymbolParser.isLineEndSymbol(symbol)) {
					input.append(symbol);
					break;
				}
			}
		}
		input = getSafeText(input);
		session.addLogLine("<<< " + input);

		// Determine context
		ZStringSymbolParser sequence = new ZStringSymbolParser(input);
		List<String> contextSymbols = confabulateContext(sequence);
		if (contextSymbols.size()==0) {
			if (prevOutput.length()>0) {
				sequence.insert(0," ");
				sequence.insert(0,prevOutput);
			}
			contextSymbols = confabulateContext(sequence);
		}

		// Determine language
		Language inputLanguage = null;
		for (String symbol: contextSymbols) {
			inputLanguage = Language.getLanguage(symbol);
			if (inputLanguage!=null) {
				break;
			}
		}
		String inputLanguageCode = "";
		if (inputLanguage!=null) {
			inputLanguageCode = inputLanguage.getCode();
		}
		
		// Determine dialog
		boolean changedDialog = false;
		if (session.getDialog()!=null) {
			// Check if input matches dialog
			if (!session.getDialog().isLanguageCode(inputLanguageCode)) {
				session.clearDialog();
				changedDialog = true;
			}
		}
		if (session.getDialog()==null) {
			for (String symbol: contextSymbols) {
				Dialog dialog = this.getDialogNoLock(symbol,inputLanguageCode);
				if (dialog!=null) {
					session.initializeDialog(dialog);
					changedDialog = true;
					break;
				}
			}
		}
		
		String context = "";
		List<String> expectedTypes = null;
		if (session.getDialog()!=null) {
			context = session.getDialog().getName() + " " + session.getDialog().getLanguage().getCode();
			expectedTypes = session.getDialog().getExpectedTypes();
		}
		
		if (context.length()>0) {
			if (changedDialog) {
				if (session.getDialog().getControllerClassName().length()>0) {
					session.addLogLine("--- Selected dialog: " + session.getDialog().getName() + " (controller: " + session.getDialog().getControllerClassName() + ")");
				} else {
					session.addLogLine("--- Selected dialog: " + session.getDialog().getName());
				}
			} else {
				session.addLogLine("--- Continuing dialog: " + session.getDialog().getName());
			}
		} else {
			session.addLogLine("--- Unable to determine dialog");
		}
		
		// Translate input
		input = patternManager.translateSequence(input,expectedTypes,null);
		session.addLogLine("--- Translated input: " + input);
		
		// Correct input
		sequence = new ZStringSymbolParser(input);
		CorrectionConfabulation correction = correctInput(sequence,session.getDialog(),session.getPromptForDialogVariable());
		if (!changedDialog) {
			if (prevOutput.length()>0) {
				sequence.insert(0," ");
				sequence.insert(0,prevOutput);
			}
			CorrectionConfabulation correction2 = correctInput(sequence,session.getDialog(),session.getPromptForDialogVariable());
			if (correction2.getCorrectionKeys().size()>=correction.getCorrectionKeys().size()) {
				correction = correction2;
			}
		}
		input.fromSymbols(correction.getOutput().toSymbols(),true,true);
		session.addLogLine("--- Corrected input: " + input);

		// Update variables
		List<DialogVariable> updatedVariables = new ArrayList<DialogVariable>(); 
		if (session.getDialog()!=null && session.getDialogController()!=null && correction.getCorrectionValues().size()>0) {
			List<String> correctedKeys = new ArrayList<String>();
			for (DialogVariable variable: session.getDialog().getVariables()) {
				String macro = "{" + variable.getName() + "}";
				if (correction.getCorrectionValues().contains(macro)) {
					String value = correction.getCorrectionKeys().get(correction.getCorrectionValues().indexOf(macro));
					correctedKeys.add(value);
					PatternObject pattern = session.getPatternForDialogVariableValue(variable, value);
					if (pattern!=null) {
						if (value.contains(patternManager.getOrConcatenator())) {
							for (String val: value.split("\\" + patternManager.getOrConcatenator())) {
								if (val.startsWith(pattern.getValuePrefix())) {
									value = val;
									break;
								}
							}
						}
						session.getDialogVariables().put(variable.getName(),value);
						updatedVariables.add(variable);
					}
				}
			}
			
			if (updatedVariables.size()<session.getDialogVariables().size()) {
				List<String> values = new ArrayList<String>(correction.getCorrectionKeys());
				for (String value: values) {
					if (value.contains(patternManager.getOrConcatenator()) || !correctedKeys.contains(value)) {
						for (DialogVariable variable: session.getDialog().getVariables()) {
							if (session.getDialogVariables().get(variable.getName()).equals("")) {
								if (value.startsWith(variable.getType())) {
									if (value.contains(patternManager.getOrConcatenator())) {
										for (String val: value.split("\\" + patternManager.getOrConcatenator())) {
											if (val.startsWith(variable.getType())) {
												value = val;
												break;
											}
										}
									}
									session.getDialogVariables().put(variable.getName(),value);
									updatedVariables.add(variable);
									break;
								}
							}
						}
					}
				}
			}
		}

		// Notify controller
		if (updatedVariables.size()>0) {
			StringBuilder variables = new StringBuilder(); 
			for (DialogVariable variable: updatedVariables) {
				if (variables.length()>0) {
					variables.append(", ");
				}
				variables.append(variable.getName());
				variables.append(" = ");
				variables.append(session.getDialogVariables().get(variable.getName()));
			}
			session.addLogLine("--- Updated variables: " + variables);
			ZStringSymbolParser controllerOutput = session.getDialogController().updatedSessionDialogVariables(session);
			List<String> symbols = null;
			if (controllerOutput.length()>0) {
				session.addLogLine("--- Controller output: " + controllerOutput);
				symbols = controllerOutput.toSymbolsPunctuated();
			} else if (session.getPromptForDialogVariable().length()>0) {
				session.addLogLine("--- Controller requests prompt for: " + session.getPromptForDialogVariable());
				DialogVariable variable = session.getDialog().getVariable(session.getPromptForDialogVariable());
				if (variable.getExamples().size()>0) {
					ZIntegerGenerator random = new ZIntegerGenerator(0,(variable.getExamples().size() - 1));
					symbols = variable.getExamples().get(random.getNewInteger()).getQuestion().toSymbols();
				}
			}
			if (symbols!=null) {
				for (String symbol: symbols) {
					symbol = session.translateSymbolToVariableValue(symbol);
					if (session.getOutput().length()>0 && !ZStringSymbolParser.isLineEndSymbol(symbol)) {
						session.getOutput().append(" ");
					}
					session.getOutput().append(symbol);
				}
				session.addLogLine("--- Translated controller output: " + session.getOutput());
			}
			if (session.getDialogController().isCompleted()) {
				session.addLogLine("--- Completed dialog: " + session.getDialog().getName());
				session.clearDialog();
			}
		}
		
		if (session.getOutput().length()==0) {
			// Extend input to output
			if (input.length()>0) {
				input.append(" ");
			}
			input.append(END_INPUT);
			List<String> extensionSymbols = confabulateExtension(input,context,maxOutputSymbols);
			for (String symbol: extensionSymbols) {
				if (symbol.equals(END_INPUT)) {
					session.setOutput(new ZStringSymbolParser());
				} else if (symbol.equals(END_OUTPUT)) {
					break;
				} else {
					symbol = session.translateSymbolToVariableValue(symbol);
					if (symbol.length()>0) {
						if (session.getOutput().length()>0 && !ZStringSymbolParser.isLineEndSymbol(symbol)) {
							session.getOutput().append(" ");
						}
						session.getOutput().append(symbol);
					}
				}
			}
			if (session.getOutput().length()>0) {
				session.addLogLine("--- Extended input to output: " + session.getOutput());
			} else {
				session.addLogLine("--- Failed to extend input");
			}
		}
		
		if (session.getOutput().length()>0) {
			session.setOutput(patternManager.translateValues(session.getOutput()));
			session.addLogLine("--- Translated output: " + session.getOutput());
		} else {
			session.addLogLine("--- Failed to confabulate output");
		}
		
		session.setOutput(getSafeText(session.getOutput(),true));
		session.addLogLine(">>> " + session.getOutput());

		unlockMe(this);
	}
	
	protected final CorrectionConfabulation correctInput(ZStringSymbolParser sequence, Dialog currentDialog,String currentDialogVariable) {
		ZStringSymbolParser context = new ZStringSymbolParser();
		if (currentDialog!=null) {
			context.append(currentDialog.getName());
			context.append(" ");
			context.append(currentDialog.getLanguage().getCode());
			if (currentDialogVariable!=null && currentDialogVariable.length()>0) {
				context.append(" ");
				context.append(currentDialog.getName());
				context.append(":");
				context.append(currentDialogVariable);
			}
		}
		return confabulateCorrection(sequence,context);
	}

	protected List<String> confabulateContext(ZStringSymbolParser sequence) {
		sequence.removeLineEndSymbols();
		sequence.removePunctuationSymbols();
		sequence.append(" .");
		ContextConfabulation confab = new ContextConfabulation();
		confab.setSequence(sequence);
		extensionConfabulator.confabulate(confab);
		return confab.getOutput().toSymbols();
	}

	protected CorrectionConfabulation confabulateCorrection(ZStringSymbolParser sequence,ZStringSymbolParser context) {
		CorrectionConfabulation confab = new CorrectionConfabulation();
		confab.setSequence(sequence);
		if (context!=null && context.length()>0) {
			confab.setContext(context);
		}
		correctionConfabulator.confabulate(confab);
		return confab;
	}
	
	protected List<String> confabulateExtension(ZStringSymbolParser sequence,String context,int maxOutputSymbols) {
		ExtensionConfabulation confab = new ExtensionConfabulation();
		confab.setSequence(sequence);
		confab.setForceMaxDepth(true);
		if (maxOutputSymbols>0) {
			confab.setMaxOutputSymbols(maxOutputSymbols);
		}
		if (context!=null && context.length()>0) {
			confab.setContext(new ZStringSymbolParser(context));
		}
		extensionConfabulator.confabulate(confab);
		return confab.getOutput().toSymbols();
	}

	protected Dialog getDialogNoLock(String name, String languageCode) {
		Dialog r = null;
		for (Dialog dialog: dialogs) {
			if (dialog.getName().equals(name) && (languageCode==null || languageCode.length()==0 || dialog.isLanguageCode(languageCode))) {
				r = dialog;
				break;
			}
		}
		return r;
	}

	protected ZStringSymbolParser getSafeText(ZStringSymbolParser text) {
		return getSafeText(text,false);
	}
	
	protected ZStringSymbolParser getSafeText(ZStringSymbolParser text,boolean correctPunctuation) {
		if (text.length()>0) {
			text.trim();
			if (!ZStringSymbolParser.endsWithLineEndSymbol(text)) {
				text.getStringBuilder().append(".");
			}
			text.fromSymbols(text.toSymbolsPunctuated(),true,correctPunctuation);
		}
		return text;
	}
}
