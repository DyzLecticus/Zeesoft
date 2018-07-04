package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class DialogObject {
	private String								language			= "";
	private String								masterContext		= "";
	private String								context				= "";
	private String								handlerClassName	= "";

	private List<DialogIO>						examples			= new ArrayList<DialogIO>();
	private SortedMap<String,DialogVariable>	variables			= new TreeMap<String,DialogVariable>();

	public abstract void initialize();
	
	public DialogIO addExample(String input, String output) {
		DialogIO r = new DialogIO();
		r.input.append(input);
		r.output.append(output);
		examples.add(r);
		return r;
	}
	
	public DialogVariable addVariable(String name, String type) {
		DialogVariable r = new DialogVariable();
		r.name = name;
		r.type = type;
		variables.put(name,r);
		return r;
	}

	public DialogVariableQA addVariableQA(String name, String question, String answer) {
		DialogVariableQA r = null;
		DialogVariable var = variables.get(name);
		if (var!=null) {
			r = new DialogVariableQA();
			r.question.append(question);
			r.answer.append(answer);
			var.examples.add(r);
		}
		return r;
	}

	public DialogHandler getNewDialogHandler() {
		DialogHandler r = null;
		try {
			Class<?> clas = Class.forName(handlerClassName);
			Object o = (DialogHandler) clas.newInstance();
			if (o instanceof DialogHandler) {
				r = (DialogHandler) o;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return r;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getMasterContext() {
		return masterContext;
	}

	public void setMasterContext(String masterContext) {
		this.masterContext = masterContext;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getHandlerClassName() {
		return handlerClassName;
	}

	public void setHandlerClassName(String handlerClassName) {
		this.handlerClassName = handlerClassName;
	}

	public List<DialogIO> getExamples() {
		return examples;
	}

	public SortedMap<String, DialogVariable> getVariables() {
		return variables;
	}
}
