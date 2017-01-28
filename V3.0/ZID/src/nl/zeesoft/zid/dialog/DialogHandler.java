package nl.zeesoft.zid.dialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.SymbolParser;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zsc.Generic;
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

	private Confabulator					contextConfabulator				= new Confabulator();
	private SortedMap<String,Confabulator>	dialogConfabulators				= new TreeMap<String,Confabulator>();
	private SortedMap<String,Confabulator>	dialogVariableConfabulators		= new TreeMap<String,Confabulator>();

	private StringBuilder					log								= new StringBuilder();
	private SortedMap<String,String>		variables						= new TreeMap<String,String>(); 
	private StringBuilder					prevOutput						= new StringBuilder();
	private Dialog							dialog							= null;
	private SortedMap<String,String>		dialogVariables					= new TreeMap<String,String>();
	
	public DialogHandler(List<Dialog> dialogs, PatternManager patternManager) {
		this.dialogs = dialogs;
		this.patternManager = patternManager;
	}
	
	public void initialize() {
		lockMe(this);
		contextConfabulator = new Confabulator();
		contextConfabulator.setLog(true);
		for (Dialog dialog: dialogs) {
			for (DialogExample example: dialog.getExamples()) {
				StringBuilder sequence = new StringBuilder();
				sequence.append(example.getInput());
				sequence.append(" ");
				sequence.append(END_INPUT);
				sequence.append(" ");
				sequence.append(example.getOutput());
				sequence.append(" ");
				sequence.append(END_OUTPUT);
				contextConfabulator.learnSequence(getSafeText(sequence),new StringBuilder(dialog.getName()));
			}
			for (DialogVariable variable: dialog.getVariables()) {
				for (DialogVariableExample example: variable.getExamples()) {
					StringBuilder sequence = new StringBuilder();
					sequence.append(example.getQuestion());
					sequence.append(" ");
					sequence.append(example.getAnswer());
					contextConfabulator.learnSequence(getSafeText(sequence),new StringBuilder(dialog.getName()));
				}
			}
		}
		UniversalAlphabetic pattern = (UniversalAlphabetic) patternManager.getPatternByClassName(UniversalAlphabetic.class.getName());
		if (pattern!=null) {
			contextConfabulator.confabulate(new ContextConfabulation());
			pattern.setKnownSymbols(contextConfabulator.getAllSequenceSymbols());
		}
		unlockMe(this);
	}
	
	public StringBuilder processInput(StringBuilder input) {
		StringBuilder output = new StringBuilder();
		
		// Correct input
		input = getSafeText(input);
		addLogLine("<<< " + input);
		
		// Determine context
		String context = "";
		List<String> contextSymbols = confabulateContext(input);
		if (prevOutput.length()>0) {
			input.insert(0," ");
			input.insert(0,prevOutput);
			if (contextSymbols.size()==0) {
				contextSymbols = confabulateContext(input);
			}
		}
		
		boolean selectedDialog = false;
		List<String> expectedTypes = null;
		
		lockMe(this);
		if (contextSymbols.size()==0 && dialog!=null) {
			context = dialog.getName();
		} else {
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
			expectedTypes = new ArrayList<String>();
			for (DialogVariable variable: dialog.getVariables()) {
				if (!expectedTypes.contains(variable.getType())) {
					expectedTypes.add(variable.getType());
				}
			}
		}
		unlockMe(this);
				
		if (context.length()>0) {
			if (selectedDialog) {
				addLogLine("--- Selected dialog: " + context);
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
		CorrectionConfabulation correction = confabulateCorrection(input,context);
		input = stringBuilderFromSymbols(SymbolParser.parseSymbols(correction.getOutput()));
		addLogLine("--- Corrected input: " + input);
		
		// Update variables
		List<DialogVariable> updatedVariables = new ArrayList<DialogVariable>(); 
		if (correction.getCorrectionValues().size()>0) {
			lockMe(this);
			for (DialogVariable variable: dialog.getVariables()) {
				String macro = "{" + variable.getName() + "}";
				if (correction.getCorrectionValues().contains(macro)) {
					String value = correction.getCorrectionKeys().get(correction.getCorrectionValues().indexOf(macro));
					PatternObject pattern = getPatternForDialogVariableValue(variable, value);
					if (pattern!=null) {
						dialogVariables.put(variable.getName(),value);
						updatedVariables.add(variable);
					}
				}
			}
			unlockMe(this);
		}

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
			// TODO: pass updated variables to dialog handler
		}
		
		if (input.length()>0) {
			input.append(" ");
		}
		input.append(END_INPUT);
		
		// Extend input to output
		List<String> extensionSymbols = confabulateExtension(input,context);
		for (String symbol: extensionSymbols) {
			if (symbol.equals(END_INPUT)) {
				output = new StringBuilder();
			} else if (symbol.equals(END_OUTPUT)) {
				break;
			}
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
				}
			}
			// TODO: Detect and translate variables
			if (output.length()>0 && !SymbolParser.isLineEndSymbol(symbol)) {
				output.append(" ");
			}
			output.append(symbol);
		}
		
		if (output.length()>0) {
			addLogLine("--- Extended input to output: " + output);
			output = patternManager.scanAndTranslateSequence(output, expectedTypes);
			addLogLine("--- Translated output: " + output);
		} else {
			addLogLine("--- Failed to extend input");
		}
		
		output = getSafeText(output);
		addLogLine(">>> " + output);
		prevOutput = new StringBuilder(output);
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
	
	protected final void setVariable(String name,String value) {
		lockMe(this);
		variables.put(name,value);
		unlockMe(this);
	}
	
	protected final String getVariable(String name) {
		String r = "";
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
		log.append(Generic.getDateTimeString(new Date()));
		log.append(": ");
		log.append(line);
		log.append("\n");
		unlockMe(this);
	}

	private final void setDialogNoLock(String name) {
		Dialog newDialog = getDialogNoLock(name);
		if (newDialog!=null && dialog!=newDialog) {
			dialog = newDialog;
			dialogVariables.clear();
			if (newDialog!=null) {
				for (DialogVariable dv: newDialog.getVariables()) {
					dialogVariables.put(dv.getName(),"");
				}
			}
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

	private final List<String> confabulateContext(StringBuilder sequence) {
		ContextConfabulation confab = new ContextConfabulation();
		confab.setSequence(sequence);
		lockMe(this);
		contextConfabulator.confabulate(confab);
		unlockMe(this);
		return SymbolParser.parseSymbols(confab.getOutput());
	}

	private final CorrectionConfabulation confabulateCorrection(StringBuilder sequence,String context) {
		CorrectionConfabulation confab = new CorrectionConfabulation();
		confab.setSequence(sequence);
		if (context!=null && context.length()>0) {
			confab.setContext(new StringBuilder(context));
		}
		lockMe(this);
		contextConfabulator.confabulate(confab);
		unlockMe(this);
		return confab;
	}

	private final List<String> confabulateExtension(StringBuilder sequence,String context) {
		ExtensionConfabulation confab = new ExtensionConfabulation();
		confab.setSequence(sequence);
		if (context!=null && context.length()>0) {
			confab.setContext(new StringBuilder(context));
		}
		lockMe(this);
		contextConfabulator.confabulate(confab);
		unlockMe(this);
		return SymbolParser.parseSymbols(confab.getOutput());
	}
	
	private final String getPatternStringForDialogVariableValue(DialogVariable variable,String value) {
		String r = "";
		PatternObject pattern = getPatternForDialogVariableValue(variable,value);
		if (pattern!=null) {
			r = pattern.getStringForValue(value);
		}
		return r;
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
	
	private final static StringBuilder getSafeText(StringBuilder text) {
		text = Generic.stringBuilderTrim(text);
		text.replace(0,1,text.substring(0,1).toUpperCase());
		if (!SymbolParser.endsWithLineEndSymbol(text)) {
			text.append(".");
		}
		return stringBuilderFromSymbols(SymbolParser.parseSymbolsFromText(text));
	}
	
	private final static StringBuilder stringBuilderFromSymbols(List<String> symbols) {
		StringBuilder r = new StringBuilder();
		for (String symbol: symbols) {
			if (r.length()>0 && !SymbolParser.isLineEndSymbol(symbol)) {
				r.append(" ");
			}
			r.append(symbol);
		}
		return r;
	}
}
