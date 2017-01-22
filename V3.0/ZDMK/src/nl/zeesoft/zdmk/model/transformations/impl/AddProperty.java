package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationNamedPackageClassObject;
import nl.zeesoft.zdmk.model.transformations.TransformationParameter;

public class AddProperty extends TransformationNamedPackageClassObject {
	public AddProperty() {
	}

	public AddProperty(String packageName,String className,String name) {
		super(packageName,className,name);
	}
	
	public AddProperty(String packageName,String className,String name,String type) {
		super(packageName,className,name);
		setType(type);
	}
	
	public AddProperty(String packageName,String className,String name,String type,boolean list) {
		super(packageName,className,name);
		setType(type);
		setList("" + list);
	}
	
	@Override
	public String getDescription() {
		return "Adds a property to a class.";
	}
	
	@Override
	public String getNameParameterDescription() {
		return "The name of the property.";
	}
	
	@Override
	protected void initializeParameters() {
		super.initializeParameters();
		addParameter("type",false,"The type class name including the package name. Defaults to '" + String.class.getName() + "' if omitted.");
		addParameter("list",false,"Indicates the property can contain multiple values. Accepts 'true' or 'false'. Defaults to 'false' if omitted.");
	}
	
	public String getType() {
		return getParameterValue("type");
	}

	public void setType(String type) {
		setParameterValue("type",type);
	}
	
	public String getList() {
		return getParameterValue("list");
	}

	public void setList(String list) {
		setParameterValue("list",list);
	}

	@Override
	protected String checkParameter(TransformationParameter param) {
		String error = "";
		if (param.getName().equals("list") && param.getValue().length()>0) {
			if (!param.getValue().equals("true") && !param.getValue().equals("false")) {
				error = "Parameter " + param.getName() + "; value must be either 'true' or 'false'";
			}
		}
		return error;
	}
}
