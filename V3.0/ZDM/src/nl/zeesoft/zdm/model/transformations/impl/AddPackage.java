package nl.zeesoft.zdm.model.transformations.impl;

import nl.zeesoft.zdm.model.transformations.TransformationNamedObject;

public class AddPackage extends TransformationNamedObject {
	public AddPackage() {
	}

	public AddPackage(String name) {
		super(name);
	}

	@Override
	public String getDescription() {
		return "Adds a package to the model.";
	}
	
	@Override
	public String getNameParameterDescription() {
		return "The name of the package.";
	}
}
