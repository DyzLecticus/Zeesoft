package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationNamedObjectSetName;
import nl.zeesoft.zdmk.model.transformations.TransformationNamedPackageClassObject;

public class SetPropertyName extends TransformationNamedPackageClassObject implements TransformationNamedObjectSetName {
	public SetPropertyName() {
	}
	
	public SetPropertyName(String packageName,String className,String name,String newName) {
		super(packageName,className,name);
		this.setNewName(newName);
	}
	
	@Override
	public String getDescription() {
		return "Sets the list property of a property to the specified value.";
	}

	@Override
	public String getNameParameterDescription() {
		return "The current name of the property.";
	}
	
	@Override
	protected void initializeParameters() {
		super.initializeParameters();
		addParameter("newName",true,"The new name of the property.");
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

