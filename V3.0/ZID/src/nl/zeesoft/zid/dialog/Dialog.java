package nl.zeesoft.zid.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zid.session.SessionDialogController;
import nl.zeesoft.zspr.Language;

/**
 * Generic dialog class.
 * 
 * Use the Language class to specify dialog languages. 
 */
public class Dialog {
	private String					name				= "";
	private Language				language			= null;
	private String					controllerClassName	= "";
	private List<DialogExample>		examples			= new ArrayList<DialogExample>();
	private List<DialogVariable>	variables			= new ArrayList<DialogVariable>();
	
	public Dialog(String name,String languageCode,String controllerClassName) {
		this.name = name;
		this.language = Language.getLanguage(languageCode);
		this.controllerClassName = controllerClassName;
	}
	
	/**
	 * Adds a dialog input / output example to this dialog.
	 * 
	 * @param input The example input
	 * @param output The example output
	 */
	public void addExample(String input, String output) {
		DialogExample example = new DialogExample(new ZStringSymbolParser(input),new ZStringSymbolParser(output));
		examples.add(example);
	}

	/**
	 * Adds a dialog variable to this dialog.
	 * 
	 * @param name The variable name 
	 * @param type The variable type (See PatternObject.TYPES)
	 */
	public void addVariable(String name,String type) {
		if (getVariable(name)==null) {
			DialogVariable variable = new DialogVariable(name,type);
			variables.add(variable);
		}
	}

	/**
	 * Adds a dialog variable example to a specified dialog variable.
	 * 
	 * @param name The variable name 
	 * @param question The variable example question
	 * @param answer The variable example answer
	 */
	public void addVariableExample(String name,String question, String answer) {
		DialogVariable variable = getVariable(name);
		if (variable!=null) {
			variable.addExample(question, answer);
		}
	}

	/**
	 * Returns a specific dialog variable or null if it does not exist.
	 * 
	 * @param name The variable name
	 * @return The dialog variable or null
	 */
	public DialogVariable getVariable(String name) {
		DialogVariable r = null;
		for (DialogVariable variable: variables) {
			if (variable.getName().equals(name)) {
				r = variable;
				break;
			}
		}
		return r;
	}

	/**
	 * Returns the language this dialog belongs to.
	 * 
	 * @return The language
	 */
	public Language getLanguage() {
		return language;
	}

	/**
	 * Sets the language this dialog belongs to.
	 * 
	 * @param language The language
	 */
	public void setLanguage(Language language) {
		this.language = language;
	}
	
	/**
	 * Returns the controller class name.
	 * 
	 * @return The controller class name
	 */
	public String getControllerClassName() {
		return controllerClassName;
	}

	/**
	 * Sets the controller class name.
	 * 
	 * @param controllerClassName The controller class name
	 */
	public void setControllerClassName(String controllerClassName) {
		this.controllerClassName = controllerClassName;
	}

	/**
	 * Returns the dialog input / output examples.
	 * 
	 * @return The dialog input / output examples
	 */
	public List<DialogExample> getExamples() {
		return new ArrayList<DialogExample>(examples);
	}

	/**
	 * Returns the dialog variables.
	 * 
	 * @return The dialog variables
	 */
	public List<DialogVariable> getVariables() {
		return new ArrayList<DialogVariable>(variables);
	}

	/**
	 * Returns the dialog name.
	 * 
	 * @return The dialog name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns true if the dialog language is English.
	 * 
	 * @return True if the dialog language is English
	 */
	public boolean isEnglish() {
		return isLanguageCode(Language.ENG);
	}

	/**
	 * Returns true if the dialog language is Dutch.
	 * 
	 * @return True if the dialog language is Dutch
	 */
	public boolean isDutch() {
		return isLanguageCode(Language.NLD);
	}

	/**
	 * Returns true if the dialog matches a specified language code.
	 * 
	 * @param languageCode The language code
	 * @return True if the dialog matches a specified language code
	 */
	public boolean isLanguageCode(String languageCode) {
		return language.getCode().equals(languageCode);
	}

	/**
	 * Uses the controller class name to instantiates and returns a new session dialog controller.
	 * 
	 * @return A new session dialog controller
	 */
	public SessionDialogController getNewDialogController()  {
		SessionDialogController r = null;
		try {
			Class<?> clas = Class.forName(controllerClassName);
			r = (SessionDialogController) clas.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * Returns a string of dialog variable types this dialog expects.
	 * 
	 * @return A string of dialog variable types this dialog expects
	 */
	public List<String> getExpectedTypes() {
		List<String> expectedTypes = new ArrayList<String>();
		for (DialogVariable variable: getVariables()) {
			if (!expectedTypes.contains(variable.getType())) {
				expectedTypes.add(variable.getType());
			}
		}
		return expectedTypes;
	}
}
