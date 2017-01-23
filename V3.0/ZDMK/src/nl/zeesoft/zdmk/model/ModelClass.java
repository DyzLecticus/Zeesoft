package nl.zeesoft.zdmk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a model package class.
 * 
 * Use the getNewClass method to obtain a new class for this package.
 */
public final class ModelClass extends ModelNamedObject {
	private ModelPackage		pack			= null;
	private ModelClass			extendsClass	= null;
	private boolean				abstr			= false;
	private List<ModelProperty>	properties		= new ArrayList<ModelProperty>();
	
	protected ModelClass() {
		
	}
	
	/**
	 * Adds a new property to this class.
	 * 
	 * @return The property
	 */
	public ModelProperty getNewProperty() {
		ModelProperty r = new ModelProperty();
		r.setCls(this);
		properties.add(r);
		return r;
	}
	
	@Override
	public String getFullName() {
		return pack.getFullName() + "." + getName();
	}

	@Override
	protected void cleanUp() {
		pack = null;
		extendsClass = null;
		for (ModelProperty prop: properties) {
			prop.cleanUp();
		}
		properties.clear();
	}

	@Override
	protected ModelObject getCopy() {
		ModelClass r = new ModelClass();
		r.setName(getName());
		r.setAbstr(abstr);
		r.setExtendsClass(extendsClass);
		for (ModelProperty prop: properties) {
			ModelProperty copy = (ModelProperty) prop.getCopy();
			copy.setCls(r);
			r.getProperties().add(copy);
		}
		return r;
	}

	/**
	 * Returns a specific class (extended) property.
	 * 
	 * @param name The name of the property
	 * @return The property or null if it does not exist
	 */
	public ModelProperty getProperty(String name) {
		ModelProperty r = null;
		for (ModelProperty prop: getExtendedProperties()) {
			if (prop.getName().equals(name)) {
				r = prop;
			}
		}
		return r;
	}

	/**
	 * Returns the package this class belongs to.
	 * 
	 * @return The package this class belongs to
	 */
	protected ModelPackage getPack() {
		return pack;
	}

	/**
	 * Sets the package this class belongs to.
	 * 
	 * @param pack The package this class belongs to
	 */
	protected void setPack(ModelPackage pack) {
		this.pack = pack;
		for (ModelProperty prop: properties) {
			prop.setCls(this);
		}
	}

	/**
	 * Returns the class that this class extends.
	 * 
	 * @return The class that this class extends
	 */
	public ModelClass getExtendsClass() {
		return extendsClass;
	}

	/**
	 * Sets the class that this class extends.
	 * 
	 * @param extendsClass The class that this class extends
	 */
	public void setExtendsClass(ModelClass extendsClass) {
		this.extendsClass = extendsClass;
	}

	/**
	 * Indicates the class is abstract.
	 * 
	 * @return True if the class is abstract
	 */
	public boolean isAbstr() {
		return abstr;
	}

	/**
	 * Indicates the class is abstract.
	 * 
	 * @param abstr True if the class is abstract
	 */
	public void setAbstr(boolean abstr) {
		this.abstr = abstr;
	}

	/**
	 * Returns the class properties.
	 *  
	 * @return The class properties
	 */
	public List<ModelProperty> getProperties() {
		return properties;
	}

	/**
	 * Returns a list of super classes of this class.
	 * 
	 * @return A list of super classes
	 */
	public List<ModelClass> getSuperClasses() {
		List<ModelClass> r = new ArrayList<ModelClass>();
		addSuperClassToList(r);
		return r;
	}
	
	/**
	 * Returns all properties of this class including extended properties.
	 * 
	 * @return All properties of this class including extended properties
	 */
	public List<ModelProperty> getExtendedProperties() {
		List<ModelProperty> r = new ArrayList<ModelProperty>();
		addPropertiesAndExtendedPropertiesToList(r);
		return r;
	}

	protected void addSuperClassToList(List<ModelClass> clss) {
		if (extendsClass!=null) {
			clss.add(extendsClass);
			extendsClass.addSuperClassToList(clss);
		}
	}
	
	protected void addPropertiesAndExtendedPropertiesToList(List<ModelProperty> props) {
		for (ModelProperty prop: properties) {
			boolean found = false;
			for (ModelProperty lProp: props) {
				if (lProp.getName().equals(prop.getName())) {
					found = true;
				}
			}
			if (!found) {
				props.add(prop);
			}
		}
		if (extendsClass!=null) {
			extendsClass.addPropertiesAndExtendedPropertiesToList(props);
		}
	}
}
