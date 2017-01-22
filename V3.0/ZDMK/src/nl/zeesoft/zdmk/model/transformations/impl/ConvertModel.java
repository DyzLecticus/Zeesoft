package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationObject;

public class ConvertModel extends TransformationObject {
	public ConvertModel() {
	}

	public ConvertModel(String idPropertyName) {
		setIdPropertyName(idPropertyName);
	}
	
	public ConvertModel(String idPropertyName,String idPropertyType) {
		setIdPropertyName(idPropertyName);
		setIdPropertyType(idPropertyType);
	}
	
	@Override
	public String getDescription() {
		return "Converts the current model to an object model. This means abstract classes are removed, extended class properties are made explicit, and class extensions are set to null.";
	}

	@Override
	protected void initializeParameters() {
		addParameter("idPropertyName",false,"The name of the ID property to add. Specifying this parameter will make the conversion add a property with the specified name to all remaining classes.");
		addParameter("idPropertyType",false,"The type of the ID property. Defaults to '" + String.class.getName() + "'.");
	}
	
	public String getIdPropertyName() {
		return getParameterValue("idPropertyName");
	}
	
	public void setIdPropertyName(String idPropertyName) {
		setParameterValue("idPropertyName",idPropertyName);
	}
	
	public String getIdPropertyType() {
		return getParameterValue("idPropertyType");
	}
	
	public void setIdPropertyType(String idPropertyType) {
		setParameterValue("idPropertyType",idPropertyType);
	}
}
