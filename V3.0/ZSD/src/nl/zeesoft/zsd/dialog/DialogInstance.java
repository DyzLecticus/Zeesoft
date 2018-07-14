package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.SequenceMatcher;

public class DialogInstance {
	public static final String					VARIABLE_NEXT_DIALOG	= "nextDialog";

	private String								language				= "";
	private String								masterContext			= "";
	private String								context					= "";
	private String								handlerClassName		= "";

	private List<DialogIO>						examples				= new ArrayList<DialogIO>();
	// TODO: make regular list
	private SortedMap<String,DialogVariable>	variables				= new TreeMap<String,DialogVariable>();

	private SequenceMatcher						matcher					= null;
	
	public void initialize() {
		// Override to implement
	}
	
	public DialogIO addExample(String input, String output) {
		DialogIO r = new DialogIO();
		r.input.append(input);
		r.output.append(output);
		examples.add(r);
		return r;
	}

	public DialogVariable addVariable(String name, String type) {
		return addVariable("","",name,type,"");
	}
	
	public DialogVariable addVariable(String name, String type,String initialValue) {
		return addVariable("","",name,type,initialValue);
	}
	
	public DialogVariable addVariable(String complexName,String complexType,String name, String type) {
		return addVariable("","",name,type,"");
	}
	
	public DialogVariable addVariable(String complexName,String complexType,String name, String type,String initialValue) {
		DialogVariable r = new DialogVariable();
		r.complexName = complexName;
		r.complexType = complexType;
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

	public void initializeMatcher() {
		matcher = new SequenceMatcher();
		for (DialogIO example: examples) {
			ZStringSymbolParser sequence = new ZStringSymbolParser();
			sequence.append(example.input);
			sequence.append(" ");
			sequence.append(matcher.getIoSeparator());
			sequence.append(" ");
			sequence.append(example.output);
			matcher.addSequence(sequence);
		}
		matcher.calculateProb();
	}

	public DialogInstanceHandler getNewHandler() {
		DialogInstanceHandler r = null;
		if (handlerClassName.length()>0) {
			try {
				Class<?> clas = Class.forName(handlerClassName);
				Object o = (DialogInstanceHandler) clas.newInstance();
				if (o instanceof DialogInstanceHandler) {
					r = (DialogInstanceHandler) o;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
	
	public String getId() {
		return language + "/" + masterContext + "/" + context;
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

	public SequenceMatcher getMatcher() {
		if (matcher==null) {
			initializeMatcher();
		}
		return matcher;
	}
}
