package nl.zeesoft.zodb.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Generic;

public class MdlCollectionProperty {
	private	MdlCollection	modelCollection		= null;
	private String 			name 				= "";
	private String			dataTypeClassName	= "";
	private String 			method				= "";
	private String 			label 				= "";
	private List<String>	constraints			= new ArrayList<String>();
	private long			minValue			= Long.MIN_VALUE;
	private long			maxValue			= Long.MAX_VALUE;

	protected MdlCollectionProperty() {
		
	}
	
	public String getCrcText() {
		String text = 
			name + Generic.SEP_STR + 
			dataTypeClassName + Generic.SEP_STR + 
			method + Generic.SEP_STR + 
			/*label + Generic.SEP_STR +*/ 
			minValue + Generic.SEP_STR + 
			maxValue;
		for (String constrain: constraints) {
			text = text + Generic.SEP_STR + constrain;
		}
		return text;
	}
	
	/**
	 * @return the modelCollection
	 */
	public MdlCollection getModelCollection() {
		return modelCollection;
	}
	
	/**
	 * @param modelCollection the modelCollection to set
	 */
	protected void setModelCollection(MdlCollection modelCollection) {
		this.modelCollection = modelCollection;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * @param method the property to set
	 */
	protected void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the constraints
	 */
	public List<String> getConstraints() {
		return constraints;
	}

	/**
	 * @param constraints the constraints to set
	 */
	public void setConstraints(List<String> constraints) {
		this.constraints = constraints;
	}

	/**
	 * @return the dataTypeClassName
	 */
	public String getDataTypeClassName() {
		return dataTypeClassName;
	}

	/**
	 * @param dataTypeClassName the dataTypeClassName to set
	 */
	public void setDataTypeClassName(String dataTypeClassName) {
		this.dataTypeClassName = dataTypeClassName;
	}

	/**
	 * @return the minValue
	 */
	public long getMinValue() {
		return minValue;
	}

	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(long minValue) {
		this.minValue = minValue;
	}

	/**
	 * @return the maxValue
	 */
	public long getMaxValue() {
		return maxValue;
	}

	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(long maxValue) {
		this.maxValue = maxValue;
	}
}
