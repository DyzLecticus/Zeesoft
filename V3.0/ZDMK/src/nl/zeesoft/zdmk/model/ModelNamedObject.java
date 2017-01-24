package nl.zeesoft.zdmk.model;

import nl.zeesoft.zdmk.model.transformations.TransformationNamedObjectSetName;
import nl.zeesoft.zdmk.model.transformations.TransformationObject;

/**
 * Abstract named model object.
 */
public abstract class ModelNamedObject extends ModelObject {
	private String name = "";
	
	public ModelNamedObject(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the full name for the current object.
	 * 
	 * The full name should be unique within the model.
	 * 
	 * @return The full name
	 */
	public abstract String getFullName();

	@Override
	protected String applyTransformation(TransformationObject transformation) {
		String error = "";
		if (transformation instanceof TransformationNamedObjectSetName) {
			setName(((TransformationNamedObjectSetName) transformation).getNewName());
		}
		return error;
	}

	/**
	 * Returns the name of this object.
	 *
	 * @return The name of this object
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this object.
	 * 
	 * @param name The name of this object
	 */
	public void setName(String name) {
		this.name = name;
	}
}
