package nl.zeesoft.zdm.model.transformations.impl;

import nl.zeesoft.zdm.model.transformations.TransformationNamedPackageObject;

public class RemoveClass extends TransformationNamedPackageObject {
	public RemoveClass() {
	}
	
	public RemoveClass(String packageName, String name) {
		super(packageName,name);
	}

	@Override
	public String getDescription() {
		return "Removes a class from a package.";
	}

	@Override
	public String getNameParameterDescription() {
		return "The name of the class (without the package name prefix).";
	}
}
