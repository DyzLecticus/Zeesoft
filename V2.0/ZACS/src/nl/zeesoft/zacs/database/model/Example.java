package nl.zeesoft.zacs.database.model;

import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class Example extends HlpObject {
	private StringBuilder 	context			= new StringBuilder();
	private StringBuilder 	input			= new StringBuilder();
	private StringBuilder 	output			= new StringBuilder();
	
	public List<String> getSymbolList(boolean includeOutput) {
		StringBuilder text = new StringBuilder(input);
		if (includeOutput && output.length()>0) {
			text.append(" ");
			text.append(output);
		}
		return Symbol.parseTextSymbols(text,0);
	}
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("context")) {
			setContext(obj.getPropertyValue("context"));
		}
		if (obj.hasPropertyValue("input")) {
			setInput(obj.getPropertyValue("input"));
		}
		if (obj.hasPropertyValue("output")) {
			setOutput(obj.getPropertyValue("output"));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("context",getContext());
		r.setPropertyValue("input",getInput());
		r.setPropertyValue("output",getOutput());
		return r;
	}

	/**
	 * @return the context
	 */
	public StringBuilder getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(StringBuilder context) {
		this.context = context;
	}

	/**
	 * @return the input
	 */
	public StringBuilder getInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(StringBuilder input) {
		this.input = input;
	}

	/**
	 * @return the output
	 */
	public StringBuilder getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(StringBuilder output) {
		this.output = output;
	}

	public static StringBuilder cleanUpText(StringBuilder text) {
		text = Generic.stringBuilderReplace(text,"\n"," ");
		text = Generic.stringBuilderReplace(text,"\r"," ");
		text = Generic.stringBuilderReplace(text,"\""," \" ");
		text = Generic.stringBuilderReplace(text,"    "," ");
		text = Generic.stringBuilderReplace(text,"   "," ");
		text = Generic.stringBuilderReplace(text,"  "," ");
		return text;
	}
}
