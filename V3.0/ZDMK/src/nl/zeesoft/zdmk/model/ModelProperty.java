package nl.zeesoft.zdmk.model;

import java.util.List;

import nl.zeesoft.zdmk.model.transformations.TransformationObject;
import nl.zeesoft.zdmk.model.transformations.impl.AddProperty;

/**
 * Represents a model package class property.
 */
public final class ModelProperty extends ModelNamedObject {
	private ModelClass		cls				= null;
	private String			type			= String.class.getName();
	private boolean			list			= false;
	
	@Override
	protected void addInitialTransformationsToList(List<TransformationObject> list) {
		AddProperty trans = new AddProperty(cls.getPack().getName(),cls.getName(),getName());
		if (type.length()>0) {
			trans.setType(type);
		}
		if (this.list) {
			trans.setList("true");
		}
		list.add(trans);
	}

	@Override
	public String getFullName() {
		return cls.getFullName() + ":" + getName();
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();
		cls = null;
	}

	@Override
	protected ModelObject getCopy() {
		ModelProperty r = new ModelProperty();
		r.setName(getName());
		r.setType(type);
		r.setList(list);
		return r;
	}

	/**
	 * Returns the class this property belongs to.
	 * 
	 * @return The class this property belongs to
	 */
	public ModelClass getCls() {
		return cls;
	}

	/**
	 * Sets the class this property belongs to.
	 * 
	 * @param cls The class this property belongs to
	 */
	public void setCls(ModelClass cls) {
		this.cls = cls;
	}
	
	/**
	 * Returns the property type.
	 * 
	 * @return The property type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the property type.
	 * 
	 * @param type The property type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Indicates the type of this property is to be wrapped in a list
	 * 
	 * @return True if the type of this property is to be wrapped in a list
	 */
	public boolean isList() {
		return list;
	}

	/**
	 * Indicates the type of this property is to be wrapped in a list
	 * 
	 * @param list True if the type of this property is to be wrapped in a list
	 */
	public void setList(boolean list) {
		this.list = list;
	}
}
