package nl.zeesoft.zids.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class DialogVariableExample extends HlpObject {
	private StringBuilder	input					= new StringBuilder();
	private StringBuilder	output					= new StringBuilder();
	
	private long			variableId				= 0; 
	private DialogVariable	variable				= null; 
		
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("input")) {
			setInput(obj.getPropertyValue("input"));
		}
		if (obj.hasPropertyValue("output")) {
			setOutput(obj.getPropertyValue("output"));
		}
		if (obj.hasPropertyValue("variable") && obj.getLinkValue("variable").size()>0) {
			setVariableId(obj.getLinkValue("variable").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("input",getInput());
		r.setPropertyValue("output",getOutput());
		r.setLinkValue("variable",getVariableId());
		return r;
	}

	/**
	 * @return the variableId
	 */
	public long getVariableId() {
		return variableId;
	}

	/**
	 * @param variableId the variableId to set
	 */
	public void setVariableId(long variableId) {
		this.variableId = variableId;
	}

	/**
	 * @return the variable
	 */
	public DialogVariable getVariable() {
		return variable;
	}

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(DialogVariable variable) {
		if (variable==null) {
			variableId = 0;
		} else {
			variableId = variable.getId();
		}
		this.variable = variable;
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

}
