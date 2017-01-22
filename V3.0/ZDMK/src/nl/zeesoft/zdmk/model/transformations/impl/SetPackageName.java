package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationNamedObject;
import nl.zeesoft.zdmk.model.transformations.TransformationNamedObjectSetName;

public class SetPackageName extends TransformationNamedObject implements TransformationNamedObjectSetName {
	public SetPackageName() {
	}
	
	public SetPackageName(String name, String newName) {
		super(name);
		setNewName(newName);
	}
	
	@Override
	public String getDescription() {
		return "Sets the name property of a package to the specified value.";
	}

	@Override
	public String getNameParameterDescription() {
		return "The current name of the package.";
	}

	@Override
	protected void initializeParameters() {
		super.initializeParameters();
		addParameter("newName",true,"The new name of the package.");
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
