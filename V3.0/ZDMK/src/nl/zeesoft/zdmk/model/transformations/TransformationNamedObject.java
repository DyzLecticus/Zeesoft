package nl.zeesoft.zdmk.model.transformations;

import nl.zeesoft.zdmk.model.transformations.TransformationObject;

public abstract class TransformationNamedObject extends TransformationObject {
	public abstract String getNameParameterDescription();

	public TransformationNamedObject() {
	}

	public TransformationNamedObject(String name) {
		setName(name);
	}
	
	@Override
	protected void initializeParameters() {
		addParameter("name",true,getNameParameterDescription());
	}
	
	public String getName() {
		return getParameterValue("name");
	}

	public void setName(String name) {
		setParameterValue("name",name);
	}
}
