package nl.zeesoft.zid.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zid.session.SessionDialogController;
import nl.zeesoft.zspr.Language;

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
	
	public void addExample(String input, String output) {
		DialogExample example = new DialogExample(new ZStringSymbolParser(input),new ZStringSymbolParser(output));
		examples.add(example);
	}

	public void addVariable(String name,String type) {
		if (getVariable(name)==null) {
			DialogVariable variable = new DialogVariable(name,type);
			variables.add(variable);
		}
	}

	public void addVariableExample(String name,String question, String answer) {
		DialogVariable variable = getVariable(name);
		if (variable!=null) {
			variable.addExample(question, answer);
		}
	}

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

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
	
	public String getControllerClassName() {
		return controllerClassName;
	}

	public void setControllerClassName(String controllerClassName) {
		this.controllerClassName = controllerClassName;
	}

	public List<DialogExample> getExamples() {
		return new ArrayList<DialogExample>(examples);
	}

	public List<DialogVariable> getVariables() {
		return new ArrayList<DialogVariable>(variables);
	}

	public String getName() {
		return name;
	}
	
	public boolean isEnglish() {
		return isLanguageCode(Language.ENG);
	}

	public boolean isDutch() {
		return isLanguageCode(Language.NLD);
	}

	public boolean isLanguageCode(String languageCode) {
		return language.getCode().equals(languageCode);
	}

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
