package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationNamedPackageObject;
import nl.zeesoft.zdmk.model.transformations.TransformationParameter;

public class AddClass extends TransformationNamedPackageObject {
	public AddClass() {
	}

	public AddClass(String packageName,String name) {
		super(packageName,name);
	}

	public AddClass(String packageName,String name,boolean abstr) {
		super(packageName,name);
		setAbstract("" + abstr);
	}

	public AddClass(String packageName,String name,boolean abstr,String extendsPackageName,String extendsClassName) {
		super(packageName,name);
		setAbstract("" + abstr);
		setExtendsPackageName(extendsPackageName);
		setExtendsClassName(extendsClassName);
	}
	
	@Override
	public String getDescription() {
		return "Adds a class to a package.";
	}
	
	@Override
	public String getNameParameterDescription() {
		return "The name of the class (without the package name prefix).";
	}
	
	@Override
	protected void initializeParameters() {
		super.initializeParameters();
		addParameter("extendsPackageName",false,"The package name of the class to extend.");
		addParameter("extendsClassName",false,"The class name of the class to extend (without the package name prefix).");
		addParameter("abstract",false,"Indicates the class is abstract. Accepts 'true' or 'false'. Defaults to 'false' if omitted.");
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
	
	public String getAbstract() {
		return getParameterValue("abstract");
	}

	public void setAbstract(String abstr) {
		setParameterValue("abstract",abstr);
	}

	@Override
	protected String checkParameter(TransformationParameter param) {
		String error = "";
		if (param.getName().equals("abstract") && param.getValue().length()>0) {
			if (!param.getValue().equals("true") && !param.getValue().equals("false")) {
				error = "Parameter " + param.getName() + "; value must be either 'true' or 'false'";
			}
		}
		return error;
	}
}
