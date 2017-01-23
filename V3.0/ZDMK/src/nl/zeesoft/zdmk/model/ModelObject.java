package nl.zeesoft.zdmk.model;

import java.util.List;

import nl.zeesoft.zdmk.model.transformations.TransformationObject;

/**
 * Abstract model object.
 */
public abstract class ModelObject {
	/**
	 * Applies a transformation to this object.
	 * 
	 * @param transformation The transformation to apply
	 * @return The error message if applicable
	 */
	protected abstract String applyTransformation(TransformationObject transformation);

	/**
	 * Cleans up references for garbage collection
	 */
	protected abstract void cleanUp();

	/**
	 * Returns a copy of the model object. 
	 * 
	 * @return A copy of the model object
	 */
	protected abstract ModelObject getCopy();
	
	/**
	 * Applies a list of transformations to this object.
	 * 
	 * @param transformations The transformations to apply
	 * @return The error message if applicable
	 */
	protected String applyTransformations(List<TransformationObject> transformations) {
		String error = "";
		for (TransformationObject trans: transformations) {
			error = applyTransformation(trans);
			if (error.length()>0) {
				break;
			}
		}
		return error;
	}
}
