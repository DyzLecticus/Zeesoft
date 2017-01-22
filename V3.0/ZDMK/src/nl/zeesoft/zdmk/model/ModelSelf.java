package nl.zeesoft.zdmk.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdmk.model.transformations.TransformationObject;
import nl.zeesoft.zdmk.model.transformations.impl.AddClass;
import nl.zeesoft.zdmk.model.transformations.impl.AddPackage;
import nl.zeesoft.zdmk.model.transformations.impl.AddProperty;
import nl.zeesoft.zdmk.model.transformations.impl.IncrementVersion;

/**
 * A SelfModel can be used to describe models within themselves.
 */
public class ModelSelf extends Model {
	/**
	 * Initializes the model.
	 * 
	 * @return The error message if applicable
	 */
	public String initializeSelf() {
		return initializeSelf("","");
	}

	/**
	 * Initializes the model.
	 * 
	 * @param packageName The self model package name
	 * @param classNamePrefix The self model class name prefix
	 * @return The error message if applicable
	 */
	public String initializeSelf(String packageName,String classNamePrefix) {
		if (packageName.length()==0) {
			packageName = this.getClass().getPackage().getName();
		}
		if (classNamePrefix.length()==0) {
			classNamePrefix = "Model";
		}
		List<TransformationObject> transformations = new ArrayList<TransformationObject>();

		transformations.add(new AddPackage(packageName));

		// Model object
		transformations.add(new AddClass(packageName,classNamePrefix + "Object",true));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Object","id",Long.class.getName()));

		// Model
		transformations.add(new AddClass(packageName,classNamePrefix + "Self",false,packageName,classNamePrefix + "Object"));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Self","versions",packageName + "." + classNamePrefix + "Version",true));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Self","packages",packageName + "." + classNamePrefix + "Package",true));

		// Model named object
		transformations.add(new AddClass(packageName,classNamePrefix + "NamedObject",true,packageName,classNamePrefix + "Object"));
		transformations.add(new AddProperty(packageName,classNamePrefix + "NamedObject","name"));

		// Version
		transformations.add(new AddClass(packageName,classNamePrefix + "Version"));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Version","number",Integer.class.getName()));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Version","log",StringBuilder.class.getName()));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Version","transformations",packageName + "." + classNamePrefix + "Transformation",true));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Version","initialTransformations",packageName + "." + classNamePrefix + "Transformation",true));

		// Transformation
		transformations.add(new AddClass(packageName,classNamePrefix + "Transformation"));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Transformation","parameters",packageName + "." + classNamePrefix + "TransformationParameter",true));

		// TransformationParameter
		transformations.add(new AddClass(packageName,classNamePrefix + "TransformationParameter"));
		transformations.add(new AddProperty(packageName,classNamePrefix + "TransformationParameter","name"));
		transformations.add(new AddProperty(packageName,classNamePrefix + "TransformationParameter","value"));
		
		// Package
		transformations.add(new AddClass(packageName,classNamePrefix + "Package",false,packageName,classNamePrefix + "NamedObject"));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Package","classes",packageName + "." + classNamePrefix + "Class",true));

		// Class
		transformations.add(new AddClass(packageName,classNamePrefix + "Class",false,packageName,classNamePrefix + "NamedObject"));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Class","properties",packageName + "." + classNamePrefix + "Property",true));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Class","extendsClass",packageName + "." + classNamePrefix + "Class"));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Class","abstract",Boolean.class.getName()));

		// Property
		transformations.add(new AddClass(packageName,classNamePrefix + "Property",false,packageName,classNamePrefix + "NamedObject"));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Property","type"));
		transformations.add(new AddProperty(packageName,classNamePrefix + "Property","list",Boolean.class.getName()));
		
		transformations.add(new IncrementVersion());
		return applyTransformations(transformations);
	}
}
