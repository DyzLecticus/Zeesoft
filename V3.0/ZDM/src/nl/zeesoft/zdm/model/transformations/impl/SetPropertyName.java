package nl.zeesoft.zdm.model.transformations.impl;

import nl.zeesoft.zdm.model.transformations.TransformationNamedObjectSetName;
import nl.zeesoft.zdm.model.transformations.TransformationNamedPackageClassObject;

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

