package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationNamedPackageObject;

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
