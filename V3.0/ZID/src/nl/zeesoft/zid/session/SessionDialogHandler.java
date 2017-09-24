package nl.zeesoft.zid.session;

import java.util.ArrayList;
import java.util.List;

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
import nl.zeesoft.zspr.pattern.patterns.UniversalAlphabetic;

public class SessionDialogHandler extends Locker {
	private static final String				END_INPUT						= "[END_INPUT]";
	private static final String				END_OUTPUT						= "[END_OUTPUT]";

	private List<Dialog>					dialogs							= null;
	private PatternManager					patternManager					= null;

	private Confabulator					contextConfabulator				= null;
	private Confabulator					correctionConfabulator			= null;
	private Confabulator					extensionConfabulator			= null;

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

	public void initialize() {
		lockMe(this);
		contextConfabulator = new Confabulator();
		contextConfabulator.setLog(true);
		correctionConfabulator = new Confabulator();
		correctionConfabulator.setLog(true);
		extensionConfabulator = new Confabulator();
		extensionConfabulator.setLog(true);
		for (Dialog dialog: dialogs) {
			ZStringSymbolParser dialogContext = new ZStringSymbolParser();
			dialogContext.append(dialog.getName());
			dialogContext.append(" ");
			dialogContext.append(dialog.getLanguage().getCode());
			for (DialogExample example: dialog.getExamples()) {
				ZStringSymbolParser sequence = new ZStringSymbolParser();
				sequence.append(example.getOutput());
				sequence.append(" ");
				sequence.append(example.getInput());
				sequence.removeLineEndSymbols();
				sequence.removePunctuationSymbols();
				contextConfabulator.learnSequence(getSafeText(sequence),dialogContext);

				correctionConfabulator.learnSequence(getSafeText(example.getInput()),dialogContext);

				sequence = new ZStringSymbolParser();
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
		// TODO: externalize using getAllSequenceSymbols
		/*
		UniversalAlphabetic pattern = (UniversalAlphabetic) patternManager.getPatternByClassName(UniversalAlphabetic.class.getName());
		if (pattern!=null) {
			contextConfabulator.confabulate(new ContextConfabulation());
			correctionConfabulator.confabulate(new ContextConfabulation());
			extensionConfabulator.confabulate(new ContextConfabulation());
			pattern.setKnownSymbols(contextConfabulator.getAllSequenceSymbols());
			pattern.addKnownSymbols(correctionConfabulator.getAllSequenceSymbols());
			pattern.addKnownSymbols(extensionConfabulator.getAllSequenceSymbols());
		}
		*/
		unlockMe(this);
	}
	
	public List<String> getAllSequenceSymbols() {
		List<String> r = new ArrayList<String>();
		lockMe(this);
		contextConfabulator.confabulate(new ContextConfabulation());
		correctionConfabulator.confabulate(new ContextConfabulation());
		extensionConfabulator.confabulate(new ContextConfabulation());
		for (String symbol: contextConfabulator.getAllSequenceSymbols()) {
			if (!r.contains(symbol)) {
				r.add(symbol);
			}
		}
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
	
	public void handleSessionInput(Session session) {
		lockMe(this);
		ZStringSymbolParser prevOutput = session.getOutput();
		
		session.setOutput(new ZStringSymbolParser());

		// Correct input
		ZStringSymbolParser input = getSafeText(session.getInput());
		session.addLogLine("<<< " + input);

		// Determine context
		ZStringSymbolParser sequence = new ZStringSymbolParser(input);
		List<String> contextSymbols = confabulateContext(sequence);
		if (contextSymbols.size()==0) {
			lockMe(this);
			if (prevOutput.length()>0) {
				sequence.insert(0," ");
				sequence.insert(0,prevOutput);
			}
			unlockMe(this);
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
				session.addLogLine("--- Selected dialog: " + session.getDialog().getName() + " (controller: " + session.getDialog().getControllerClassName() + ")");
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
		if (!changedDialog && session.getPromptForDialogVariable().length()>0) {
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
		lockMe(this);
		contextConfabulator.confabulate(confab);
		unlockMe(this);
		return confab.getOutput().toSymbols();
	}

	protected CorrectionConfabulation confabulateCorrection(ZStringSymbolParser sequence,ZStringSymbolParser context) {
		CorrectionConfabulation confab = new CorrectionConfabulation();
		confab.setSequence(sequence);
		if (context!=null && context.length()>0) {
			confab.setContext(context);
		}
		lockMe(this);
		correctionConfabulator.confabulate(confab);
		unlockMe(this);
		return confab;
	}

	protected List<String> confabulateExtension(ZStringSymbolParser sequence,String context) {
		ExtensionConfabulation confab = new ExtensionConfabulation();
		confab.setSequence(sequence);
		confab.setForceMaxDepth(true);
		if (context!=null && context.length()>0) {
			confab.setContext(new ZStringSymbolParser(context));
		}
		lockMe(this);
		extensionConfabulator.confabulate(confab);
		unlockMe(this);
		return confab.getOutput().toSymbols();
	}

	protected final Dialog getDialogNoLock(String name, String languageCode) {
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
