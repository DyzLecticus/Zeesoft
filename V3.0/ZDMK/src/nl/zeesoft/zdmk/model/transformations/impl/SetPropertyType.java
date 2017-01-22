package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationNamedPackageClassObject;

public class SetPropertyType extends TransformationNamedPackageClassObject {
	public SetPropertyType() {
	}
	
	public SetPropertyType(String packageName,String className,String name,String type) {
		super(packageName,className,name);
		this.setType(type);
	}
	
	@Override
	public String getDescription() {
		return "Sets the type property of a property to the specified value.";
	}

	@Override
	public String getNameParameterDescription() {
		return "The name of the property.";
	}
	
	@Override
	protected void initializeParameters() {
		super.initializeParameters();
		addParameter("type",true,"The type class name including the package name.");
	}
	
	public String getType() {
		return getParameterValue("type");
	}

	public void setType(String type) {
		setParameterValue("type",type);
	}
}
