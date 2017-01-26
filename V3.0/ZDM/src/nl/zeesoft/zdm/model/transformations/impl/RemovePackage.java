package nl.zeesoft.zdm.model.transformations.impl;

import nl.zeesoft.zdm.model.transformations.TransformationNamedObject;

public class RemovePackage extends TransformationNamedObject {
	public RemovePackage() {
	}
	
	public RemovePackage(String name) {
		super(name);
	}
	
	@Override
	public String getDescription() {
		return "Removes a package from the model.";
	}

	@Override
	public String getNameParameterDescription() {
		return "The name of the package.";
	}
}
