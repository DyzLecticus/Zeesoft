package nl.zeesoft.zdm.model.transformations.impl;

import nl.zeesoft.zdm.model.transformations.TransformationObject;

public class IncrementVersion extends TransformationObject {
	@Override
	public String getDescription() {
		return "Creates a new version of the model containing all transformations needed to revert the model back to the current state.";
	}

	@Override
	protected void initializeParameters() {
		// No parameters required
	}
}
