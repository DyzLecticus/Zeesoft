package nl.zeesoft.zid.session;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogVariable;

public class Session {
	private	long							id								= 0;
	private	ZDate							start							= new ZDate();
	private	ZDate							lastActivity					= new ZDate();
	private	ZDate							end								= null;

	private ZStringSymbolParser				input							= new ZStringSymbolParser();
	private ZStringSymbolParser				output							= new ZStringSymbolParser();

	private StringBuilder					log								= new StringBuilder();
	private SortedMap<String,Object>		variables						= new TreeMap<String,Object>(); 

	private Dialog							dialog							= null;
	private SessionDialogController			dialogController				= null;
	private SortedMap<String,String>		dialogVariables					= new TreeMap<String,String>();
	private String							promptForDialogVariable			= "";

	public Session(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}

	public ZDate getStart() {
		return start;
	}

	public ZDate getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(ZDate lastActivity) {
		this.lastActivity = lastActivity;
	}

	public ZDate getEnd() {
		return end;
	}

	public void setEnd(ZDate end) {
		this.end = end;
	}
	
	public ZStringSymbolParser getInput() {
		return input;
	}
	
	public void setInput(ZStringSymbolParser input) {
		this.input = input;
	}
	
	public ZStringSymbolParser getOutput() {
		return output;
	}
	
	public void setOutput(ZStringSymbolParser output) {
		this.output = output;
	}
	
	public void addLogLine(String line) {
		log.append((new ZDate()).getDateTimeString());
		log.append(": ");
		log.append(line);
		log.append("\n");
	}
	
	public StringBuilder getLog() {
		return log;
	}
	
	public void setLog(StringBuilder log) {
		this.log = log;
	}
	
	public Dialog getDialog() {
		return dialog;
	}
	
	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}
	
	public SessionDialogController getDialogController() {
		return dialogController;
	}
	
	public void setDialogController(SessionDialogController dialogController) {
		this.dialogController = dialogController;
	}
	
	public String getPromptForDialogVariable() {
		return promptForDialogVariable;
	}
	
	public void setPromptForDialogVariable(String promptForDialogVariable) {
		this.promptForDialogVariable = promptForDialogVariable;
	}
	
	public SortedMap<String, Object> getVariables() {
		return variables;
	}
	
	public SortedMap<String, String> getDialogVariables() {
		return dialogVariables;
	}
	
	public void clearDialog() {
		dialog = null;
		dialogController = null;
		promptForDialogVariable = "";
		dialogVariables.clear();
	}

	public void initializeDialog(Dialog dialog) {
		setDialog(dialog);
		dialogController = dialog.getNewDialogController();
		promptForDialogVariable = "";
		dialogVariables.clear();
		for (DialogVariable dv: dialog.getVariables()) {
			if (promptForDialogVariable.length()==0) {
				promptForDialogVariable = dv.getName();
			}
			dialogVariables.put(dv.getName(),"");
		}
	}

	public DialogVariable getDialogVariable() {
		DialogVariable r = null;
		if (dialog!=null && promptForDialogVariable.length()>0) {
			r = dialog.getVariable(promptForDialogVariable);
		}
		return r;
	}
}
