package nl.zeesoft.zid.dialog;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.SymbolParser;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zsc.Generic;
import nl.zeesoft.zsc.confabulator.Confabulator;
import nl.zeesoft.zsc.confabulator.confabulations.ContextConfabulation;
import nl.zeesoft.zsc.confabulator.confabulations.ExtensionConfabulation;
import nl.zeesoft.zspr.pattern.PatternManager;

public class DialogHandler extends Locker {
	private static final String				SELECTED_DIALOG_OUTPUT			= "[SELECTED_DIALOG_OUTPUT]";
	private static final String				PROMPT_VARIABLE_QUESTION		= "[PROMPT_VARIABLE_QUESTION]";

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
				sequence.append(example.getOutput());
				contextConfabulator.learnSequence(sequence,new StringBuilder(dialog.getName()));
			}
			for (DialogVariable variable: dialog.getVariables()) {
				for (DialogVariableExample example: variable.getExamples()) {
					StringBuilder sequence = new StringBuilder();
					sequence.append(example.getQuestion());
					sequence.append(" ");
					sequence.append(example.getAnswer());
					contextConfabulator.learnSequence(sequence,new StringBuilder(dialog.getName()));
				}
			}
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
		ContextConfabulation contextConfab = new ContextConfabulation();
		contextConfab.setSequence(input);
		
		lockMe(this);
		contextConfabulator.confabulate(contextConfab);
		unlockMe(this);
		
		StringBuilder confabulatedContext = contextConfab.getOutput();
		List<String> contextSymbols = SymbolParser.parseSymbols(confabulatedContext);
		boolean selectedDialog = false;
		
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
	
	protected final String getVariable(String name,String value) {
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
	
	protected final String getDialogVariable(String name,String value) {
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
	
	protected final static StringBuilder getSafeText(StringBuilder text) {
		StringBuilder r = new StringBuilder();
		text = Generic.stringBuilderTrim(text);
		text.replace(0,1,text.substring(0,1).toUpperCase());
		if (!SymbolParser.endsWithLineEndSymbol(text)) {
			text.append(".");
		}
		List<String> symbols = SymbolParser.parseSymbolsFromText(text);
		for (String symbol: symbols) {
			if (r.length()>0) {
				r.append(" ");
			}
			r.append(symbol);
		}
		return r;
	}
}
