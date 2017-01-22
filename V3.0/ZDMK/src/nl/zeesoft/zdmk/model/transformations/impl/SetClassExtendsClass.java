package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationNamedPackageObject;

public class SetClassExtendsClass extends TransformationNamedPackageObject {
	public SetClassExtendsClass() {
	}
	
	public SetClassExtendsClass(String packageName,String name,String extendsPackageName,String extendsClassName) {
		super(packageName,name);
		setExtendsPackageName(extendsPackageName);
		setExtendsClassName(extendsClassName);
	}
	
	@Override
	public String getDescription() {
		return "Sets the extendsClass property of a class to the specified value.";
	}

	@Override
	public String getNameParameterDescription() {
		return "The name of the class (without the package name prefix).";
	}

	@Override
	protected void initializeParameters() {
		super.initializeParameters();
		addParameter("extendsPackageName",true,"The package name of the class to extend.");
		addParameter("extendsClassName",true,"The class name of the class to extend (without the package name prefix).");
	}
	
	public String getExtendsPackageName() {
		return getParameterValue("extendsPackageName");
	}

	public void setExtendsPackageName(String extendsPackageName) {
		setParameterValue("extendsPackageName",extendsPackageName);
	}
	
	public String getExtendsClassName() {
		return getParameterValue("extendsClassName");
	}

	public void setExtendsClassName(String extendsClassName) {
		setParameterValue("extendsClassName",extendsClassName);
	}	
}
