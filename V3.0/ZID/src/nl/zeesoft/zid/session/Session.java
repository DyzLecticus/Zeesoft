package nl.zeesoft.zid.session;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogVariable;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObject;

/**
 * Sessions are used to maintain dialog states.
 */
public class Session {
	private	long							id								= 0;
	private	ZDate							start							= new ZDate();
	private	ZDate							lastActivity					= new ZDate();
	private	ZDate							end								= null;
	private	String							externalId						= "";

	private ZStringSymbolParser				input							= new ZStringSymbolParser();
	private ZStringSymbolParser				output							= new ZStringSymbolParser();

	private ZStringBuilder					log								= new ZStringBuilder();
	private SortedMap<String,Object>		variables						= new TreeMap<String,Object>(); 

	private Dialog							dialog							= null;
	private SessionDialogController			dialogController				= null;
	private SortedMap<String,String>		dialogVariables					= new TreeMap<String,String>();
	private String							promptForDialogVariable			= "";

	private PatternManager					patternManager					= null;

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

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
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
	
	public ZStringBuilder getLog() {
		return log;
	}
	
	public void setLog(ZStringBuilder log) {
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

	public PatternManager getPatternManager() {
		return patternManager;
	}

	public void setPatternManager(PatternManager patternManager) {
		this.patternManager = patternManager;
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
	
	public String translateSymbolToVariableValue(String symbol) {
		if (symbol.startsWith("{") && symbol.endsWith("}") && symbol.length()>2) {
			String name = symbol.substring(1,(symbol.length()-1));
			DialogVariable variable = null;
			String value = "";
			if (getDialog()!=null) {
				variable = getDialog().getVariable(name);
				value = getDialogVariables().get(name);
			}
			if (variable!=null && value.length()>0) {
				symbol = getPatternStringForDialogVariableValue(variable, value);
			} else {
				Object obj = getVariables().get(name);
				if (obj!=null) {
					symbol = obj.toString();
				}
			}
		}
		return symbol;
	}

	public String getPatternStringForDialogVariableValue(DialogVariable variable,String value) {
		String r = "";
		PatternObject pattern = getPatternForDialogVariableValue(variable,value);
		if (pattern!=null) {
			r = pattern.getStringForValue(value);
		}
		return r;
	}

	public PatternObject getPatternForDialogVariableValue(DialogVariable variable,String value) {
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
	
	public String getDialogVariableValueString(String name) {
		return getPatternStringForDialogVariableValue(getDialog().getVariable(name),getDialogVariables().get(name));
	}
}
