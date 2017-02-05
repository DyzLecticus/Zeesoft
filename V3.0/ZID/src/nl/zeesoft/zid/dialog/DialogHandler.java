package nl.zeesoft.zid.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zsc.confabulator.Confabulator;
import nl.zeesoft.zsc.confabulator.confabulations.ContextConfabulation;
import nl.zeesoft.zsc.confabulator.confabulations.CorrectionConfabulation;
import nl.zeesoft.zsc.confabulator.confabulations.ExtensionConfabulation;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObject;
import nl.zeesoft.zspr.pattern.patterns.UniversalAlphabetic;

public class DialogHandler extends Locker {
	private static final String				END_INPUT						= "[END_INPUT]";
	private static final String				END_OUTPUT						= "[END_OUTPUT]";

	private List<Dialog>					dialogs							= null;
	private PatternManager					patternManager					= null;

	private Confabulator					contextConfabulator				= null;
	private Confabulator					correctionConfabulator			= null;
	private Confabulator					extensionConfabulator			= null;

	private StringBuilder					log								= new StringBuilder();
	private SortedMap<String,Object>		variables						= new TreeMap<String,Object>(); 
	private ZStringSymbolParser				prevOutput						= new ZStringSymbolParser();

	private Dialog							dialog							= null;
	private DialogControllerObject			dialogController				= null;
	private SortedMap<String,String>		dialogVariables					= new TreeMap<String,String>();
	private String							promptForDialogVariable			= "";

	public DialogHandler(List<Dialog> dialogs, PatternManager patternManager) {
		super(null);
		this.dialogs = dialogs;
		this.patternManager = patternManager;
	}
	
