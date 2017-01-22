package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationNamedPackageClassObject;
import nl.zeesoft.zdmk.model.transformations.TransformationParameter;

public class SetPropertyList extends TransformationNamedPackageClassObject {
	public SetPropertyList() {
	}
	
	public SetPropertyList(String packageName,String className,String name,boolean list) {
		super(packageName,className,name);
		this.setList("" + list);
	}
	
	@Override
	public String getDescription() {
		return "Sets the list property of a property to the specified value.";
	}

	@Override
	public String getNameParameterDescription() {
		return "The name of the property.";
	}
	
	@Override
	protected void initializeParameters() {
		super.initializeParameters();
		addParameter("list",true,"Indicates the property can contain multiple values. Accepts 'true' or 'false'.");
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
