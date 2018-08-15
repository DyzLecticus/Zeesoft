package nl.zeesoft.zsd.entity;

import nl.zeesoft.zsd.BaseConfiguration;

public class UniversalFrowny extends UniversalSmiley {
	@Override
	public String getType() {
		return BaseConfiguration.TYPE_FROWNY;
	}
	
	@Override
	protected void addExpression(String eyes,String nose) {
		addExpressionValue(eyes + nose + "(");
		addExpressionValue(")" + nose + eyes);
		addExpressionValue(eyes + nose + "[");
		addExpressionValue("]" + nose + eyes);
		addExpressionValue(eyes + nose + "{");
		addExpressionValue("}" + nose + eyes);
	}
}