	public DialogHandler(Messenger msgr, List<Dialog> dialogs, PatternManager patternManager) {
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
			for (DialogExample example: dialog.getExamples()) {
				ZStringSymbolParser sequence = new ZStringSymbolParser();
				sequence.append(example.getOutput());
				sequence.append(" ");
				sequence.append(example.getInput());
				contextConfabulator.learnSequence(getSafeText(sequence),new ZStringSymbolParser(dialog.getName()));

				correctionConfabulator.learnSequence(getSafeText(example.getInput()),new ZStringSymbolParser(dialog.getName()));

				sequence = new ZStringSymbolParser();
				sequence.append(example.getInput());
				sequence.append(" ");
				sequence.append(END_INPUT);
				sequence.append(" ");
				sequence.append(example.getOutput());
				sequence.append(" ");
				sequence.append(END_OUTPUT);
				extensionConfabulator.learnSequence(getSafeText(sequence),new ZStringSymbolParser(dialog.getName()));
			}
			for (DialogVariable variable: dialog.getVariables()) {
				for (DialogVariableExample example: variable.getExamples()) {
					ZStringSymbolParser sequence = new ZStringSymbolParser();
					sequence.append(example.getQuestion());
					sequence.append(" ");
					sequence.append(example.getAnswer());
					ZStringSymbolParser context = new ZStringSymbolParser();
					context.append(dialog.getName());
					context.append(" ");
					context.append(dialog.getName());
					context.append(":");
					context.append(variable.getName());
					correctionConfabulator.learnSequence(getSafeText(sequence),context);
				}
			}
		}
		UniversalAlphabetic pattern = (UniversalAlphabetic) patternManager.getPatternByClassName(UniversalAlphabetic.class.getName());
		if (pattern!=null) {
			contextConfabulator.confabulate(new ContextConfabulation());
			correctionConfabulator.confabulate(new ContextConfabulation());
			extensionConfabulator.confabulate(new ContextConfabulation());
			pattern.setKnownSymbols(contextConfabulator.getAllSequenceSymbols());
			pattern.addKnownSymbols(correctionConfabulator.getAllSequenceSymbols());
			pattern.addKnownSymbols(extensionConfabulator.getAllSequenceSymbols());
		}
		unlockMe(this);
	}

	public ZStringSymbolParser processInput(ZStringSymbolParser input) {
		ZStringSymbolParser output = processInputDefault(input);
		boolean retry = false;
		lockMe(this);
		if (dialog!=null && prevOutput.length()>0 && output.equals(prevOutput)) {
			setDialogNoLock(null);
			retry = true;
		}
		unlockMe(this);
		if (retry) {
			output = processInputDefault(input);
		}
		lockMe(this);
		prevOutput = new ZStringSymbolParser(output);
		unlockMe(this);
		return output;
	}

	protected ZStringSymbolParser processInputDefault(ZStringSymbolParser input) {
		ZStringSymbolParser output = new ZStringSymbolParser();
		
		Dialog currentDialog = null;
		DialogControllerObject currentDialogController = null;
		String currentDialogVariable = "";
		
		lockMe(this);
		currentDialog = dialog;
		currentDialogController = dialogController;
		currentDialogVariable = promptForDialogVariable;
		unlockMe(this);
		
		// Correct input
		input = getSafeText(input);
		addLogLine("<<< " + input);

		// Determine context
		String context = "";
		boolean selectedDialog = false;
		List<String> expectedTypes = null;
		if (currentDialogVariable.length()>0) {
			context = currentDialog.getName();
			expectedTypes = currentDialog.getExpectedTypes();
		} else {
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
			
			// Determine dialog
			lockMe(this);
			if (contextSymbols.size()==0 && dialog!=null) {
				context = dialog.getName();
			} else if (contextSymbols.size()>0) {
				context = contextSymbols.get(0);
				if (dialog==null || !dialog.getName().equals(context)) {
					setDialogNoLock(context);
					if (dialog==null) {
						context = "";
					} else {
						selectedDialog = true;
					}
				}
			}
			if (dialog!=null) {
				currentDialog = dialog;
				currentDialogController = dialogController;
				currentDialogVariable = "";
				expectedTypes = dialog.getExpectedTypes();
				for (DialogVariable variable: dialog.getVariables()) {
					if (currentDialogVariable.length()==0) {
						promptForDialogVariable = variable.getName();
						currentDialogVariable = promptForDialogVariable;
						break;
					}
				}
			} else {
				currentDialog = null;
				currentDialogController = null;
				currentDialogVariable = "";
			}
			unlockMe(this);
		}
		if (context.length()>0) {
			if (selectedDialog) {
				addLogLine("--- Selected dialog: " + currentDialog.getName() + " (controller: " + currentDialog.getControllerClassName() + ")");
			} else {
				addLogLine("--- Continuing dialog: " + context);
			}
		} else {
			addLogLine("--- Unable to determine dialog");
		}
		
		// Translate input
		input = patternManager.scanAndTranslateSequence(input, expectedTypes);
		addLogLine("--- Translated input: " + input);

		// Correct input
		CorrectionConfabulation correction = correctInput(input,currentDialog,currentDialogVariable);
		input.fromSymbols(correction.getOutput().toSymbols(),true,true);
		addLogLine("--- Corrected input: " + input);
		
		// Update variables
		List<DialogVariable> updatedVariables = new ArrayList<DialogVariable>(); 
		if (currentDialog!=null && currentDialogController!=null && correction.getCorrectionValues().size()>0) {
			lockMe(this);
			
			List<String> values = new ArrayList<String>(correction.getCorrectionKeys());
			int i = 0;
			for (String value: values) {
				for (DialogVariable variable: dialog.getVariables()) {
					if (dialogVariables.get(variable.getName()).equals("")) {
						if (value.startsWith(variable.getType())) {
							if (value.contains(patternManager.getOrConcatenator())) {
								for (String val: value.split("\\" + patternManager.getOrConcatenator())) {
									if (val.startsWith(variable.getType())) {
										value = val;
										break;
									}
								}
							}
							dialogVariables.put(variable.getName(),value);
							updatedVariables.add(variable);
							correction.getCorrectionKeys().remove(i);
							correction.getCorrectionValues().remove(i);
							i--;
							break;
						}
					}
				}
				i++;
			}
			
			for (DialogVariable variable: dialog.getVariables()) {
				String macro = "{" + variable.getName() + "}";
				if (correction.getCorrectionValues().contains(macro)) {
					String value = correction.getCorrectionKeys().get(correction.getCorrectionValues().indexOf(macro));
					PatternObject pattern = getPatternForDialogVariableValue(variable, value);
					if (pattern!=null) {
						if (value.contains(patternManager.getOrConcatenator())) {
							for (String val: value.split("\\" + patternManager.getOrConcatenator())) {
								if (val.startsWith(pattern.getValuePrefix())) {
									value = val;
									break;
								}
							}
						}
						dialogVariables.put(variable.getName(),value);
						updatedVariables.add(variable);
					}
				}
			}
			unlockMe(this);
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
				variables.append(getDialogVariable(variable.getName()));
			}
			addLogLine("--- Updated variables: " + variables);
			ZStringSymbolParser controllerOutput = currentDialogController.updatedDialogVariables(this,currentDialog);
			List<String> symbols = null;
			if (controllerOutput.length()>0) {
				addLogLine("--- Controller output: " + controllerOutput);
				symbols = controllerOutput.toSymbolsPunctuated();
			} else if (currentDialogController.getPromptForDialogVariable().length()>0) {
				addLogLine("--- Controller requests prompt for: " + currentDialogController.getPromptForDialogVariable());
				DialogVariable variable = currentDialog.getVariable(currentDialogController.getPromptForDialogVariable());
				if (variable.getExamples().size()>0) {
					ZIntegerGenerator random = new ZIntegerGenerator(0,(variable.getExamples().size() - 1));
					symbols = variable.getExamples().get(random.getNewInteger()).getQuestion().toSymbols();
				}
			}
			if (symbols!=null) {
				for (String symbol: symbols) {
					symbol = translateSymbolToVariableValue(symbol);
					if (output.length()>0 && !ZStringSymbolParser.isLineEndSymbol(symbol)) {
						output.append(" ");
					}
					output.append(symbol);
				}
				addLogLine("--- Translated controller output: " + output);
			}
			if (currentDialogController.isCompleted()) {
				lockMe(this);
				setDialogNoLock(null);
				currentDialog = dialog;
				currentDialogController = dialogController;
				currentDialogVariable = promptForDialogVariable;
				unlockMe(this);
			} else if (currentDialogController.getPromptForDialogVariable().length()>0) {
				lockMe(this);
				promptForDialogVariable = currentDialogController.getPromptForDialogVariable(); 			
				currentDialogVariable = promptForDialogVariable;
				unlockMe(this);
			}
		}
		
		if (output.length()==0) {
			// Extend input to output
			if (input.length()>0) {
				input.append(" ");
			}
			input.append(END_INPUT);
			List<String> extensionSymbols = confabulateExtension(input,context);
			for (String symbol: extensionSymbols) {
				if (symbol.equals(END_INPUT)) {
					output = new ZStringSymbolParser();
				} else if (symbol.equals(END_OUTPUT)) {
					break;
				} else {
					symbol = translateSymbolToVariableValue(symbol);
					if (symbol.length()>0) {
						if (output.length()>0 && !ZStringSymbolParser.isLineEndSymbol(symbol)) {
							output.append(" ");
						}
						output.append(symbol);
					}
				}
			}
			if (output.length()>0) {
				addLogLine("--- Extended input to output: " + output);
			} else {
				addLogLine("--- Failed to extend input");
			}
		}
		
		if (output.length()>0) {
			output = patternManager.scanAndTranslateValues(output);
			addLogLine("--- Translated output: " + output);
		} else {
			addLogLine("--- Failed to confabulate output");
		}
		
		output = getSafeText(output,true);
		addLogLine(">>> " + output);
		return output;
	}

	public final StringBuilder getLog() {
		StringBuilder r = null;
		lockMe(this);
		r = new StringBuilder(log);
		unlockMe(this);
		return r;
	}
	
	public final void clearLog() {
		lockMe(this);
		log = new StringBuilder();
		unlockMe(this);
	}
	
	protected final void setVariable(String name,Object value) {
		lockMe(this);
		variables.put(name,value);
		unlockMe(this);
	}
	
	protected final Object getVariable(String name) {
		Object r = "";
		lockMe(this);
		if (variables.containsKey(name)) {
			r = variables.get(name);
		}
		unlockMe(this);
		return r;
	}

	protected final void removeVariable(String name) {
		lockMe(this);
		if (variables.containsKey(name)) {
			variables.remove(name);
		}
		unlockMe(this);
	}

	protected final void clearVariables() {
		lockMe(this);
		variables.clear();
		unlockMe(this);
	}
	
	protected final void setDialogVariable(String name,String value) {
		lockMe(this);
		dialogVariables.put(name,value);
		unlockMe(this);
	}
	
	protected final String getDialogVariable(String name) {
		String r = "";
		lockMe(this);
		if (dialogVariables.containsKey(name)) {
			r = dialogVariables.get(name);
		}
		unlockMe(this);
		return r;
	}

	protected final void removeDialogVariable(String name) {
		lockMe(this);
		if (dialogVariables.containsKey(name)) {
			dialogVariables.remove(name);
		}
		unlockMe(this);
	}

	protected final void clearDialogVariables() {
		lockMe(this);
		dialogVariables.clear();
		unlockMe(this);
	}

	protected final void addLogLine(String line) {
		lockMe(this);
		log.append((new ZDate()).getDateTimeString());
		log.append(": ");
		log.append(line);
		log.append("\n");
		unlockMe(this);
	}

	protected final String getPatternStringForDialogVariableValue(DialogVariable variable,String value) {
		String r = "";
		PatternObject pattern = getPatternForDialogVariableValue(variable,value);
		if (pattern!=null) {
			r = pattern.getStringForValue(value);
		}
		return r;
	}
	
	protected CorrectionConfabulation correctInput(ZStringSymbolParser sequence, Dialog currentDialog,String currentDialogVariable) {
		ZStringSymbolParser context = new ZStringSymbolParser();
		if (currentDialog!=null) {
			context.append(currentDialog.getName());
			if (currentDialogVariable!=null && currentDialogVariable.length()>0) {
				context.append(" ");
				context.append(currentDialog.getName());
				context.append(":");
				context.append(currentDialogVariable);
			}
		}
		return confabulateCorrection(sequence,context);
	}

	private final void setDialogNoLock(String name) {
		Dialog newDialog = getDialogNoLock(name);
		if (newDialog!=null && dialog!=newDialog) {
			dialog = newDialog;
			dialogController = dialog.getNewController();
			dialogVariables.clear();
			promptForDialogVariable = "";
			if (newDialog!=null) {
				for (DialogVariable dv: newDialog.getVariables()) {
					dialogVariables.put(dv.getName(),"");
				}
			}
		} else if (newDialog==null) {
			dialog = null;
			dialogController = null;
			dialogVariables.clear();
			promptForDialogVariable = "";
		}
	}

	private Dialog getDialogNoLock(String name) {
		Dialog r = null;
		for (Dialog dialog: dialogs) {
			if (dialog.getName().equals(name)) {
				r = dialog;
				break;
			}
		}
		return r;
	}

	private final String translateSymbolToVariableValue(String symbol) {
		if (symbol.startsWith("{") && symbol.endsWith("}") && symbol.length()>2) {
			String name = symbol.substring(1,(symbol.length()-1));
			DialogVariable variable = null;
			String value = "";
			lockMe(this);
			if (dialog!=null) {
				variable = dialog.getVariable(name);
				value = dialogVariables.get(name);
			}
			unlockMe(this);
			if (variable!=null && value.length()>0) {
				symbol = getPatternStringForDialogVariableValue(variable, value);
			} else {
				Object obj = getVariable(name);
				if (obj!=null) {
					symbol = obj.toString();
				}
			}
		}
		return symbol;
	}
	
	private final List<String> confabulateContext(ZStringSymbolParser sequence) {
		ContextConfabulation confab = new ContextConfabulation();
		confab.setSequence(sequence);
		lockMe(this);
		contextConfabulator.confabulate(confab);
		unlockMe(this);
		return confab.getOutput().toSymbols();
	}

	private final CorrectionConfabulation confabulateCorrection(ZStringSymbolParser sequence,ZStringSymbolParser context) {
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

	private final List<String> confabulateExtension(ZStringSymbolParser sequence,String context) {
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
	
	private final PatternObject getPatternForDialogVariableValue(DialogVariable variable,String value) {
		PatternObject r = null;
		List<PatternObject> patterns = patternManager.getPatternsForValues(value);
		for (PatternObject pattern: patterns) {
			if (pattern.getBaseValueType().equals(variable.getType())) {
				r = pattern;
				break;
			}
		}
		return r;
	}

	private final static ZStringSymbolParser getSafeText(ZStringSymbolParser text) {
		return getSafeText(text,false);
	}
	
	private final static ZStringSymbolParser getSafeText(ZStringSymbolParser text,boolean correctPunctuation) {
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
