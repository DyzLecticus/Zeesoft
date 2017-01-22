package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationObject;

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
