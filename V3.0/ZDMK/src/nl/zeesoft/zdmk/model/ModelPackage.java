package nl.zeesoft.zdmk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a model package.
 * 
 * Use the getNewClass method to obtain a new class for this package.
 */
public final class ModelPackage extends ModelNamedObject {
	private Model				model	= null;
	private List<ModelClass>	classes	= new ArrayList<ModelClass>();
	
	/**
	 * Adds a new class to this package.
	 * 
	 * @return The class
	 */
	public ModelClass getNewClass() {
		ModelClass r = new ModelClass();
		r.setPack(this);
		classes.add(r);
		return r;
	}
	
	@Override
	public String getFullName() {
		return getName();
	}

	@Override
	protected void cleanUp() {
		model = null;
		for (ModelClass cls: classes) {
			cls.cleanUp();
		}
		classes.clear();
	}

	@Override
	protected ModelObject getCopy() {
		ModelPackage r = new ModelPackage();
		r.setName(getName());
		for (ModelClass cls: classes) {
			ModelClass copy = (ModelClass) cls.getCopy();
			copy.setPack(r);
			r.getClasses().add(copy);
		}
		return r;
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
		for (ModelClass cls: classes) {
			cls.setPack(this);
		}
	}
	
	/**
	 * Returns the package classes.
	 * 
	 * @return The package classes
	 */
	public List<ModelClass> getClasses() {
		return classes;
	}
	
	/**
	 * Returns a specific package class.
	 * 
	 * @param name The name of the class
	 * @return The class or null if it does not exist
	 */
	public ModelClass getClass(String name) {
		ModelClass r = null;
		for (ModelClass cls: classes) {
			if (cls.getName().equals(name)) {
				r = cls;
				break;
			}
		}
		return r;
	}
}
