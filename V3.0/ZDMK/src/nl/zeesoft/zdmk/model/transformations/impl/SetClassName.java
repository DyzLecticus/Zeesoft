package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationNamedObjectSetName;
import nl.zeesoft.zdmk.model.transformations.TransformationNamedPackageObject;

public class SetClassName extends TransformationNamedPackageObject implements TransformationNamedObjectSetName {
	public SetClassName() {
	}
	
	public SetClassName(String packageName,String name,String newName) {
		super(packageName,name);
		setNewName(newName);
	}
	
	@Override
	public String getDescription() {
		return "Sets the name property of a class to the specified value.";
	}

	@Override
	public String getNameParameterDescription() {
		return "The current name of the class (without the package name prefix).";
	}

	@Override
	protected void initializeParameters() {
		super.initializeParameters();
		addParameter("newName",true,"The new name of the class (without the package name prefix).");
	}
	
	@Override
	public String getNewName() {
		return getParameterValue("newName");
	}
	
	@Override
	public void setNewName(String newName) {
		setParameterValue("newName",newName);
	}
}
