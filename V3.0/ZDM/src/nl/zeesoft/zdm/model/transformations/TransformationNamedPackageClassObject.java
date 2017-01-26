package nl.zeesoft.zdm.model.transformations;

public abstract class TransformationNamedPackageClassObject extends TransformationNamedPackageObject {
	public TransformationNamedPackageClassObject() {
	}

	public TransformationNamedPackageClassObject(String packageName,String className,String name) {
		super(packageName,name);
		setClassName(className);
	}
	
	@Override
	protected void initializeParameters() {
		addParameter("className",true,"The name of the class (without the package name prefix).");
		super.initializeParameters();
	}

	public String getClassName() {
		return getParameterValue("className");
	}

	public void setClassName(String name) {
		setParameterValue("className",name);
	}
}
