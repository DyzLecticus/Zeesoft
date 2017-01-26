package nl.zeesoft.zdm.model.transformations.impl;

import nl.zeesoft.zdm.model.transformations.TransformationObject;

public class RevertVersionCurrent extends TransformationObject {
	@Override
	public String getDescription() {
		return "Reverts all transformations made within the current model version.";
	}

	@Override
	protected void initializeParameters() {
		// No parameters required
	}
}
