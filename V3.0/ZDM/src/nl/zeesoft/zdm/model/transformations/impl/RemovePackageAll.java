package nl.zeesoft.zdm.model.transformations.impl;

import nl.zeesoft.zdm.model.transformations.TransformationObject;

public class RemovePackageAll extends TransformationObject {
	@Override
	public String getDescription() {
		return "Removes all packages from the model.";
	}

	@Override
	protected void initializeParameters() {
		// No parameters required
	}
}
