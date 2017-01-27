package nl.zeesoft.zid.dialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zsc.Generic;
import nl.zeesoft.zsc.confabulator.Confabulator;
import nl.zeesoft.zspr.pattern.PatternManager;

public class DialogHandler extends Locker {
	private List<Dialog>				dialogs				= null;
	private PatternManager				patternManager		= null;

	private Confabulator				contextConfabulator	= new Confabulator();

	private StringBuilder				log					= new StringBuilder();
	private SortedMap<String,String>	variables			= new TreeMap<String,String>(); 
	private StringBuilder				prevOutput			= new StringBuilder();
	private Dialog						dialog				= null;
	private SortedMap<String,String>	dialogVariables		= new TreeMap<String,String>();
	
	public DialogHandler(List<Dialog> dialogs, PatternManager patternManager) {
		this.dialogs = dialogs;
		this.patternManager = patternManager;
	}
	
	public void initialize() {
		lockMe(this);
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
					sequence.append(example.getInput());
					sequence.append(" ");
					sequence.append(example.getOutput());
					contextConfabulator.learnSequence(sequence,new StringBuilder(dialog.getName()));
				}
			}
		}
		unlockMe(this);
	}
	
	public StringBuilder processInput(StringBuilder input) {
		addLogLine("<<< " + input);
		StringBuilder output = new StringBuilder();
		
		// Determine input context
		StringBuilder context = new StringBuilder();
		addLogLine("Context " + context);

		
		// Determine dialog
		// Use context to extend line ending if missing
		// Use context to correct input (parse variables)
		// Use context 
		
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
	
	protected final void setDialog(String name) {
		Dialog newDialog = getDialog(name);
		if (dialog!=newDialog) {
			lockMe(this);
			dialog = newDialog;
			unlockMe(this);
			clearDialogVariables();
			if (newDialog!=null) {
				for (DialogVariable dv: newDialog.getVariables()) {
					setDialogVariable(dv.getName(),"");
				}
			}
		}
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

	private Dialog getDialog(String name) {
		Dialog r = null;
		for (Dialog dialog: dialogs) {
			if (dialog.getName().equals(name)) {
				r = dialog;
				break;
			}
		}
		return r;
	}
}
