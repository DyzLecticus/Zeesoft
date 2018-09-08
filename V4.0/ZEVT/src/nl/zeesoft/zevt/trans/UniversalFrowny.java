package nl.zeesoft.zevt.trans;

public class UniversalFrowny extends UniversalSmiley {
	@Override
	public String getType() {
		return TYPE_FROWNY;
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
