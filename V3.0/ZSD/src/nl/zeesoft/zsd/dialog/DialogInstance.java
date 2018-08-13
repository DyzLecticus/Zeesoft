package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.EntityValueTranslator;
import nl.zeesoft.zsd.SequenceMatcher;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.entity.complex.ComplexObject;
import nl.zeesoft.zsd.entity.complex.ComplexPattern;

public class DialogInstance {
	public static final String					VARIABLE_NEXT_DIALOG	= "nextDialog";

	private String								language				= "";
	private String								masterContext			= "";
	private String								context					= "";
	private String								handlerClassName		= "";
	private String								defaultFilterContext	= "";

	private List<DialogIO>						examples				= new ArrayList<DialogIO>();
	private List<DialogVariable>				variables				= new ArrayList<DialogVariable>();

	private SequenceMatcher						matcher					= null;
	
	public void initialize(EntityValueTranslator t) {
		// Override to implement
	}
	
	public DialogIO addExample(String input, String output) {
		return addExample(new ZStringSymbolParser(input),output,"");
	}

	public DialogIO addExample(String input, String output,String filterContext) {
		return addExample(new ZStringSymbolParser(input),output,filterContext);
	}

	public DialogIO addExample(ZStringSymbolParser input, String output,String filterContext) {
		DialogIO r = new DialogIO();
		r.input.append(input);
		r.output.append(output);
		if (filterContext.length()>0) {
			r.filterContexts.add(filterContext);
		}
		examples.add(r);
		return r;
	}

	/**
	 * Adds complex patterns as dialog examples.
	 * Must be called after adding variables.
	 * 
	 * @param t The entity value translator
	 * @param type The complex entity type
	 */
	protected void addComplexPatterns(EntityValueTranslator t, String type) {
		EntityObject eo = t.getEntityObject(getLanguage(),type);
		if (eo!=null && eo instanceof ComplexObject) {
			ComplexObject co = (ComplexObject) eo;
			for (ComplexPattern pattern: co.getPatterns()) {
				addComplexPattern(t,pattern);
			}
		}
	}

	protected void addComplexPattern(EntityValueTranslator t,ComplexPattern pattern) {
		ZStringSymbolParser ptn = new ZStringSymbolParser(pattern.pattern);
		for (DialogVariable var: variables) {
			ptn.replace("{" + var.name + "}","[" + var.type + "]");
		}
		addExample(ptn,"","");
	}

	public DialogVariable addNextDialogVariable() {
		return addVariable(VARIABLE_NEXT_DIALOG,BaseConfiguration.TYPE_ALPHABETIC);
	}

	public DialogVariable addVariable(String name, String type) {
		return addVariable(name,type,"","","",true);
	}

	public DialogVariable addVariable(String name, String type,boolean overwrite) {
		return addVariable(name,type,"","","",overwrite);
	}
	
	public DialogVariable addVariable(String name, String type,String initialValue) {
		return addVariable(name,type,"","",initialValue,true);
	}
	
	public DialogVariable addVariable(String name, String type,String complexName,String complexType) {
		return addVariable(name,type,complexName,complexType,"",true);
	}
	
	public DialogVariable addVariable(String name, String type,String complexName,String complexType,String initialValue,boolean overwrite) {
		DialogVariable r = new DialogVariable();
		r.complexName = complexName;
		r.complexType = complexType;
		r.name = name;
		r.type = type;
		r.initialValue = initialValue;
		r.overwrite = overwrite;
		variables.add(r);
		return r;
	}

	public DialogVariablePrompt addVariablePrompt(String name, String prompt) {
		DialogVariablePrompt r = null;
		DialogVariable var = getVariable(name);
		if (var!=null) {
			r = new DialogVariablePrompt();
			r.prompt.append(prompt);
			var.prompts.add(r);
		}
		return r;
	}

	public void initializeMatcher() {
		matcher = new SequenceMatcher();
		for (DialogIO example: examples) {
			if (example.output.length()>0) {
				List<String> contexts = null;
				if (example.filterContexts.size()>0) {
					contexts = new ArrayList<String>(example.filterContexts);
				} else {
					contexts = new ArrayList<String>();
				}
				contexts.add("");
				ZStringSymbolParser sequence = new ZStringSymbolParser();
				sequence.append(example.input);
				sequence.append(" ");
				sequence.append(matcher.getIoSeparator());
				sequence.append(" ");
				sequence.append(example.output);
				matcher.addSymbols(sequence.toSymbolsPunctuated(),contexts);
			}
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
	
	public DialogVariable getVariable(String name) {
		DialogVariable r = null;
		for (DialogVariable variable: variables) {
			if (variable.name.equals(name)) {
				r = variable;
				break;
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

	public String getDefaultFilterContext() {
		return defaultFilterContext;
	}

	public void setDefaultFilterContext(String defaultFilterContext) {
		this.defaultFilterContext = defaultFilterContext;
	}

	public List<DialogIO> getExamples() {
		return examples;
	}

	public List<DialogVariable> getVariables() {
		return variables;
	}

	public SequenceMatcher getMatcher() {
		if (matcher==null) {
			initializeMatcher();
		}
		return matcher;
	}
}
