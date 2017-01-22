package nl.zeesoft.zdmk.model.transformations;

public abstract class TransformationNamedPackageObject extends TransformationNamedObject {
	public TransformationNamedPackageObject() {
	}

	public TransformationNamedPackageObject(String packageName,String name) {
		super(name);
		setPackageName(packageName);
	}
	
	@Override
	protected void initializeParameters() {
		addParameter("packageName",true,"The name of the package.");
		super.initializeParameters();
	}

	public String getPackageName() {
		return getParameterValue("packageName");
	}

	public void setPackageName(String name) {
		setParameterValue("packageName",name);
	}
}
