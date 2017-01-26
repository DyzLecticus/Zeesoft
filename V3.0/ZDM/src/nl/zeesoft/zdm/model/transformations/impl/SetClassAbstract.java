package nl.zeesoft.zdm.model.transformations.impl;

import nl.zeesoft.zdm.model.transformations.TransformationNamedPackageObject;
import nl.zeesoft.zdm.model.transformations.TransformationParameter;

public class SetClassAbstract extends TransformationNamedPackageObject {
	public SetClassAbstract() {
	}
	
	public SetClassAbstract(String packageName,String name,boolean abstr) {
		super(packageName,name);
		setAbstract("" + abstr);
	}
	
	@Override
	public String getDescription() {
		return "Sets the abstract property of a class to the specified value.";
	}

	@Override
	public String getNameParameterDescription() {
		return "The name of the class (without the package name prefix).";
	}

	@Override
	protected void initializeParameters() {
		super.initializeParameters();
		addParameter("abstract",true,"Indicates the class is abstract. Accepts 'true' or 'false'.");
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
