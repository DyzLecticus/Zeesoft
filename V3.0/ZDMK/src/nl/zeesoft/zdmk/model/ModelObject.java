package nl.zeesoft.zdmk.model;

import java.util.List;

import nl.zeesoft.zdmk.model.transformations.TransformationObject;

/**
 * Abstract model object.
 */
public abstract class ModelObject {
	private Model	model	= null;
	
	/**
	 * Applies a transformation to this object.
	 * 
	 * @param transformation The transformation to apply
	 * @return The error message if applicable
	 */
	protected abstract String applyTransformation(TransformationObject transformation);
	
	/**
	 * Adds initial model state transformations to a list.
	 * 
	 * These transformations can be used to reconstruct the current model state.
	 * 
	 * @param list The list to add the transformations to
	 */
	protected abstract void addInitialTransformationsToList(List<TransformationObject> list);
	
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

	/**
	 * Cleans up references for garbage collection
	 */
	protected void cleanUp() {
		model = null;
	}
	
	/**
	 * Returns the model this object belongs to.
	 * 
	 * @return The model this object belongs to
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * Sets the model this object belongs to.
	 *  
	 * @param model The model this object belongs to
	 */
	protected void setModel(Model model) {
		this.model = model;
	}

	/**
	 * Returns a copy of the model object. 
	 * 
	 * @return A copy of the model object
	 */
	protected ModelObject getCopy() {
		return null;
	}
}
