package nl.zeesoft.zdmk.model;

/**
 * Represents a model package class property.
 */
public final class ModelProperty extends ModelNamedObject {
	private ModelClass		cls				= null;
	private String			type			= String.class.getName();
	private boolean			list			= false;
	
	protected ModelProperty() {
		
	}

	@Override
	public String getFullName() {
		return cls.getFullName() + ":" + getName();
	}

	@Override
	protected void cleanUp() {
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
	protected ModelClass getCls() {
		return cls;
	}

	/**
	 * Sets the class this property belongs to.
	 * 
	 * @param cls The class this property belongs to
	 */
	protected void setCls(ModelClass cls) {
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
