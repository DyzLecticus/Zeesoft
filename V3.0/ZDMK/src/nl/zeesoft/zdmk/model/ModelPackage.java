package nl.zeesoft.zdmk.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdmk.model.transformations.TransformationObject;
import nl.zeesoft.zdmk.model.transformations.impl.AddPackage;

/**
 * Represents a model package.
 */
public final class ModelPackage extends ModelNamedObject {
	private List<ModelClass>	classes	= new ArrayList<ModelClass>();
	
	@Override
	protected void addInitialTransformationsToList(List<TransformationObject> list) {
		list.add(new AddPackage(getName()));
		for (ModelClass cls: classes) {
			cls.addInitialTransformationsToList(list);
		}
	}

	@Override
	public String getFullName() {
		return getName();
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();
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

	@Override
	protected void setModel(Model model) {
		super.setModel(model);
		for (ModelClass cls: classes) {
			cls.setModel(model);
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
