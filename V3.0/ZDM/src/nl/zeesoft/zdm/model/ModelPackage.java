package nl.zeesoft.zdm.model;

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
	
	public ModelPackage(String name) {
		super(name);
	}
	
	/**
	 * Adds a new class to this package.
	 * 
	 * @param name The name of the class
	 * @return The class
	 */
	public ModelClass getNewClass(String name) {
		ModelClass r = new ModelClass(name);
		r.setPack(this);
		classes.add(r);
		return r;
	}

	/**
	 * Removes a specific class from this package.
	 * 
	 * @param name The name of the class
	 */
	public void removeClass(String name) {
		ModelClass cls = getClass(name);
		if (cls!=null) {
			cls.cleanUp();
			classes.remove(cls);
		}
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
		ModelPackage r = new ModelPackage(getName());
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
		return new ArrayList<ModelClass>(classes);
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
