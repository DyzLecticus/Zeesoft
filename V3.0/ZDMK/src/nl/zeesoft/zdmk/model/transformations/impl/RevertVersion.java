package nl.zeesoft.zdmk.model.transformations.impl;

import nl.zeesoft.zdmk.model.transformations.TransformationObject;
import nl.zeesoft.zdmk.model.transformations.TransformationParameter;

public class RevertVersion extends TransformationObject {
	public RevertVersion() {
	}
	
	public RevertVersion(int number) {
		setNumber("" + number);
	}
	
	@Override
	public String getDescription() {
		return "Reverts the model state back to the initial state of a certain model version. Removes the specified version and all higher versions from the model.";
	}
	
	@Override
	protected void initializeParameters() {
		addParameter("number",true,"The number of the model version to revert to. Accepts integer values only.");
	}
	
	public String getNumber() {
		return getParameterValue("number");
	}

	public void setNumber(String number) {
		setParameterValue("number",number);
	}

	@Override
	protected String checkParameter(TransformationParameter param) {
		String error = "";
		if (param.getName().equals("number") && param.getValue().length()>0) {
			int num = 0;
			try {
				num = Integer.parseInt(param.getValue());
			} catch (NumberFormatException e) {
				error = "Parameter " + param.getName() + "; value must be an integer";
			}
			if (error.length()==0 && num<=0) {
				error = "Parameter " + param.getName() + "; value must greater or equal to 0";
			}
		}
		return error;
	}
}
