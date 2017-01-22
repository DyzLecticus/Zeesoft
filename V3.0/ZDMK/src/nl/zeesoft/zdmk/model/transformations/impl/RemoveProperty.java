package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationNamedPackageClassObject;

public class RemoveProperty extends TransformationNamedPackageClassObject {
	public RemoveProperty() {
		
	}
	
	public RemoveProperty(String packageName,String className,String name) {
		super(packageName,className,name);
	}
	
	@Override
	public String getDescription() {
		return "Removes a property from a class.";
	}

	@Override
	public String getNameParameterDescription() {
		return "The name of the property.";
	}
}
